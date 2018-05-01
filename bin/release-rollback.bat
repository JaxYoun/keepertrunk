@echo off
echo [INFO] Rollback the pom to before

cd %~dp0
cd ..
call mvn release:rollback
pause