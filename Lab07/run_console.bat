@echo off
echo ========================================================
echo   Starting SBA301 - Lab 07 (Console Smoke Test)
echo ========================================================
cd /d %~dp0
call mvnw.cmd compile exec:java -Dexec.mainClass="fu.se.lab07.app.ConsoleSmokeTest"
pause
