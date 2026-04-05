set classpath=.;c:\java\DataJets\bin\datajets.jar
@echo off
cd src
echo Compiling source...
javac -d ..\bin\ *.java
echo Build complete!
echo Running application...
cd ..\bin
java FirstDataJet
cd ..
pause
