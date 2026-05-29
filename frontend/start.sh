#!/bin/bash

echo "========================================"
echo "云绣定制平台 - 前端开发服务器启动"
echo "========================================"
echo ""

cd "$(dirname "$0")"

echo "当前目录: $(pwd)"
echo ""

if [ ! -f "package.json" ]; then
    echo "[错误] 找不到 package.json 文件！"
    echo "请确保您在 frontend 目录下运行此脚本。"
    exit 1
fi

if [ ! -d "node_modules" ]; then
    echo "[信息] 检测到 node_modules 不存在，正在安装依赖..."
    echo ""
    npm install
    if [ $? -ne 0 ]; then
        echo "[错误] 依赖安装失败！"
        exit 1
    fi
    echo ""
fi

echo "[信息] 启动开发服务器..."
echo "访问地址: http://localhost:3000"
echo "按 Ctrl+C 停止服务器"
echo ""

npm run dev
