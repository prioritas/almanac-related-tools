@echo off
@setlocal
@echo %*
set OLIVSOFT_HOME=..\..
::
cd %~dp0
set CP=%OLIVSOFT_HOME%\all-libs\nauticalalmanac.jar
set CP=%CP%;%OLIVSOFT_HOME%\all-libs\almanactools.jar
set CP=%CP%;%OLIVSOFT_HOME%\all-libs\geomutil.jar
set CP=%CP%;%OLIVSOFT_HOME%\all-3rd-party\xmlparserv2.jar
set CP=%CP%;%OLIVSOFT_HOME%\all-3rd-party\orai18n-collation.jar
set CP=%CP%;%OLIVSOFT_HOME%\all-3rd-party\orai18n-mapping.jar
set CP=%CP%;%OLIVSOFT_HOME%\all-3rd-party\fnd2.zip
set CP=%CP%;%OLIVSOFT_HOME%\all-3rd-party\xdo-0301.jar
::
set XSL_STYLESHEET=.\data2fop_2pages.xsl
set LANG=%1
set WITH_STARS=%2
if [%LANG%] == [FR] (
  echo On parle francais
  set PRM_OPTION=-docconf .\lang_fr.cfg 
  if [%WITH_STARS%] == [false] set PRM_OPTION=-docconf .\lang_fr_ns.cfg 
  copy literals_fr.xsl literals.xsl  
) else (
  echo Will speak English
  copy literals_en.xsl literals.xsl
  set PRM_OPTION=-docconf .\lang_en.cfg 
  if [%WITH_STARS%] == [false] set PRM_OPTION=-docconf .\lang_en_ns.cfg 
)
@echo Publishing
@echo %PRM_OPTION%
@java -Xms256m -Xmx1024m -classpath %CP% oracle.apps.xdo.template.FOProcessor %PRM_OPTION% -xml %3 -xsl %XSL_STYLESHEET% -pdf %4
@echo Done calculating, displaying.
call %4
:end
::pause
@endlocal
exit
