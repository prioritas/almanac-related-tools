@echo off
@echo +------------------------+
@echo ! Sight Reduction Sample !
@echo +------------------------+
@setlocal
set CP=.\classes
set CP=%CP%;..\NauticalAlmanac\deploy\nauticalalmanac.jar
set CP=%CP%;..\GeomUtil\deploy\geomutil.jar
java -classpath %CP% -DdeltaT=65.984 app.raw.SightReductionSample
@endlocal
pause