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
set ANT-PARAM="kingcore_jar"

@rem set BuildPath=E:\Woo_Java\java_pub\prj_pub\build
@rem set BuildDisk=E:
@rem set FileName=wpub_j142.jar
@rem set CopyFrom=E:\Woo_Java\java_pub\prj_pub\temporary\classes
@rem set DestPath=D:\

@rem ***************** ����ָ��Ŀ¼��ant
@rem cd %BuildPath%
@rem CHDIR

@rem call run-ant.cmd
call .\run-ant.cmd"

@rem ***************** �����ļ�,ת�Ƶ�ant��
@rem delete the old file
@rem del %DestPath%\%FileName%
@rem copy file
@rem copy %CopyFrom%\%FileName% %DestPath%\%FileName%

@rem drop the "pause" when this file is call by Editplus2 or JBuild.
pause

@echo on

@ENDLOCAL
