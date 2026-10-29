@echo off
echo ==================================================================
echo   Starting SBA301 - Slot 33: Spring Data MongoDB CRUD ^& Export
echo   Layered 3-Tier Architecture with Service Layer Validation
echo ==================================================================
cd /d %~dp0
call mvnw.cmd spring-boot:run
pause
