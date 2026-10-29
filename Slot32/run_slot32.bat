@echo off
echo ==================================================================
echo   Starting SBA301 - Slot 32 (MongoDB NoSQL Model ^& Architecture)
echo   3-Tier Architecture with Service Layer Validation
echo ==================================================================
cd /d %~dp0
call mvnw.cmd compile exec:java
pause
