# Elasticsearch 部署指南

## 概述

本指南介绍如何部署 Elasticsearch 用于知识库向量检索功能。

## 部署方式

### 方式 1: Docker 部署（推荐）

#### 1.1 单节点部署（开发环境）

**启动 Elasticsearch**:
```bash
docker run -d \
  --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
  docker.elastic.co/elasticsearch/elasticsearch:8.11.0
```

**参数说明**:
- `-p 9200:9200`: HTTP API 端口
- `-p 9300:9300`: 节点通信端口
- `discovery.type=single-node`: 单节点模式
- `xpack.security.enabled=false`: 禁用安全认证（开发环境）
- `ES_JAVA_OPTS`: JVM 内存设置（根据服务器配置调整）

**验证部署**:
```bash
curl http://localhost:9200
```

**预期输出**:
```json
{
  "name" : "...",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "...",
  "version" : {
    "number" : "8.11.0",
    ...
  },
  "tagline" : "You Know, for Search"
}
```

#### 1.2 启用安全认证（生产环境）

**启动 Elasticsearch**:
```bash
docker run -d \
  --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=true" \
  -e "ELASTIC_PASSWORD=your_password" \
  -e "ES_JAVA_OPTS=-Xms2g -Xmx2g" \
  docker.elastic.co/elasticsearch/elasticsearch:8.11.0
```

**配置应用**:
```yaml
elasticsearch:
  host: localhost
  port: 9200
  username: elastic
  password: your_password
  index-name: knowledge_embeddings
```

#### 1.3 持久化数据

**创建数据卷**:
```bash
docker volume create es-data
```

**启动 Elasticsearch**:
```bash
docker run -d \
  --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -v es-data:/usr/share/elasticsearch/data \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
  docker.elastic.co/elasticsearch/elasticsearch:8.11.0
```

### 方式 2: Docker Compose 部署

**创建 `docker-compose.yml`**:
```yaml
version: '3.8'

services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
      - "9300:9300"
    volumes:
      - es-data:/usr/share/elasticsearch/data
    networks:
      - elastic

volumes:
  es-data:
    driver: local

networks:
  elastic:
    driver: bridge
```

**启动**:
```bash
docker-compose up -d
```

**停止**:
```bash
docker-compose down
```

### 方式 3: 本地安装

#### 3.1 下载 Elasticsearch

访问 [Elasticsearch 官网](https://www.elastic.co/downloads/elasticsearch) 下载对应版本。

#### 3.2 解压并启动

**Linux/Mac**:
```bash
tar -xzf elasticsearch-8.11.0-linux-x86_64.tar.gz
cd elasticsearch-8.11.0
./bin/elasticsearch
```

**Windows**:
```powershell
Expand-Archive elasticsearch-8.11.0-windows-x86_64.zip
cd elasticsearch-8.11.0
.\bin\elasticsearch.bat
```

#### 3.3 配置

编辑 `config/elasticsearch.yml`:
```yaml
cluster.name: my-cluster
node.name: node-1
network.host: 0.0.0.0
http.port: 9200
discovery.type: single-node
xpack.security.enabled: false
```

## 配置优化

### 1. 内存配置

**开发环境**:
```bash
-e "ES_JAVA_OPTS=-Xms512m -Xmx512m"
```

**生产环境**:
```bash
-e "ES_JAVA_OPTS=-Xms4g -Xmx4g"
```

**建议**:
- 最小 512MB（开发）
- 推荐 2-4GB（生产）
- 不超过物理内存的 50%

### 2. 磁盘配置

**使用 SSD**:
- 向量检索对 I/O 性能要求高
- SSD 可以显著提升检索速度

**预留空间**:
- 每 1000 个文档分段约需 10-20MB
- 建议预留 10GB+ 空间

### 3. 网络配置

**防火墙规则**:
```bash
# 允许 9200 端口（HTTP API）
sudo ufw allow 9200/tcp

# 允许 9300 端口（节点通信）
sudo ufw allow 9300/tcp
```

## 索引管理

### 1. 查看索引

```bash
curl http://localhost:9200/_cat/indices?v
```

### 2. 查看索引详情

```bash
curl http://localhost:9200/knowledge_embeddings
```

### 3. 删除索引

```bash
curl -X DELETE http://localhost:9200/knowledge_embeddings
```

### 4. 查看索引统计

```bash
curl http://localhost:9200/knowledge_embeddings/_stats
```

## 监控与维护

### 1. 查看集群健康

```bash
curl http://localhost:9200/_cluster/health?pretty
```

**健康状态**:
- `green`: 所有分片正常
- `yellow`: 主分片正常，副本分片异常
- `red`: 部分主分片异常

### 2. 查看节点信息

```bash
curl http://localhost:9200/_nodes/stats?pretty
```

### 3. 查看日志

**Docker**:
```bash
docker logs elasticsearch
```

**本地安装**:
```bash
tail -f logs/my-cluster.log
```

### 4. 备份数据

**创建快照仓库**:
```bash
curl -X PUT "http://localhost:9200/_snapshot/my_backup" -H 'Content-Type: application/json' -d'
{
  "type": "fs",
  "settings": {
    "location": "/usr/share/elasticsearch/backup"
  }
}
'
```

**创建快照**:
```bash
curl -X PUT "http://localhost:9200/_snapshot/my_backup/snapshot_1?wait_for_completion=true"
```

## 故障排查

### 问题 1: 无法启动

**检查端口占用**:
```bash
netstat -an | grep 9200
```

**检查日志**:
```bash
docker logs elasticsearch
```

**常见原因**:
- 端口被占用
- 内存不足
- 磁盘空间不足

### 问题 2: 连接超时

**检查防火墙**:
```bash
sudo ufw status
```

**检查网络**:
```bash
curl -v http://localhost:9200
```

**检查配置**:
- 确认 `network.host` 设置
- 确认端口映射正确

### 问题 3: 性能问题

**检查内存使用**:
```bash
curl http://localhost:9200/_nodes/stats/jvm?pretty
```

**检查磁盘 I/O**:
```bash
iostat -x 1
```

**优化建议**:
- 增加 JVM 内存
- 使用 SSD
- 优化查询参数

### 问题 4: 索引损坏

**检查索引健康**:
```bash
curl http://localhost:9200/_cluster/health?pretty
```

**修复索引**:
```bash
curl -X POST "http://localhost:9200/knowledge_embeddings/_forcemerge?max_num_segments=1"
```

## 生产环境部署

### 1. 集群部署

**docker-compose.yml**:
```yaml
version: '3.8'

services:
  es01:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
    container_name: es01
    environment:
      - node.name=es01
      - cluster.name=es-cluster
      - discovery.seed_hosts=es02,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms2g -Xmx2g"
    ports:
      - "9200:9200"
    volumes:
      - es01-data:/usr/share/elasticsearch/data
    networks:
      - elastic

  es02:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
    container_name: es02
    environment:
      - node.name=es02
      - cluster.name=es-cluster
      - discovery.seed_hosts=es01,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms2g -Xmx2g"
    volumes:
      - es02-data:/usr/share/elasticsearch/data
    networks:
      - elastic

  es03:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
    container_name: es03
    environment:
      - node.name=es03
      - cluster.name=es-cluster
      - discovery.seed_hosts=es01,es02
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms2g -Xmx2g"
    volumes:
      - es03-data:/usr/share/elasticsearch/data
    networks:
      - elastic

volumes:
  es01-data:
  es02-data:
  es03-data:

networks:
  elastic:
    driver: bridge
```

### 2. 安全配置

**启用 HTTPS**:
```yaml
environment:
  - xpack.security.enabled=true
  - xpack.security.http.ssl.enabled=true
  - xpack.security.transport.ssl.enabled=true
```

**设置密码**:
```bash
docker exec -it es01 bin/elasticsearch-setup-passwords auto
```

### 3. 监控配置

**使用 Kibana**:
```yaml
kibana:
  image: docker.elastic.co/kibana/kibana:8.11.0
  container_name: kibana
  ports:
    - "5601:5601"
  environment:
    - ELASTICSEARCH_HOSTS=http://es01:9200
  networks:
    - elastic
```

访问: http://localhost:5601

## 总结

**开发环境推荐**:
- Docker 单节点部署
- 禁用安全认证
- 512MB-1GB 内存

**生产环境推荐**:
- Docker Compose 集群部署
- 启用安全认证
- 4GB+ 内存
- SSD 存储
- 定期备份

**快速开始**:
```bash
# 1. 启动 Elasticsearch
docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" -e "xpack.security.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.11.0

# 2. 验证
curl http://localhost:9200

# 3. 配置应用并启动
```

祝部署顺利！🚀
