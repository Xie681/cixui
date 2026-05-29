@echo off
echo ========================================
echo 云绣定制平台 - 前端开发服务器启动
echo ========================================
echo.

cd /d %~dp0

echo 当前目录: %CD%
echo.

if not exist "package.json" (
    echo [错误] 找不到 package.json 文件！
    echo 请确保您在 frontend 目录下运行此脚本。
    pause
    exit /b 1
)

if not exist "node_modules" (
    echo [信息] 检测到 node_modules 不存在，正在安装依赖...
    echo.
    call npm install
    if errorlevel 1 (
        echo [错误] 依赖安装失败！
        pause
        exit /b 1
    )
    echo.
)

echo [信息] 启动开发服务器...
echo 访问地址: http://localhost:3000
echo 按 Ctrl+C 停止服务器
echo.

call npm run dev

pause
