@echo off
echo [INFO] Install the jar to local

cd %~dp0
cd ..
call mvn clean install -Dmaven.test.skip=true
pause