@echo off
chcp 65001 >nul
echo 正在运行图书管理系统...
echo.
cd /d "%~dp0"
java -cp "out/production/NUIST-SETU-HW;src/main/resources" com.library.Main
pause
