@echo off
echo [INFO] Install the jar to nexus.

cd %~dp0
cd ..
call mvn clean install -DskipTests=true
cd bin
pause