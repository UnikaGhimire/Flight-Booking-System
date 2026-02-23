$ErrorActionPreference = "Stop"

Write-Host "========================================"
Write-Host "Advanced Flight Booking System - JAVADOC"
Write-Host "========================================"
Write-Host ""

New-Item -ItemType Directory -Force -Path docs | Out-Null

$javadoc = $null
if ($env:JAVA_HOME) {
    $candidate = Join-Path $env:JAVA_HOME "bin\javadoc.exe"
    if (Test-Path $candidate) {
        $javadoc = $candidate
    }
}

if (-not $javadoc) {
    $candidate = "C:\Program Files\Java\jdk-25\bin\javadoc.exe"
    if (Test-Path $candidate) {
        $javadoc = $candidate
    }
}

if (-not $javadoc) {
    try {
        $prev = $ErrorActionPreference
        $ErrorActionPreference = "Continue"
        $where = & where.exe javadoc 2>$null
        if ($LASTEXITCODE -eq 0 -and $where) {
        $javadoc = $where[0]
    }
} finally {
    $ErrorActionPreference = $prev
}
}

if (-not $javadoc) {
    Write-Host "javadoc not found. Set JAVA_HOME or add JDK bin to PATH."
    exit 1
}

& $javadoc -d docs -sourcepath src -subpackages bcu.cmp5332.bookingsystem

Write-Host ""
Write-Host "Javadoc generated in: docs\index.html"
