#!/bin/bash

###############################################################################
# Gitee Webhook 自动部署 - 一键配置脚本
#
# 使用方法：
#   bash setup-webhook.sh
#
# 或者：
#   curl -fsSL https://gitee.com/jinyue093220/cixiu/raw/master/deploy/setup-webhook.sh | bash
###############################################################################

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 配置变量
PROJECT_DIR="/opt/cixiu"
GITEE_REPO="https://gitee.com/jinyue093220/cixiu.git"
WEBHOOK_SECRET=""

# 显示欢迎信息
show_welcome() {
    clear
    echo -e "${GREEN}"
    echo "========================================="
    echo "  Gitee Webhook 自动部署配置向导"
    echo "========================================="
    echo -e "${NC}"
    echo ""
    echo "本脚本将帮助您完成以下配置："
    echo "  1. 安装依赖软件 (Docker, Python, Flask)"
    echo "  2. 克隆项目到服务器"
    echo "  3. 配置 Webhook 服务"
    echo "  4. 配置防火墙"
    echo "  5. 启动服务"
    echo ""
    read -p "按 Enter 继续，或 Ctrl+C 取消..."
}

# 检查是否为 root
check_root() {
    if [[ $EUID -ne 0 ]]; then
       log_error "请使用 root 用户运行此脚本"
       exit 1
    fi
}

# 检测系统
detect_system() {
    log_info "检测系统信息..."

    if [ -f /etc/os-release ]; then
        . /etc/os-release
        OS=$NAME
        VER=$VERSION_ID
    else
        log_error "无法检测系统版本"
        exit 1
    fi

    log_success "系统: $OS $VER"
}

# 更新系统
update_system() {
    log_info "更新系统软件包..."

    apt update -y
    apt upgrade -y

    log_success "系统更新完成"
}

# 安装基础工具
install_basic_tools() {
    log_info "安装基础工具..."

    apt install -y curl wget vim git ufw net-tools

    log_success "基础工具安装完成"
}

# 安装 Docker
install_docker() {
    log_info "检查 Docker 安装状态..."

    if command -v docker &> /dev/null; then
        log_success "Docker 已安装: $(docker --version)"
    else
        log_info "安装 Docker..."
        curl -fsSL https://get.docker.com | bash
        systemctl start docker
        systemctl enable docker
        log_success "Docker 安装完成"
    fi
}

# 安装 Docker Compose
install_docker_compose() {
    log_info "检查 Docker Compose 安装状态..."

    if docker compose version &> /dev/null; then
        log_success "Docker Compose 已安装: $(docker compose version)"
    else
        log_info "安装 Docker Compose..."
        apt install -y docker-compose-plugin
        log_success "Docker Compose 安装完成"
    fi
}

# 安装 Python 和 Flask
install_python() {
    log_info "检查 Python 安装状态..."

    if command -v python3 &> /dev/null; then
        log_success "Python3 已安装: $(python3 --version)"
    else
        log_info "安装 Python3..."
        apt install -y python3 python3-pip
        log_success "Python3 安装完成"
    fi

    log_info "安装 Flask..."
    pip3 install flask -q
    log_success "Flask 安装完成"
}

# 克隆项目
clone_project() {
    log_info "克隆项目到服务器..."

    if [ -d "$PROJECT_DIR/.git" ]; then
        log_warning "项目目录已存在，跳过克隆"
        cd "$PROJECT_DIR"
        git pull origin master
    else
        log_info "克隆项目..."
        mkdir -p "$(dirname $PROJECT_DIR)"

        echo ""
        echo "请选择认证方式："
        echo "  1. HTTPS (需要用户名密码)"
        echo "  2. SSH (需要配置 SSH Key)"
        read -p "请选择 [1/2]: " auth_choice

        case $auth_choice in
            1)
                git clone $GITEE_REPO $PROJECT_DIR
                cd $PROJECT_DIR
                git config credential.helper store
                ;;
            2)
                SSH_REPO="git@gitee.com:jinyue093220/cixiu.git"

                if [ ! -f ~/.ssh/gitee_deploy ]; then
                    log_info "生成 SSH 密钥..."
                    ssh-keygen -t ed25519 -C "server-deploy" -f ~/.ssh/gitee_deploy -N ""

                    echo ""
                    log_warning "请将以下公钥添加到 Gitee:"
                    echo ""
                    cat ~/.ssh/gitee_deploy.pub
                    echo ""
                    log_info "访问: https://gitee.com/profile/sshkeys"
                    read -p "添加完成后按 Enter 继续..."

                    cat >> ~/.ssh/config <<EOF
Host gitee.com
    HostName gitee.com
    User git
    IdentityFile ~/.ssh/gitee_deploy
EOF
                    chmod 600 ~/.ssh/config
                fi

                git clone $SSH_REPO $PROJECT_DIR
                cd $PROJECT_DIR
                ;;
            *)
                log_error "无效选择"
                exit 1
                ;;
        esac
    fi

    log_success "项目克隆完成"
}

# 配置环境变量
configure_env() {
    log_info "配置环境变量..."

    cd $PROJECT_DIR

    if [ -f .env ]; then
        log_warning ".env 文件已存在，跳过配置"
    else
        log_info "创建 .env 文件..."

        read -p "请输入服务器 IP 地址: " server_ip

        cat > .env <<EOF
# ==================== 数据库配置 ====================
MYSQL_ROOT_PASSWORD=$(openssl rand -base64 24)
MYSQL_DATABASE=cixiu
MYSQL_USER=cixiu
MYSQL_PASSWORD=$(openssl rand -base64 24)

# ==================== Redis 配置 ====================
REDIS_PASSWORD=$(openssl rand -base64 24)

# ==================== 后端配置 ====================
JWT_SECRET=$(openssl rand -base64 32)

# 阿里云 OSS 配置（请手动修改）
ALIYUN_OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
ALIYUN_OSS_BUCKET=your-bucket-name
ALIYUN_OSS_ACCESS_KEY_ID=your_access_key_id
ALIYUN_OSS_ACCESS_KEY_SECRET=your_access_key_secret

# ==================== 前端配置 ====================
VITE_API_BASE_URL=

# ==================== 其他配置 ====================
SPRING_PROFILES_ACTIVE=prod
EOF

        chmod 600 .env

        log_success ".env 文件已创建"
        log_warning "请编辑 .env 文件，修改阿里云 OSS 配置"
        log_info "编辑命令: vim $PROJECT_DIR/.env"
    fi
}

# 首次部署
initial_deploy() {
    log_info "执行首次部署..."

    cd $PROJECT_DIR
    chmod +x deploy/auto-deploy.sh

    log_warning "这可能需要几分钟时间..."
    bash deploy/auto-deploy.sh

    log_success "首次部署完成"
}

# 配置 Webhook 服务
configure_webhook() {
    log_info "配置 Webhook 服务..."

    # 生成 Webhook 密钥
    WEBHOOK_SECRET=$(openssl rand -hex 32)

    log_info "生成的 Webhook 密钥: $WEBHOOK_SECRET"
    log_warning "请保存此密钥，稍后在 Gitee 配置 Webhook 时使用"

    # 创建 systemd 服务文件
    cat > /etc/systemd/system/gitee-webhook.service <<EOF
[Unit]
Description=Gitee Webhook Service for Auto Deployment
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=$PROJECT_DIR/deploy
Environment="WEBHOOK_SECRET=$WEBHOOK_SECRET"
Environment="DEPLOY_SCRIPT=$PROJECT_DIR/deploy/auto-deploy.sh"
Environment="PROJECT_DIR=$PROJECT_DIR"
Environment="ALLOWED_BRANCHES=master"
Environment="DEPLOY_TIMEOUT=600"
Environment="LOG_LEVEL=INFO"
ExecStart=/usr/bin/python3 $PROJECT_DIR/deploy/webhook_server.py
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF

    # 重新加载 systemd
    systemctl daemon-reload

    # 启动服务
    systemctl start gitee-webhook
    systemctl enable gitee-webhook

    # 检查服务状态
    if systemctl is-active --quiet gitee-webhook; then
        log_success "Webhook 服务启动成功"
    else
        log_error "Webhook 服务启动失败"
        journalctl -u gitee-webhook -n 20
        exit 1
    fi
}

# 配置防火墙
configure_firewall() {
    log_info "配置防火墙..."

    # 启用 UFW
    if ! ufw status | grep -q "Status: active"; then
        log_info "启用防火墙..."
        echo "y" | ufw enable
    fi

    # 允许 SSH
    ufw allow 22/tcp

    # 允许 Webhook
    ufw allow 5000/tcp

    # 允许 Web 服务
    ufw allow 80/tcp
    ufw allow 8080/tcp

    log_success "防火墙配置完成"
    ufw status
}

# 测试服务
test_services() {
    log_info "测试服务..."

    # 测试 Webhook 服务
    if curl -f http://localhost:5000/health &> /dev/null; then
        log_success "Webhook 服务正常"
    else
        log_error "Webhook 服务异常"
    fi

    # 测试后端服务
    if curl -f http://localhost:8080/api/health &> /dev/null; then
        log_success "后端服务正常"
    else
        log_warning "后端服务未就绪（可能正在启动）"
    fi

    # 测试前端服务
    if curl -f http://localhost/ &> /dev/null; then
        log_success "前端服务正常"
    else
        log_warning "前端服务未就绪（可能正在启动）"
    fi
}

# 显示配置摘要
show_summary() {
    clear
    echo -e "${GREEN}"
    echo "========================================="
    echo "  配置完成！"
    echo "========================================="
    echo -e "${NC}"
    echo ""

    # 获取服务器 IP
    SERVER_IP=$(curl -s ifconfig.me || hostname -I | awk '{print $1}')

    echo -e "${BLUE}【服务访问地址】${NC}"
    echo "  前端: http://$SERVER_IP/"
    echo "  后端: http://$SERVER_IP:8080/health"
    echo "  API 文档: http://$SERVER_IP:8080/swagger-ui.html"
    echo "  Webhook: http://$SERVER_IP:5000/webhook"
    echo ""

    echo -e "${BLUE}【Gitee Webhook 配置】${NC}"
    echo "  1. 访问: https://gitee.com/jinyue093220/cixiu/hooks"
    echo "  2. 点击 '添加 WebHook'"
    echo "  3. URL: http://$SERVER_IP:5000/webhook"
    echo "  4. 密码: $WEBHOOK_SECRET"
    echo "  5. 勾选: Push"
    echo ""

    echo -e "${BLUE}【常用命令】${NC}"
    echo "  查看 Webhook 日志: journalctl -u gitee-webhook -f"
    echo "  查看应用日志: cd $PROJECT_DIR && docker compose logs -f"
    echo "  重启 Webhook: systemctl restart gitee-webhook"
    echo "  手动部署: cd $PROJECT_DIR && bash deploy/auto-deploy.sh"
    echo ""

    echo -e "${BLUE}【重要提示】${NC}"
    echo "  1. 请在阿里云控制台的安全组中开放端口: 80, 5000, 8080"
    echo "  2. 请编辑 .env 文件，配置阿里云 OSS 信息"
    echo "  3. 请在 Gitee 中配置 Webhook"
    echo ""

    log_success "所有配置已完成！"
}

# 主流程
main() {
    show_welcome
    check_root
    detect_system
    update_system
    install_basic_tools
    install_docker
    install_docker_compose
    install_python
    clone_project
    configure_env

    echo ""
    read -p "是否执行首次部署？[y/n]: " do_deploy
    if [[ $do_deploy == "y" ]]; then
        initial_deploy
    fi

    configure_webhook
    configure_firewall
    test_services
    show_summary
}

# 执行
main "$@"
