@echo off
echo ========================================================
echo   Starting SBA301 - Lab 07 (JavaFX + Hibernate ORM)
echo   Architecture: MVC Layer Service + Repository Pattern
echo ========================================================
cd /d %~dp0
call mvnw.cmd compile exec:java
pause
