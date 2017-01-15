@echo off & setlocal enabledelayedexpansion

rem LIB_PATH用来设置后台服务JAR存放的目录,需要按不同的机器指向不同的目录
rem 默认指向CRM前台子项目的输出路径,但是CRM引用的是client包,实际上后期这样将启动不了后台服务
rem 最好是手工在机器上建个LIB目录,再把需要的包复制进去
set LIB_PATH=..\target\exams\WEB-INF\lib

set CURRENT_DIR=%cd%

set LIB_JARS=""
cd %LIB_PATH%
for %%i in (*) do set LIB_JARS=!LIB_JARS!;%LIB_PATH%\%%i

@echo %LIB_JARS%
cd %CURRENT_DIR%

if ""%1"" == ""debug"" goto debug
if ""%1"" == ""jmx"" goto jmx

java -Xms64m -Xmx1024m -XX:MaxPermSize=64M -classpath ..\conf;..\target\classes;;%LIB_JARS% com.alibaba.dubbo.container.Main
goto end

:debug
java -Xms64m -Xmx1024m -XX:MaxPermSize=64M -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -classpath ..\conf;%LIB_JARS% com.alibaba.dubbo.container.Main
goto end

:jmx
java -Xms64m -Xmx1024m -XX:MaxPermSize=64M -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -classpath ..\conf;%LIB_JARS% com.alibaba.dubbo.container.Main

:end
pause