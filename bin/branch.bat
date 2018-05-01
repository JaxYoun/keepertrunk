@echo off
echo [INFO] Branch ynedut-edut.

cd %~dp0
cd ..
call mvn release:branch -DautoVersionSubmodules=true -DupdateBranchVersions=true -DupdateWorkingCopyVersions=false -DbranchName=0.0.4-SNAPSHOT
pause