@echo off
echo ==================================================================
echo   Starting SBA301 - Slot 38 in JDWP Remote Debug Mode
echo   JDWP Port: 5005 (suspend=n) - IDE can attach at any time!
echo ==================================================================
cd /d %~dp0
call mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
pause
