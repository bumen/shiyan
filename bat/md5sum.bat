@echo off

set var=%~dp0
cd /d %var%

#计算file目录下文件md5值
md5sums .\file

echo.
echo.
pause


