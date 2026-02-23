$ErrorActionPreference = "Stop"

Write-Host "========================================"
Write-Host "Advanced Flight Booking System - TEST"
Write-Host "========================================"
Write-Host ""

if (-not (Test-Path "lib/junit-platform-console-standalone.jar")) {
    Write-Host "JUnit 5 jar not found. Place it at lib/junit-platform-console-standalone.jar"
    exit 1
}

New-Item -ItemType Directory -Force -Path bin | Out-Null
New-Item -ItemType Directory -Force -Path bin_test | Out-Null

# Compile main sources
Get-ChildItem -Path src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName | Out-File -Encoding ascii sources.txt
& javac -d bin "@sources.txt"

# Compile tests
Get-ChildItem -Path test -Recurse -Filter *.java | Select-Object -ExpandProperty FullName | Out-File -Encoding ascii test_sources.txt
& javac -d bin_test -cp "bin;lib/junit-platform-console-standalone.jar" "@test_sources.txt"

Write-Host ""
Write-Host "Running tests..."
& java -jar lib/junit-platform-console-standalone.jar --class-path "bin;bin_test" --scan-classpath
