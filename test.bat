@ECHO OFF

echo %time:~0,5%>>d:\applet-test\test.log
set /a C=0
:doEcho
if ""%1"" == """" goto eof
SET /a C=C+1
ECHO [%C%] %1
shift


goto doEcho
shift
:eof

echo "Bye"
PAUSE>NUL
@echo on
 