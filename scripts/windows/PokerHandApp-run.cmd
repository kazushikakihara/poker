@echo off
setlocal
cd /d "%~dp0..\..\dist"
echo Enter 5 cards (e.g. AS KS QS JS 10S):
set /p CARDS=
"%cd%\PokerHandApp.exe" %CARDS%
echo.
pause
endlocal
