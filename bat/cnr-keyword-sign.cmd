@echo off

@setlocal
chcp 65001

set ERROR_CODE=0

set /P WORDS=<words.txt

java -cp baw-test-1.0-SNAPSHOT.jar KeywordSignature %WORDS%

if ERRORLEVEL 1 goto stop
goto end

@REM error
:stop
echo [ERROR] signature word error
goto error

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

echo signature finish!

exit /B %ERROR_CODE%