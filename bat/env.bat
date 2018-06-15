@ECHO OFF

echo.
echo    **************************************************************
echo    *  用于配置环境变量的批处理程序                              *
echo    *  配置JAVA环境变量输入: 1                                   *
echo    *  配置Python环境变量输入：2                                 *
echo    *  配置C环境变量输入: 3                                      *
echo    *  以下注意事项：                                            *
echo    *  1）环境变量只对当前用户有效                               *
echo    *  2）配置前会先备份注册表(备份到所在文件夹的backup.reg)     *
echo    *  3）如配置有问题请运行backup.reg还原                       *
echo    *  4）有些杀毒软件会报安全警报，请点击信任                   *
echo    *                                --scott     *
echo    **************************************************************


SET /P inputKey="输入要配置的环境变量："
::用户变量存储路径
SET regPath= HKEY_CURRENT_USER\Environment
::系统变量存储路径，路径中有空格要用引号括起来
::SET regPath=HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Session" "Manager\Environment
set key=path

::判断用户配置的键(HKEY_CURRENT_USER\Environment\path)是否有值
reg query %regPath% /v %key% 1>nul 2>nul
if %ERRORLEVEL%==0 (
::取注册表中的键值(HKEY_CURRENT_USER\Environment\path)存入%oldValue%变量中
::复制内容后在末尾加上新的值再保存到path中，避免覆盖原值
For /f "tokens=3,4 delims= " %%v in ('Reg Query %regPath% /v %key% ') do Set oldValue=%%v 
) else Set oldValue="."

if %inputKey%==1 ( goto java )
if %inputKey%==2 ( goto python )
if %inputKey%==3 ( goto c )

:java
SET /P JDKPath="请输入JDK安装路径，bin文件夹上一层(例如：C:\Java\jdk1.8.0_31) :"
::%~dp0指的当前目录
ECHO "备份注册表。。。"
reg export %regPath% %~dp0backup.reg
echo=
ECHO "设置JAVA_HOME"
SETX JAVA_HOME "%JDKPath%"
echo=
ECHO "设置CLASSPATH" 
SETX CLASSPATH ".;%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%\lib\dt.jar;%%JAVA_HOME%%\jre\lib\rt.jar" 
echo=
ECHO "设置PATH"

::下面两种方式(setx、reg add )效果一样。不过setx会截断字符,1024字符以上的用reg add方式。
if "%oldValue%"==""."" ( SETX PATH "%%JAVA_HOME%%\bin" ) else SETX PATH "%oldValue%;%%JAVA_HOME%%\bin"
::if "%oldValue%"==""."" (reg add %regPath% /v Path /t REG_EXPAND_SZ /d "%%JAVA_HOME%%\bin" /f) else reg add %regPath% /v Path /t REG_EXPAND_SZ /d "%oldValue%;%%JAVA_HOME%%\bin" /f
echo=
SET /P a="配置已完成,按回车退出。。。"
::start "" "%JDKPath%\bin\javac.exe"
exit


:python
SET /P PyPath="请输入Python安装路径(例如：C:\Python) :"
ECHO "备份注册表。。。"
reg export %regPath% %~dp0backup.reg
echo=
ECHO "设置PATH"
::下面两种方式(setx、reg add )效果一样。不过setx会截断字符,1024字符以上的用reg add方式。
if "%oldValue%"==""."" (SETX PATH %PyPath%) else SETX PATH "%oldValue%;%PyPath%"
::if "%oldValue%"==""."" (reg add %regPath% /v Path /t REG_EXPAND_SZ /d "%PyPath%" /f) else reg add %regPath% /v Path /t REG_EXPAND_SZ /d "%oldValue%;%PyPath%" /f
echo=
SET /P a="配置已完成,按回车退出。。。"
::start "" "%PyPath%\python.exe"
exit

:c
SET /P CPath="请输入Dev-Cpp安装路径,bin文件夹上一层(例如：C:\Dev-Cpp) :"
ECHO "备份注册表。。。"
reg export %regPath% %~dp0backup.reg
echo=
ECHO "设置PATH"
::下面两种方式(setx、reg add )效果一样。不过setx会截断字符,1024字符以上的用reg add方式。
if "%oldValue%"==""."" (SETX PATH "%CPath%") else SETX PATH "%oldValue%;%CPath%\bin"
::if "%oldValue%"==""."" (reg add "%regPath%" /v Path /t REG_EXPAND_SZ /d "%CPath%\bin" /f) else reg add %regPath% /v "Path" /t REG_EXPAND_SZ /d "%oldValue%;%CPath%\bin" /f
echo=
SET /P a="配置已完成,按回车退出。。。"
::start "" "%CPath%\bin\gcc.exe"
exit
