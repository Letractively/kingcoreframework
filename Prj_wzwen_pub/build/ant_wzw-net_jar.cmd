@rem ************************************************************
@rem * author wuzewen
@rem * date 2005-1-12
@rem * make file scm.war.
@rem * all content in this file is like be typed on dos-mode.
@rem ************************************************************

@rem ***************** �����ʼ
@echo off
SETLOCAL

@rem ***************** init
@rem set BuildPath=E:\Work_Java\deploy\build
@rem set BuildDisk=E:
@rem set FileName=wzw.jar
set ANT-PARAM="wzw-net_jar"
@rem set CopyFrom=E:\Work_Java\deploy\temp\classes
@rem set DestPath=E:\


@rem ***************** ����ָ��Ŀ¼��ant
@rem cd %BuildPath%
@rem CHDIR

@rem call run-ant.cmd
@rem call "%BuildPath%\run-ant.cmd"
call run-ant.cmd

@rem ***************** �����ļ�
@rem delete the old file
@rem del %DestPath%\%FileName%
@rem copy file
@rem copy %CopyFrom%\%FileName% %DestPath%\%FileName%

@rem drop the "pause" when this file is call by Editplus2 or JBuild.
pause

@echo on

@ENDLOCAL
