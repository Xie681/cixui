#!/usr/bin/env python3
"""
Gitee Webhook 接收服务
监听 Gitee 的 Push 事件，自动执行部署脚本

使用方法：
1. 安装依赖：pip3 install flask
2. 运行服务：python3 webhook_server.py
3. 配置 Gitee Webhook：http://your-server-ip:5000/webhook
"""

import os
import hmac
import hashlib
import base64
import subprocess
import logging
from datetime import datetime
from flask import Flask, request, jsonify

app = Flask(__name__)

# ==================== 配置区域 ====================
# Webhook 密钥（与 Gitee 配置保持一致，留空表示不验证）
WEBHOOK_SECRET = os.environ.get('WEBHOOK_SECRET', '')

# 部署脚本路径
DEPLOY_SCRIPT = os.environ.get('DEPLOY_SCRIPT', '/opt/cixiu/deploy/auto-deploy.sh')

# 项目目录
PROJECT_DIR = os.environ.get('PROJECT_DIR', '/opt/cixiu')

# 允许触发部署的分支（只有这些分支的推送才会触发部署）
ALLOWED_BRANCHES = os.environ.get('ALLOWED_BRANCHES', 'master').split(',')

# 部署超时时间（秒）
DEPLOY_TIMEOUT = int(os.environ.get('DEPLOY_TIMEOUT', '600'))

# 日志级别
LOG_LEVEL = os.environ.get('LOG_LEVEL', 'INFO')
# ================================================

# 日志配置
logging.basicConfig(
    level=getattr(logging, LOG_LEVEL),
    format='%(asctime)s - %(levelname)s - %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)


def verify_signature(timestamp, signature):
    """
    验证 Gitee Webhook 签名

    Gitee 签名算法：
    1. 签名字符串 = timestamp + "\n" + secret
    2. HMAC-SHA256 加密
    3. Base64 编码

    Args:
        timestamp: Gitee 发送的时间戳
        signature: Gitee 发送的签名（Base64编码）

    Returns:
        bool: 签名是否有效
    """
    if not signature or not WEBHOOK_SECRET or not timestamp:
        return False

    # Gitee 签名算法：timestamp + "\n" + secret
    sign_string = f"{timestamp}\n{WEBHOOK_SECRET}"

    # 使用 HMAC-SHA256 加密
    mac = hmac.new(
        WEBHOOK_SECRET.encode('utf-8'),
        msg=sign_string.encode('utf-8'),
        digestmod=hashlib.sha256
    )

    # Base64 编码
    expected_signature = base64.b64encode(mac.digest()).decode('utf-8')

    return hmac.compare_digest(expected_signature, signature)


def execute_deploy():
    """
    执行部署脚本

    Returns:
        tuple: (success: bool, output: str, error: str)
    """
    try:
        logger.info(f'开始执行部署脚本: {DEPLOY_SCRIPT}')

        result = subprocess.run(
            ['bash', DEPLOY_SCRIPT],
            cwd=PROJECT_DIR,
            capture_output=True,
            text=True,
            timeout=DEPLOY_TIMEOUT
        )

        success = result.returncode == 0

        if success:
            logger.info('部署脚本执行成功')
        else:
            logger.error(f'部署脚本执行失败，返回码: {result.returncode}')

        return success, result.stdout, result.stderr

    except subprocess.TimeoutExpired:
        error_msg = f'部署超时（超过 {DEPLOY_TIMEOUT} 秒）'
        logger.error(error_msg)
        return False, '', error_msg

    except Exception as e:
        error_msg = f'部署异常: {str(e)}'
        logger.error(error_msg)
        return False, '', error_msg


@app.route('/webhook', methods=['POST'])
def webhook():
    """
    处理 Gitee Webhook 请求
    """
    # 获取请求头
    event = request.headers.get('X-Gitee-Event', '')
    signature = request.headers.get('X-Gitee-Token', '')
    timestamp = request.headers.get('X-Gitee-Timestamp', '')

    logger.info(f'收到 Webhook 请求，事件类型: {event}')

    # 验证签名（如果配置了密钥）
    if WEBHOOK_SECRET:
        if not verify_signature(timestamp, signature):
            logger.warning('❌ 签名验证失败')
            return jsonify({
                'success': False,
                'error': '签名验证失败'
            }), 403
        logger.info('✅ 签名验证通过')
    else:
        logger.warning('⚠️  未配置 WEBHOOK_SECRET，跳过签名验证')

    # 检查是否是 Push 事件（Gitee 发送的事件类型为 push_hooks 或 Push Hook）
    if event not in ['Push Hook', 'push_hooks']:
        logger.info(f'忽略非 Push 事件: {event}')
        return jsonify({
            'success': True,
            'message': f'忽略事件类型: {event}'
        }), 200

    # 解析 JSON 数据
    try:
        data = request.json
    except Exception as e:
        logger.error(f'JSON 解析失败: {e}')
        return jsonify({
            'success': False,
            'error': 'Invalid JSON'
        }), 400

    # 提取分支信息
    ref = data.get('ref', '')
    branch = ref.replace('refs/heads/', '')

    logger.info(f'推送分支: {branch}')

    # 检查是否是允许的分支
    if branch not in ALLOWED_BRANCHES:
        logger.info(f'忽略非部署分支: {branch}（允许的分支: {ALLOWED_BRANCHES}）')
        return jsonify({
            'success': True,
            'message': f'仅 {ALLOWED_BRANCHES} 分支触发部署'
        }), 200

    # 提取提交信息
    commits = data.get('commits', [])
    commit_count = len(commits)
    pusher_name = data.get('pusher', {}).get('name', 'unknown')
    pusher_email = data.get('pusher', {}).get('email', '')
    repository = data.get('repository', {}).get('name', 'unknown')

    logger.info('='*60)
    logger.info('📦 部署信息')
    logger.info(f'  仓库: {repository}')
    logger.info(f'  分支: {branch}')
    logger.info(f'  推送者: {pusher_name} <{pusher_email}>')
    logger.info(f'  提交数: {commit_count}')

    if commits:
        latest_commit = commits[-1]
        commit_message = latest_commit.get('message', '').split('\n')[0]
        commit_id = latest_commit.get('id', '')[:8]
        logger.info(f'  最新提交: [{commit_id}] {commit_message}')

    logger.info('='*60)

    # 执行部署
    success, output, error = execute_deploy()

    # 构造响应
    response_data = {
        'success': success,
        'timestamp': datetime.now().isoformat(),
        'repository': repository,
        'branch': branch,
        'commits': commit_count,
        'pusher': pusher_name,
    }

    if success:
        logger.info('='*60)
        logger.info('🎉 部署成功！')
        logger.info('='*60)
        response_data['message'] = '部署成功'
        if output:
            response_data['output'] = output[-500:]  # 只返回最后500字符
        return jsonify(response_data), 200
    else:
        logger.error('='*60)
        logger.error('❌ 部署失败！')
        logger.error('='*60)
        response_data['error'] = '部署失败'
        if error:
            response_data['details'] = error[-500:]
        return jsonify(response_data), 500


@app.route('/health', methods=['GET'])
def health():
    """健康检查接口"""
    return jsonify({
        'status': 'ok',
        'service': 'gitee-webhook',
        'timestamp': datetime.now().isoformat(),
        'config': {
            'project_dir': PROJECT_DIR,
            'deploy_script': DEPLOY_SCRIPT,
            'allowed_branches': ALLOWED_BRANCHES,
            'webhook_secret_configured': bool(WEBHOOK_SECRET)
        }
    }), 200


@app.route('/', methods=['GET'])
def index():
    """首页"""
    return jsonify({
        'service': 'Gitee Webhook Server',
        'version': '1.0',
        'endpoints': {
            '/webhook': 'POST - 接收 Gitee Webhook',
            '/health': 'GET - 健康检查',
            '/': 'GET - 服务信息'
        }
    }), 200


if __name__ == '__main__':
    logger.info('='*60)
    logger.info('🚀 Gitee Webhook 服务启动')
    logger.info('='*60)
    logger.info(f'项目目录: {PROJECT_DIR}')
    logger.info(f'部署脚本: {DEPLOY_SCRIPT}')
    logger.info(f'允许分支: {ALLOWED_BRANCHES}')
    logger.info(f'签名验证: {"启用" if WEBHOOK_SECRET else "禁用"}')
    logger.info(f'监听地址: 0.0.0.0:5000')
    logger.info('='*60)

    # 启动 Flask 服务
    app.run(
        host='0.0.0.0',
        port=5000,
        debug=False
    )
