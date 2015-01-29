@setlocal
@echo off
@echo Type "%0 -help" for help
set CP=..\..\all-libs\almanactools.jar
set CP=%CP%;..\..\all-libs\nauticalalmanac.jar
set CP=%CP%;..\..\all-libs\geomutil.jar
set CP=%CP%;..\..\all-libs\nmeaparser.jar
set CP=%CP%;..\..\all-libs\coreutilities.jar
java -cp %CP% app.samples.VikingSunCompass %*
@endlocal
