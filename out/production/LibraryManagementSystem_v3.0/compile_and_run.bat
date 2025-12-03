@echo off
chcp 65001 >nul
echo 正在编译项目...
echo.
cd /d "%~dp0"

echo 编译 Java 文件...
javac -encoding UTF-8 -d out/production/NUIST-SETU-HW -sourcepath src/main/java src/main/java/com/library/Main.java src/main/java/com/library/controller/MenuController.java src/main/java/com/library/service/*.java src/main/java/com/library/model/*.java

if %errorlevel% neq 0 (
    echo 编译失败！
    pause
    exit /b 1
)

echo 编译成功！
echo.
echo 正在运行图书管理系统...
echo.
java -cp "out/production/NUIST-SETU-HW;src/main/resources" com.library.Main
pause
