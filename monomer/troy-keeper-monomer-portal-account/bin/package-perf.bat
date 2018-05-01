@echo off
echo [INFO] Package the jar in target dir.

cd %~dp0
cd ..
call mvn clean package -DskipTests=true -Pperf -Denv=package
cd bin
pause