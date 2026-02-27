@echo off
echo ========================================
echo Demarrage du Backend Spring Boot
echo ========================================
echo.

cd /d "%~dp0"

echo Verification de MySQL sur le port 3307...
echo.

echo Lancement du backend...
echo.

call mvnw.cmd spring-boot:run

pause
