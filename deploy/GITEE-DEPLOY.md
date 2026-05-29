# Gitee + Webhook 自动化部署指南

## 目录

- [方案概述](#方案概述)
- [服务器配置要求](#服务器配置要求)
- [快速开始](#快速开始)
- [详细配置步骤](#详细配置步骤)
- [使用说明](#使用说明)
- [故障排查](#故障排查)

---

## 方案概述

### 工作原理

```
开发者推送代码
    ↓
Gitee 仓库接收
    ↓
Gitee 触发 Webhook
    ↓
阿里云服务器接收 Webhook (webhook_server.py)
    ↓
自动执行部署脚本 (auto-deploy.sh)
    ↓
应用自动更新
```

### 优势

- ✅ **完全免费**：无需付费 CI/CD 服务
- ✅ **国内稳定**：Gitee 在国内访问快速稳定
- ✅ **轻量级**：Webhook 服务内存占用 < 50MB
- ✅ **配置简单**：30分钟完成配置
- ✅ **自动化**：代码推送后自动部署

---

## 服务器配置要求

### 推荐配置

```yaml
服务器配置：
  - vCPU: 2核
  - 内存: 2GB
  - 存储: 40-60GB
  - 带宽: 3Mbps
  - 系统: Ubuntu 22.04 LTS
  - 月费: 45-60元

服务商选择：
  - 阿里云轻量应用服务器
  - 腾讯云轻量应用服务器
  - 华为云弹性云服务器
```

### 端口要求

需要开放以下端口：

- `5000`: Webhook 接收服务
- `80`: 前端 Web 服务
- `8080`: 后端 API 服务
- `22`: SSH 管理（建议修改默认端口）

---

## 快速开始

### 一键部署脚本

在本地运行以下命令，将自动配置文件推送到 Gitee：

```bash
# 1. 提交配置文件
git add deploy/
git commit -m "feat: 添加 Gitee Webhook 自动部署配置"
git push origin master
```

### 服务器端快速配置

登录您的阿里云服务器，执行以下命令：

```bash
# 下载并执行一键配置脚本
curl -fsSL https://gitee.com/jinyue093220/cixiu/raw/master/deploy/setup-webhook.sh | bash
```

> 注：一键脚本会自动完成所有配置，包括安装依赖、配置服务等

---

## 详细配置步骤

### 步骤 1：准备服务器

#### 1.1 购买服务器

访问阿里云官网，购买轻量应用服务器：

- 选择地域：西南1（成都）或就近地域
- 选择镜像：Ubuntu 22.04 LTS（系统镜像）
- 选择配置：2核2GB，40GB ESSD
- 选择带宽：3Mbps

#### 1.2 登录服务器

```bash
# 使用 SSH 登录
ssh root@your-server-ip

# 首次登录建议修改密码
passwd
```

#### 1.3 更新系统

```bash
# 更新软件包列表
apt update && apt upgrade -y

# 安装基础工具
apt install -y curl wget vim git ufw
```

### 步骤 2：安装依赖软件

#### 2.1 安装 Docker

```bash
# 安装 Docker
curl -fsSL https://get.docker.com | bash

# 将当前用户添加到 docker 组
usermod -aG docker $USER

# 启动 Docker
systemctl start docker
systemctl enable docker

# 验证安装
docker --version
```

#### 2.2 安装 Docker Compose

```bash
# 安装 Docker Compose 插件
apt install docker-compose-plugin -y

# 验证安装
docker compose version
```

#### 2.3 安装 Python 和 Flask

```bash
# 安装 Python3 和 pip
apt install -y python3 python3-pip

# 安装 Flask
pip3 install flask

# 验证安装
python3 --version
pip3 list | grep -i flask
```

### 步骤 3：克隆项目到服务器

#### 3.1 创建项目目录

```bash
# 创建项目根目录
mkdir -p /opt/cixiu
cd /opt/cixiu
```

#### 3.2 配置 Git 访问

**方式一：使用 HTTPS（推荐新手）**

```bash
# 克隆项目
git clone https://gitee.com/jinyue093220/cixiu.git .

# 首次会要求输入用户名和密码
# 配置凭据保存（避免每次输入）
git config --global credential.helper store
```

**方式二：使用 SSH Key（推荐）**

```bash
# 生成 SSH 密钥
ssh-keygen -t ed25519 -C "server-deploy" -f ~/.ssh/gitee_deploy

# 查看公钥
cat ~/.ssh/gitee_deploy.pub

# 复制公钥，添加到 Gitee
# 访问：https://gitee.com/profile/sshkeys
# 点击"添加公钥"，粘贴公钥内容

# 配置 SSH
cat >> ~/.ssh/config <<EOF
Host gitee.com
    HostName gitee.com
    User git
    IdentityFile ~/.ssh/gitee_deploy
EOF

chmod 600 ~/.ssh/config

# 测试连接
ssh -T git@gitee.com

# 克隆项目（使用 SSH）
git clone git@gitee.com:jinyue093220/cixiu.git /opt/cixiu
cd /opt/cixiu
```

### 步骤 4：配置环境变量

```bash
cd /opt/cixiu

# 创建 .env 文件
cat > .env <<'EOF'
# ==================== 数据库配置 ====================
MYSQL_ROOT_PASSWORD=your_strong_password_2024
MYSQL_DATABASE=cixiu
MYSQL_USER=cixiu
MYSQL_PASSWORD=your_mysql_password_2024

# ==================== Redis 配置 ====================
REDIS_PASSWORD=your_redis_password_2024

# ==================== 后端配置 ====================
# JWT 密钥（至少32位随机字符串）
JWT_SECRET=your_jwt_secret_key_at_least_32_characters_long_2024

# 阿里云 OSS 配置
ALIYUN_OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
ALIYUN_OSS_BUCKET=your-bucket-name
ALIYUN_OSS_ACCESS_KEY_ID=your_access_key_id
ALIYUN_OSS_ACCESS_KEY_SECRET=your_access_key_secret

# ==================== 前端配置 ====================
# 后端 API 地址（替换为您的服务器IP或域名）
VITE_API_BASE_URL=

# ==================== 其他配置 ====================
# 环境标识
SPRING_PROFILES_ACTIVE=prod
EOF

# 设置文件权限（重要！）
chmod 600 .env

# 编辑配置（请务必修改为真实值）
vim .env
```

**⚠️ 重要提示**：

1. 必须修改所有密码为强密码
2. 必须配置真实的阿里云 OSS 信息
3. 必须将 `your-server-ip` 替换为实际 IP

### 步骤 5：首次手动部署

在配置自动部署前，先手动部署一次验证环境：

```bash
cd /opt/cixiu

# 给部署脚本执行权限
chmod +x deploy/auto-deploy.sh

# 执行首次部署
bash deploy/auto-deploy.sh
```

等待部署完成（约 3-5 分钟），访问验证：

- 前端：`http://your-server-ip/`
- 后端健康检查：`http://your-server-ip:8080/health`
- API 文档：`http://your-server-ip:8080/swagger-ui.html`

### 步骤 6：配置 Webhook 服务

#### 6.1 设置 Webhook 密钥

```bash
# 生成随机密钥
openssl rand -hex 32

# 复制生成的密钥，稍后在 Gitee 配置时使用
```

#### 6.2 编辑 systemd 服务文件

```bash
# 编辑服务配置
vim /opt/cixiu/deploy/gitee-webhook.service

# 修改以下内容：
# 1. User=root （如果使用其他用户，请修改）
# 2. WEBHOOK_SECRET=刚才生成的密钥
```

#### 6.3 安装并启动服务

```bash
# 复制服务文件到 systemd 目录
cp /opt/cixiu/deploy/gitee-webhook.service /etc/systemd/system/

# 重新加载 systemd
systemctl daemon-reload

# 启动服务
systemctl start gitee-webhook

# 设置开机自启
systemctl enable gitee-webhook

# 查看服务状态
systemctl status gitee-webhook

# 查看服务日志
journalctl -u gitee-webhook -f
```

#### 6.4 测试 Webhook 服务

```bash
# 测试健康检查接口
curl http://localhost:5000/health

# 应该返回类似：
# {
#   "status": "ok",
#   "service": "gitee-webhook",
#   ...
# }
```

### 步骤 7：配置防火墙

```bash
# 启用防火墙
ufw enable

# 允许 SSH（重要！先允许 SSH 避免被锁定）
ufw allow 22/tcp

# 允许 Webhook 端口
ufw allow 5000/tcp

# 允许 Web 服务端口
ufw allow 80/tcp
ufw allow 8080/tcp

# 查看规则
ufw status

# 如果使用阿里云，还需要在安全组开放端口
```

### 步骤 8：配置 Gitee Webhook

#### 8.1 登录 Gitee

访问您的仓库：`https://gitee.com/jinyue093220/cixiu`

#### 8.2 添加 Webhook

1. 点击 **管理** → **WebHooks**
2. 点击 **添加 WebHook**
3. 填写配置：

```
URL: http://your-server-ip:5000/webhook
密码: 刚才生成的 WEBHOOK_SECRET
勾选事件：Push
```

4. 点击 **添加**

#### 8.3 测试 Webhook

在 Webhook 列表中，点击 **测试** 按钮：

- 如果返回 `200`，说明配置成功
- 查看服务器日志：`journalctl -u gitee-webhook -n 50`

### 步骤 9：测试自动部署

#### 9.1 本地修改代码

```bash
# 在本地修改任意文件
echo "# 测试自动部署" >> README.md

# 提交并推送
git add README.md
git commit -m "test: 测试 Gitee Webhook 自动部署"
git push origin master
```

#### 9.2 观察服务器

```bash
# 实时查看 Webhook 服务日志
journalctl -u gitee-webhook -f

# 应该看到：
# - 收到 Webhook 请求
# - 执行部署脚本
# - 部署成功
```

#### 9.3 验证部署

访问应用，确认更新已生效。

---

## 使用说明

### 日常开发流程

#### 标准流程（推荐）

```bash
# 1. 创建功能分支
git checkout -b feature-new-function

# 2. 开发代码...

# 3. 提交代码
git add .
git commit -m "feat: 新增某某功能"

# 4. 推送到 Gitee
git push origin feature-new-function

# 5. 在 Gitee 创建 Pull Request 到 master

# 6. 代码审查通过后，合并 PR

# 7. 合并到 master 后自动触发部署
```

#### 快速流程（适合个人项目）

```bash
# 1. 直接在 master 分支开发

# 2. 提交代码
git add .
git commit -m "feat: 更新功能"

# 3. 推送（自动触发部署）
git push origin master

# 4. 等待 3-5 分钟，访问应用查看更新
```

### 查看部署日志

```bash
# 查看 Webhook 服务日志（最近50条）
journalctl -u gitee-webhook -n 50

# 实时查看日志
journalctl -u gitee-webhook -f

# 查看今天的日志
journalctl -u gitee-webhook --since today

# 查看应用容器日志
docker compose logs -f
```

### 手动触发部署

如果需要手动重新部署：

```bash
# SSH 登录服务器
ssh root@your-server-ip

# 进入项目目录
cd /opt/cixiu

# 拉取最新代码
git pull origin master

# 执行部署
bash deploy/auto-deploy.sh
```

### 管理 Webhook 服务

```bash
# 查看服务状态
systemctl status gitee-webhook

# 启动服务
systemctl start gitee-webhook

# 停止服务
systemctl stop gitee-webhook

# 重启服务
systemctl restart gitee-webhook

# 查看日志
journalctl -u gitee-webhook -f

# 禁用自启动
systemctl disable gitee-webhook

# 启用自启动
systemctl enable gitee-webhook
```

---

## 故障排查

### 问题 1: Webhook 服务无法访问

**症状**：

- Gitee Webhook 测试失败
- 返回连接超时或 502

**排查步骤**：

```bash
# 1. 检查服务是否运行
systemctl status gitee-webhook

# 2. 检查端口是否监听
netstat -tunlp | grep 5000

# 3. 检查防火墙
ufw status

# 4. 测试本地访问
curl http://localhost:5000/health

# 5. 查看详细日志
journalctl -u gitee-webhook -n 100
```

**解决方法**：

```bash
# 如果服务未运行
systemctl start gitee-webhook

# 如果端口未开放
ufw allow 5000/tcp

# 如果使用阿里云，检查安全组是否开放 5000 端口
```

### 问题 2: 收到 Webhook 但未触发部署

**症状**：

- Webhook 返回 200
- 但应用未更新

**排查步骤**：

```bash
# 查看 Webhook 日志
journalctl -u gitee-webhook -n 50

# 检查是否是 master 分支
# 默认只有 master 分支才触发部署
```

**解决方法**：

如果需要其他分支也触发部署：

```bash
# 编辑服务配置
vim /etc/systemd/system/gitee-webhook.service

# 修改 ALLOWED_BRANCHES
Environment="ALLOWED_BRANCHES=master,develop,test"

# 重启服务
systemctl daemon-reload
systemctl restart gitee-webhook
```

### 问题 3: 部署脚本执行失败

**症状**：

- Webhook 收到请求
- 但部署失败，返回 500

**排查步骤**：

```bash
# 查看详细错误
journalctl -u gitee-webhook -n 100

# 手动执行部署脚本
cd /opt/cixiu
bash deploy/auto-deploy.sh

# 检查 Docker 是否正常
docker ps
docker compose ps
```

**常见原因**：

1. Git 拉取失败（检查 SSH Key 或凭据）
2. Docker 构建失败（检查 Dockerfile）
3. 环境变量未配置（检查 .env 文件）
4. 磁盘空间不足（`df -h`）

### 问题 4: 签名验证失败

**症状**：

- Webhook 返回 403
- 日志显示 "签名验证失败"

**解决方法**：

```bash
# 1. 检查密钥是否一致
# 查看服务中的密钥
systemctl cat gitee-webhook | grep WEBHOOK_SECRET

# 2. 在 Gitee 中重新配置密钥
# 确保与服务中的 WEBHOOK_SECRET 一致

# 3. 如果不需要签名验证（不推荐）
# 编辑服务配置，将 WEBHOOK_SECRET 设为空
Environment="WEBHOOK_SECRET="

systemctl daemon-reload
systemctl restart gitee-webhook
```

### 问题 5: 部署超时

**症状**：

- 部署时间过长
- 日志显示 "部署超时"

**解决方法**：

```bash
# 增加超时时间
vim /etc/systemd/system/gitee-webhook.service

# 修改超时设置（默认600秒=10分钟）
Environment="DEPLOY_TIMEOUT=1200"

# 重启服务
systemctl daemon-reload
systemctl restart gitee-webhook
```

### 问题 6: Python 或 Flask 未安装

**症状**：

- 服务启动失败
- 日志显示 "No module named 'flask'"

**解决方法**：

```bash
# 安装 Flask
pip3 install flask

# 重启服务
systemctl restart gitee-webhook
```

---

## 高级配置

### 配置多分支部署

编辑 `/etc/systemd/system/gitee-webhook.service`：

```ini
# 允许多个分支触发部署
Environment="ALLOWED_BRANCHES=master,develop,test"
```

### 添加部署通知

在 `deploy/auto-deploy.sh` 末尾添加：

```bash
# 企业微信通知
send_wechat_notification() {
    curl 'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=YOUR_KEY' \
      -H 'Content-Type: application/json' \
      -d '{
        "msgtype": "text",
        "text": {
          "content": "✅ 部署成功\n项目: cixiu\n分支: master\n时间: '"$(date '+%Y-%m-%d %H:%M:%S')"'"
        }
      }'
}

# 在脚本成功结束时调用
send_wechat_notification
```

### 配置 HTTPS（使用 Nginx）

```bash
# 安装 Nginx
apt install nginx -y

# 配置反向代理
cat > /etc/nginx/sites-available/webhook <<'EOF'
server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;

    location /webhook {
        proxy_pass http://127.0.0.1:5000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
EOF

ln -s /etc/nginx/sites-available/webhook /etc/nginx/sites-enabled/
nginx -t
systemctl reload nginx
```

---

## 安全建议

### 1. 配置 Webhook 密钥

**必须配置** `WEBHOOK_SECRET`，防止恶意请求触发部署。

### 2. 限制访问 IP

使用防火墙限制只允许 Gitee 的 IP 访问：

```bash
# Gitee Webhook IP 段（需查询最新）
ufw allow from 212.64.62.0/24 to any port 5000
```

### 3. 使用 HTTPS

生产环境建议使用 Nginx + SSL 证书。

### 4. 定期备份

```bash
# 定期备份数据库（已包含在 auto-deploy.sh 中）
# 备份文件位置：/opt/cixiu/backups/
```

### 5. 监控日志

```bash
# 配置日志轮转
vim /etc/logrotate.d/gitee-webhook
```

---

## 常用命令速查

```bash
# ========== 服务管理 ==========
systemctl status gitee-webhook    # 查看服务状态
systemctl start gitee-webhook     # 启动服务
systemctl stop gitee-webhook      # 停止服务
systemctl restart gitee-webhook   # 重启服务
journalctl -u gitee-webhook -f    # 查看实时日志

# ========== 应用管理 ==========
cd /opt/cixiu                      # 进入项目目录
docker compose ps                  # 查看容器状态
docker compose logs -f             # 查看应用日志
bash deploy/auto-deploy.sh        # 手动部署

# ========== Git 操作 ==========
git pull origin master             # 拉取最新代码
git status                         # 查看状态
git log -5                         # 查看最近5次提交

# ========== 系统监控 ==========
df -h                              # 查看磁盘空间
free -h                            # 查看内存使用
htop                               # 查看进程（需安装）
```

---

## 总结

通过 Gitee + Webhook 自动部署方案，您可以：

✅ **节省成本**：仅需一台 2核2GB 服务器（45-60元/月）
✅ **提高效率**：代码推送后自动部署，3-5分钟上线
✅ **国内稳定**：Gitee 在国内访问快速可靠
✅ **配置简单**：30分钟完成全部配置
✅ **安全可靠**：自动备份、签名验证、日志记录

### 下一步行动

1. ✅ 按照本指南完成服务器配置
2. ✅ 测试自动部署流程
3. ✅ 配置通知（可选）
4. ✅ 设置监控和告警（可选）

---

**文档版本**: v1.0
**最后更新**: 2025-10-24
**维护者**: 噤玥
