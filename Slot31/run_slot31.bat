@echo off
echo ========================================================
echo   Starting SBA301 - Slot 31 (JavaFX + Hibernate CRUD UI)
echo   Architecture: MVC Layer Service + Repository Pattern
echo ========================================================
cd /d %~dp0
call mvnw.cmd compile exec:java
pause

