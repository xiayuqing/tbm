@echo off
set JVMFLAGS=-Dfile.encoding=UTF-8
set BASE_HOME=%~dp0%..\

set CLASSPATH=%BASE_HOME%\lib\*;%CLASSPATH%

set TEMPLATE_PATH=%BASE_HOME%\template
set PROJECT_XML_PATH=%BASE_HOME%\xml

set AUTO_MAN_MAIN=org.jason.automan.startup.AutoManBoot

echo Auto-Man Generate started...
java -cp "%CLASSPATH%" %JVMFLAGS% %AUTO_MAN_MAIN% %PROJECT_XML_PATH% %TEMPLATE_PATH%
