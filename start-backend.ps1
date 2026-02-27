# Script pour lancer le backend Spring Boot
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Démarrage du Backend Spring Boot" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Vérifier MySQL
Write-Host "Vérification de MySQL sur le port 3307..." -ForegroundColor Yellow
$mysqlCheck = Test-NetConnection -ComputerName localhost -Port 3307 -InformationLevel Quiet -WarningAction SilentlyContinue
if ($mysqlCheck) {
    Write-Host "✓ MySQL est accessible sur le port 3307" -ForegroundColor Green
} else {
    Write-Host "✗ MySQL n'est PAS accessible sur le port 3307" -ForegroundColor Red
    Write-Host "Veuillez démarrer MySQL dans XAMPP" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "Lancement du backend..." -ForegroundColor Yellow
Write-Host ""

# Lancer le backend
$scriptPath = Join-Path $PSScriptRoot "mvnw.cmd"
if (Test-Path $scriptPath) {
    & cmd.exe /c "cd /d `"$PSScriptRoot`" && `"$scriptPath`" spring-boot:run"
} else {
    Write-Host "Erreur: mvnw.cmd introuvable dans $PSScriptRoot" -ForegroundColor Red
    exit 1
}
