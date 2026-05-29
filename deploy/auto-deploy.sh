#!/bin/bash

###############################################################################
# CI/CD 自动化部署脚本
# 由 Gitee Webhook 调用，执行服务器端的部署操作
###############################################################################

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log_info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} ✅ $1"
}

log_warning() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} ⚠️  $1"
}

log_error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} ❌ $1"
}

# 配置
PROJECT_DIR=$(pwd)
BACKUP_DIR="${PROJECT_DIR}/backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
MAX_BACKUPS=5  # 保留最近5个备份

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 1. 检查环境
check_environment() {
    log_info "检查运行环境..."

    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装"
        exit 1
    fi

    if ! docker compose version &> /dev/null; then
        log_error "Docker Compose 未安装"
        exit 1
    fi

    if [ ! -f ".env" ]; then
        log_error ".env 文件不存在，请先配置环境变量"
        exit 1
    fi

    log_success "环境检查通过"
}

# 2. 拉取最新代码
pull_code() {
    log_info "拉取最新代码..."

    cd "$PROJECT_DIR"
    git pull origin master

    log_success "代码更新完成"
}

# 3. 备份数据库
backup_database() {
    log_info "备份数据库..."

    # 检查MySQL容器是否运行
    if docker ps | grep -q cixiu-mysql; then
        # 从.env文件读取密码
        source .env

        BACKUP_FILE="${BACKUP_DIR}/db_backup_${TIMESTAMP}.sql"

        docker exec cixiu-mysql mysqldump \
            -u root \
            -p"${MYSQL_ROOT_PASSWORD:-123456tpf}" \
            --single-transaction \
            --quick \
            --lock-tables=false \
            cixiu > "$BACKUP_FILE" 2>/dev/null

        if [ -f "$BACKUP_FILE" ]; then
            # 压缩备份文件
            gzip "$BACKUP_FILE"
            log_success "数据库备份完成: ${BACKUP_FILE}.gz"
        else
            log_warning "数据库备份失败，继续部署"
        fi
    else
        log_warning "MySQL容器未运行，跳过备份"
    fi
}

# 4. 清理旧备份
cleanup_old_backups() {
    log_info "清理旧备份（保留最近${MAX_BACKUPS}个）..."

    cd "$BACKUP_DIR"
    ls -t db_backup_*.sql.gz 2>/dev/null | tail -n +$((MAX_BACKUPS + 1)) | xargs -r rm -f

    log_success "旧备份清理完成"
}

# 5. 停止旧容器
stop_containers() {
    log_info "停止旧容器..."

    cd "$PROJECT_DIR"

    if docker compose ps | grep -q "Up"; then
        docker compose down
        log_success "旧容器已停止"
    else
        log_info "没有运行中的容器"
    fi
}

# 6. 构建新镜像
build_images() {
    log_info "构建Docker镜像..."

    cd "$PROJECT_DIR"

    # 使用 --no-cache 确保构建最新版本
    docker compose build --no-cache

    log_success "镜像构建完成"
}

# 7. 启动新容器
start_containers() {
    log_info "启动新容器..."

    cd "$PROJECT_DIR"

    docker compose up -d

    log_success "容器已启动"
}

# 8. 等待服务就绪
wait_for_services() {
    log_info "等待服务启动..."

    # 等待MySQL就绪
    local max_attempts=60
    local attempt=0

    source .env

    while [ $attempt -lt $max_attempts ]; do
        if docker exec cixiu-mysql mysqladmin ping -h localhost -u root -p"${MYSQL_ROOT_PASSWORD:-123456tpf}" &> /dev/null; then
            log_success "MySQL已就绪"
            break
        fi
        attempt=$((attempt + 1))
        sleep 2
    done

    # 等待后端服务
    attempt=0
    while [ $attempt -lt $max_attempts ]; do
        if curl -f http://localhost:8080/api/health &> /dev/null; then
            log_success "后端服务已就绪"
            break
        fi
        attempt=$((attempt + 1))
        sleep 2
    done

    if [ $attempt -eq $max_attempts ]; then
        log_error "服务启动超时"
        docker compose logs backend
        exit 1
    fi
}

# 9. 健康检查
health_check() {
    log_info "执行健康检查..."

    source .env
    local failed=0

    # 检查后端
    if curl -f http://localhost:8080/api/health &> /dev/null; then
        log_success "后端服务健康"
    else
        log_error "后端服务异常"
        failed=1
    fi

    # 检查前端
    if curl -f http://localhost/ &> /dev/null; then
        log_success "前端服务健康"
    else
        log_error "前端服务异常"
        failed=1
    fi

    # 检查MySQL
    if docker exec cixiu-mysql mysqladmin ping -h localhost -u root -p"${MYSQL_ROOT_PASSWORD:-123456tpf}" &> /dev/null; then
        log_success "MySQL服务健康"
    else
        log_error "MySQL服务异常"
        failed=1
    fi

    # 检查Redis
    if docker exec cixiu-redis redis-cli -a "${REDIS_PASSWORD:-123456tpf}" ping &> /dev/null; then
        log_success "Redis服务健康"
    else
        log_error "Redis服务异常"
        failed=1
    fi

    if [ $failed -eq 1 ]; then
        log_error "健康检查失败"
        exit 1
    fi

    log_success "所有服务健康检查通过"
}

# 10. 清理旧镜像和容器
cleanup_docker() {
    log_info "清理Docker资源..."

    # 删除悬空镜像
    docker image prune -f &> /dev/null || true

    # 删除停止的容器
    docker container prune -f &> /dev/null || true

    log_success "Docker资源清理完成"
}

# 11. 显示部署信息
show_deployment_info() {
    log_success "========================================="
    log_success "部署成功！"
    log_success "========================================="
    echo ""
    log_info "部署时间: $(date +'%Y-%m-%d %H:%M:%S')"
    log_info "Git提交: $(git rev-parse --short HEAD)"
    log_info "Git分支: $(git branch --show-current)"
    echo ""
    log_info "服务状态："
    docker compose ps
    echo ""
    log_info "备份位置: $BACKUP_DIR"
    log_info "最新备份: $(ls -t ${BACKUP_DIR}/db_backup_*.sql.gz 2>/dev/null | head -n 1 || echo '无')"
}

# 主流程
main() {
    log_info "========================================="
    log_info "开始自动化部署"
    log_info "========================================="
    echo ""

    check_environment
    pull_code
    backup_database
    cleanup_old_backups
    stop_containers
    build_images
    start_containers
    wait_for_services
    health_check
    cleanup_docker
    show_deployment_info

    echo ""
    log_success "========================================="
    log_success "🎉 部署流程全部完成！"
    log_success "========================================="
}

# 执行部署
main "$@"
