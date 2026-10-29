@echo off
echo ==================================================================
echo   Starting SBA301 - Slot 38: DevOps, CI/CD, DevTools ^& Actuator
echo ==================================================================
cd /d %~dp0
call mvnw.cmd spring-boot:run
pause
