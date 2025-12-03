@echo off
echo Starting Library Management System...
cd /d "c:/Users/lianxiang/Desktop/LibarayManagementSystem/2 - 副本/NUIST-SETU-HW.3/NUIST-SETU-HW"
javac -encoding UTF-8 -d out/production/NUIST-SETU-HW -sourcepath src/main/java src/main/java/com/library/Main.java src/main/java/com/library/controller/MenuController.java src/main/java/com/library/service/*.java src/main/java/com/library/model/*.java
if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    java -cp "out/production/NUIST-SETU-HW;src/out/production/NUIST-SETU-HW" com.library.Main
) else (
    echo Compilation failed!
)
pause