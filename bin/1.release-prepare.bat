@echo off
echo [INFO] Prepare release the jar to Central Repository

cd %~dp0
cd ..
call mvn clean release:prepare -DignoreSnapshots=true -DautoVersionSubmodules=true -DskipTests=true -Dmaven.test.skip=true
pause