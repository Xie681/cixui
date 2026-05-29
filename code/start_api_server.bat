@echo off
echo ========================================
echo 启动刺绣AI服务
echo ========================================

REM 检查Python环境
python --version
if errorlevel 1 (
    echo 错误: 未找到Python，请先安装Python
    pause
    exit /b 1
)

echo.
echo 正在启动API服务器（端口8001）...
echo.

REM 启动Flask服务
python api_server.py

pause
