@echo off

goto initconfig

:initpath
echo administrator environment Path init  

set regPath=HKEY_CURRENT_USER\Environment

reg query %regPath% /v path 1>nul 2>nul
if %errorlevel% neq 0 ( 
    goto simpleaddpath
)

for /f "tokens=3,4" %%i in ('reg query %regPath% /v path ') do set ADMIN_PATH=%%i

echo administrator environment Path=%ADMIN_PATH%
echo administrator environment Path init end
echo.

if not defined CONFIG_PATH echo no path && echo. && goto EOF
if "%CONFIG_PATH%"=="" echo no path && echo. && goto EOF

:initfilter
echo filter open 

set bmnUsrHomeTmp=%BMN_USR_HOME%
set BMN_USR_HOME=
set loopPath=%CONFIG_PATH%

echo loop yourself environment path
echo.

:loopfilter

for /f "tokens=1,* delims=;" %%i in ("%loopPath%") do (
    set loopValue=%%i
    set loopPath=%%j
)

echo %ADMIN_PATH% | findstr /c:%loopValue% 1>nul 2>nul
if %errorlevel% neq 0 (
    set BMN_PATH=%BMN_PATH%;%loopValue%
)

echo loop loopValue=%loopValue%
echo loop loopPath=%loopPath%
echo loop BMN_PATH=%BMN_PATH%
echo.

if defined loopPath (
    goto loopfilter
)

echo loop yourself environment path end

set BMN_USR_HOME=%bmnUsrHomeTmp%
set bmnUsrHomeTmp=
set loopValue=

echo filter close 
echo.

:: filter finish

goto addhome

:addpath
echo add path

echo.
echo add Path OLD_PATH=%ADMIN_PATH%
echo.
echo add Path BMN_PATH=%BMN_PATH%
echo.
echo add Path PATH=%ADMIN_PATH%;%BMN_PATH%

if not "%BMN_PATH%" == "" (
    reg add %regPath% /v Path /t REG_EXPAND_SZ /d "%ADMIN_PATH%;%BMN_PATH%" /f
)

echo.
echo add path end
echo.

echo config environment success, but need yourself to refresh config.
goto EOF

:simpleaddpath
echo administrator environment Path no found 
set BMN_PATH=%CONFIG_PATH%

:addhome
echo add home

echo add BMN_USR_HOME=%BMN_USR_HOME%
reg add %regPath% /v BMN_USR_HOME /t REG_SZ /d %BMN_USR_HOME% /f

if not defined CONFIG_HOME echo no home && echo. && goto addpath
if "%CONFIG_HOME%"=="" echo no home && echo. && goto addpath

:loophome
echo loop home

for /f "tokens=1,* delims=;" %%i in ("%CONFIG_HOME%") do (
    set CONFIG_HOME=%%j
    
    set homename=%%i
)

:: delims=$ can not match whitespace, so path can had whitespace
if defined %homename% for /f "delims=$" %%i in ('call echo %%%homename%%%') do set homevalue=%%i

if defined %homename% (
    echo add home %homename%=%homevalue%
    reg add %regPath% /v %homename% /t REG_SZ /d %homevalue% /f
)

if defined CONFIG_HOME (
    goto loophome
)
echo add home end
echo.

goto addpath

:EOF
echo finish success
pause > null
exit /b 0

:initconfig
echo yourself environment Path config init 

:: root home default
set BMN_USR_HOME=E:\data\home\user00\bsr\usr

set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_102"

:: add home environment
::set CONFIG_HOME=TEST_HOME1;TEST_HOME2

set CONFIG_HOME=JAVA_HOME
echo yourself environment Path CONFIG_HOME=%CONFIG_HOME%

set PYTHON_HOME=%%BMN_USR_HOME%%\python

set NGINX_HOME=%%BMN_USR_HOME%%\nginx

set MYSQL_HOME=%%BMN_USR_HOME%%\mysql

set REDIS_HOME=%%BMN_USR_HOME%%\redis

set MONGODB_HOME=%%BMN_USR_HOME%%\mongo

set TOMCAT_HOME=%%BMN_USR_HOME%%\tomcat

set MAVEN_HOME=%%BMN_USR_HOME%%\maven

:: add path environment 
set CONFIG_PATH=%PYTHON_HOME%;%NGINX_HOME%;%REDIS_HOME%;%MYSQL_HOME%\bin;%MONGODB_HOME%\bin;%TOMCAT_HOME%\bin;%MAVEN_HOME%\bin;

echo yourself environment Path CONFIG_PATH=%CONFIG_PATH%
echo yourself environment Path config init end
echo.

goto initpath
