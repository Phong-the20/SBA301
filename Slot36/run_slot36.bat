@echo off
echo ==================================================================
echo   Starting SBA301 - Slot 36: Spring MVC Thymeleaf ^& MongoDB
echo   Layered 3-Tier Architecture with Service Layer Validation
echo ==================================================================
cd /d %~dp0
call mvnw.cmd spring-boot:run
pause
