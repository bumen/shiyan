@REM check thrift version is base pom.xml config version
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off


@setlocal

set ERROR_CODE=0

@REM set thrift version
for /f "tokens=3 delims= " %%i in ('thrift -version') do set version=%%i

if not "%1" == "%version%" (goto stop) else echo thrift version ok
goto end
	
:stop
echo [ERROR] Required thrift version: %1
echo [ERROR] Actual thrift version: %version%
goto error

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

exit /B %ERROR_CODE%
