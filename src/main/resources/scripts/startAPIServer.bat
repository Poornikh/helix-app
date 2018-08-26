cd /d %~dp0
for /f "delims== tokens=1,2" %%G in ("%~dp0/lib/env.properties") do set %%G=%%H

set PATH=%JAVA_HOME%/bin/
echo %JAVA_HOME%

start cmd.exe /k java  -jar "../lib/helix-app-rev001.jar"