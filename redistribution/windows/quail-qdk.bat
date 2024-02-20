@echo off

if exist "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" (
    "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" -jar "%QUAIL_HOME%\qdk.jar" %*
)
if not exist "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" (
    java -jar "%QUAIL_HOME%\qdk.jar" %*
)