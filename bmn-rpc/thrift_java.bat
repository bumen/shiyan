@REM check thrift version is base pom.xml config version
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off

@setlocal

set ERROR_CODE=0

thrift -r -out java/src/main/java -v --gen java thrift/server.thrift

if ERRORLEVEL 1 goto stop
goto end

@REM thrift error
:stop
echo [ERROR] thrift generate java code error
goto error

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

echo Generate finish!

if "%1" == "on" pause

exit /B %ERROR_CODE%

