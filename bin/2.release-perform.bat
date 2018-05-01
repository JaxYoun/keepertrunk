@echo off
echo [INFO] Perform release the jar to Central Repository

cd %~dp0
cd ..
call mvn release:perform -DskipDocs=true
pause