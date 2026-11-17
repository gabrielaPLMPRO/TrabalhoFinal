$ErrorActionPreference = 'Stop'

Push-Location $PSScriptRoot
try {
    if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME/bin/java.exe")) {
        $java = Get-Command java.exe -ErrorAction SilentlyContinue
        if ($java) {
            $env:JAVA_HOME = Split-Path (Split-Path $java.Source -Parent) -Parent
        } else {
            $jdk = Get-ChildItem "$env:LOCALAPPDATA/Programs/Eclipse Adoptium/jdk-*" -Directory -ErrorAction SilentlyContinue |
                Sort-Object LastWriteTime -Descending | Select-Object -First 1
            if (-not $jdk) { throw 'Instale um JDK 21 ou superior e configure JAVA_HOME.' }
            $env:JAVA_HOME = $jdk.FullName
        }
    }

    $mavenLocal = Join-Path $PSScriptRoot '.tools/apache-maven-3.9.16/bin/mvn.cmd'
    if (Test-Path $mavenLocal) {
        $maven = $mavenLocal
    } else {
        $comando = Get-Command mvn.cmd -ErrorAction SilentlyContinue
        if (-not $comando) { throw 'Maven nao encontrado. Instale o Maven e adicione sua pasta bin ao PATH.' }
        $maven = $comando.Source
    }

    & $maven javafx:run
    $resultado = $LASTEXITCODE
} finally {
    Pop-Location
}
exit $resultado
