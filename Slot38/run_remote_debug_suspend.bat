@echo off
echo ==================================================================
echo   Starting SBA301 - Slot 38 in JDWP Startup Debug Mode
echo   JDWP Port: 5005 (suspend=y) - JVM will WAIT until IDE attaches!
echo ==================================================================
cd /d %~dp0
call mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"
pause
