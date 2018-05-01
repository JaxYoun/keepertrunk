@echo off
echo [INFO] Deploy the jar to nexus.

cd %~dp0
cd ..
call mvn clean deploy -DskipTests=true
cd bin
pause