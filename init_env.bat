@echo off
setlocal

rem py-simple-chat 初始化脚本

echo [py-simple-chat] 初始化 Python 环境...

rem 确保在脚本所在目录执行
cd /d %~dp0

rem 检查 uv 是否已安装
where uv >nul 2>nul
if errorlevel 1 (
    echo 未检测到 uv，請先安装：
    echo   pip install uv
    goto :end
)

echo.
echo 正在创建虚拟环境 (.venv)...
uv venv

if errorlevel 1 (
    echo 创建虚拟环境失败，请检查错误信息。
    goto :end
)

echo.
echo 正在安装依赖（使用清华 PyPI 镜像）...
uv pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple

if errorlevel 1 (
    echo 依赖安装失败，请检查网络或镜像源设置。
    goto :end
)

echo.
echo 初始化完成！
echo 如需启动服务，可执行：
echo   uv run python app.py

set /p START_SERVER=是否现在启动服务? (y/N): 
if /I "%START_SERVER%"=="y" (
    echo 正在启动服务...
    uv run python app.py
)

:end
echo.
pause
endlocal
