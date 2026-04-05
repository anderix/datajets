@echo off
cd src
echo Compiling source...
javac -d ..\bin\ *.java
echo Build complete!
echo Running application...
cd ..\bin
java HelloDataJet
cd ..
pause
