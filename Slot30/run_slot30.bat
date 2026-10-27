@echo off
echo ========================================================
echo   Starting SBA301 - Slot 30: Hibernate CRUD Application
echo   Presentation -> Service -> Repository -> DAO -> Hibernate
echo ========================================================
cd /d %~dp0
call mvnw.cmd compile exec:java
pause
