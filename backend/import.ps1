$ErrorActionPreference = 'Stop'
$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$jarPath = Join-Path $PSScriptRoot 'target/backend-0.0.1-SNAPSHOT.jar'
$csvPath = Join-Path $projectRoot 'dataset/diem_thi_thpt_2024.csv'
$logPath = Join-Path $projectRoot 'import-run.log'

if (-not (Test-Path -LiteralPath $jarPath)) {
    throw "Backend JAR not found: $jarPath. Run 'mvn clean package' in backend first."
}
if (-not (Test-Path -LiteralPath $csvPath)) {
    throw "CSV not found: $csvPath"
}

$env:IMPORT_CSV_PATH = $csvPath
Remove-Item Env:DEBUG -ErrorAction SilentlyContinue

$runtimeDirectory = Join-Path ([System.IO.Path]::GetTempPath()) ('gscores-import-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $runtimeDirectory | Out-Null
$runtimeJarPath = Join-Path $runtimeDirectory (Split-Path $jarPath -Leaf)

Push-Location $projectRoot
try {
    Copy-Item -LiteralPath $jarPath -Destination $runtimeJarPath
    & java -Xmx1g -jar $runtimeJarPath --spring.profiles.active=import --logging.level.org.hibernate.SQL=OFF --logging.level.com.gscores.backend.importer=INFO --logging.level.com.gscores.backend.service=INFO *>&1 | Tee-Object -FilePath $logPath
    $importExitCode = $LASTEXITCODE
} finally {
    Pop-Location
    Remove-Item -LiteralPath $runtimeDirectory -Recurse -Force
}
exit $importExitCode
