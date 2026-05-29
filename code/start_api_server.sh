#!/bin/bash

echo "========================================"
echo "启动刺绣AI服务"
echo "========================================"

# 检查Python环境
if ! command -v python3 &> /dev/null
then
    echo "错误: 未找到Python3，请先安装Python"
    exit 1
fi

python3 --version

echo ""
echo "正在启动API服务器（端口8001）..."
echo ""

# 启动Flask服务
python3 api_server.py
