@rem author wuzewen
@rem date 2004-11-16
@rem make file scm.war.

@rem all content in this file is like be typed on dos-mode.
@rem **********************************************************************************
@rem about *.cmd or *.bat file.
@rem error:	set JAVA_HOME = d:\bea\jdk131_03	-can't have separate
@rem		set JAVA_HOME="d:\bea\jdk131_03"	-can't have "*"
@rem success:	set JAVA_HOME=d:\bea\jdk131_03
@rem
@rem
@rem set ANT_HOME = "d:\ant11"
@rem set PATH=%PATH%;%JAVA_HOME%\bin;%ANT_HOME%\bin;		;to get the path of "javac"
@rem 
@rem 
@rem 
@rem ***************** �����ʼ

@echo off
SETLOCAL

@rem ***************** ��ʼ����������
set ANT_HOME=D:\Program\ant
@rem set JAVA_HOME=D:\bea\jdk142_08
set JAVA_HOME=D:\Program Files\Java\jdk1.5
if "%JAVA_HOME%" == "" set JAVA_HOME=D:\Program files\Java\jdk1.5

set PATH=%PATH%;%JAVA_HOME%\bin;%ANT_HOME%\bin

@rem path

@rem ***************** ��λbulid.xml
@rem echo on
@rem set current path for JBuilder, isn't needed in Editplus2.
@rem CHDIR
@rem cd %BuildPath%
@rem %BuildDisk%
@rem CHDIR

@rem ***************** ����ant����
@rem call ant.bat in ant
%ANT_HOME%\bin\ant.bat %ANT-PARAM%

@echo off
@rem pause
@echo copy file
@echo copy f:\work\newdeploy\web\scm.war D:\bea\user_projects\yyjdomain\applications\scm.war
@rem copy f:\work\newdeploy\web\scm.war D:\bea\user_projects\yyjdomain\applications\scm.war

@rem pause
@rem pause
@rem pause
@rem pause
pause
@echo on

@ENDLOCAL
