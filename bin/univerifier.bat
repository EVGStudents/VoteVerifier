@echo off
REM Batch file for running the UniVerifier application

REM Define installation location. Installer assumed
REM defining a value for $INSTALL_PATH.
REM set DIRNAME=%~dp0%
set INSTALL_DIR=${INSTALL_PATH}

set JARFILE="%INSTALL_DIR%\UniVerifier-${VERSION}-jar-with-dependencies.jar"

REM Set current directory to INSTALL_DIR
cd %INSTALL_DIR%

REM echo start javaw -jar %JARFILE% %*
start javaw -jar %JARFILE% %*
