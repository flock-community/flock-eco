# This file reads the Makefile and tries to convert it to sensible Windows commands

param (
    [parameter(Position=0)]
    [ValidateSet('build', 'dev', 'help')]
    [String]$argument=$(throw "Build or dev? Example: .\make.ps1 build")
)

$unixFeDir = '/'
$unixMavenDir = '~/.m2'
$unixCurrentDir = '$(shell pwd)'

function slash([string]$path) {
    return $path.replace('/', '\')
}

$winFeDir = slash($unixFeDir)
$winMavenDir = slash($unixMavenDir)
$winCurrentDir = Split-Path $MyInvocation.MyCommand.Path

$makeFile = Get-Content -Path ./Makefile | Where-Object { $_ -match 'docker' }
function transformUnixToWin ([string]$command) {
    return $command.replace($unixCurrentDir, '' + $winCurrentDir + '').replace($unixMavenDir, $winMavenDir).Trim()
}

$buildBackend  = transformUnixToWin($makeFile | Where-Object { $_ -match 'build' })
$runBackend    = transformUnixToWin($makeFile | Where-Object { $_ -match 'run' })

if ($argument -eq 'build') {
    Write-Output $buildBackend
    Invoke-Expression $buildBackend
} elseif ($argument -eq 'dev') {
    Write-Output $runBackend
    Invoke-Expression $runBackend
} else {
    Write-Output $buildBackend
    Write-Output $runBackend
}
