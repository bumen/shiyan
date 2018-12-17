#!/bin/sh

STDOUT=`pwd`/stdout.log
STDERR=`pwd`/stderr.log

ARGS=
APPID=gm-server

CLASS_NAME=com.ourpalm.core.service.Main
#HOST_NAME=`hostname -i`


CLASS_PATH=./:./lib/*

pid="run.pid"

start()
{
if [ -f "$pid" ]
        then
                echo
                echo The $SERVER_NAME Server already Started!
                echo
        else
                echo Start The $SERVER_NAME Server....

               java -D$APPID -Xms512m -Xmx1024m -Xmn128m  \
          -XX:+UseG1GC -XX:+PrintGCDetails  -Xloggc:gc.log \
         -classpath $CLASS_PATH  $CLASS_NAME $ARGS>>$STDOUT 2>>$STDERR&
                echo $! > $pid
                sleep 3
                run=`cat $pid`
                counter=`ps -p $run|grep -v PID |wc -l`
                if [ $counter -eq 1 ]
                        then
                                echo The $APPID Server 已经启动完成!
                                echo
                        else

                echo The $APPID Server 启动失败
                                echo 喊大哥帮忙看一眼吧!
                                echo
                fi
fi

#log start

}

stop()
{
if  [ -f "$pid" ]
        then
                echo
                echo Stop The $APPID Server....
                echo

                kill `cat $pid`
                sleep 10
                run=`cat $pid`
                counter=`ps -p $run|grep -v PID |wc -l`

                if [ $counter -eq 1 ]
                    then
                        echo The $APPID Server NOT Stoped!
                        echo please Check the system
                        echo
                        kill -9 `cat $pid`
                        echo "The $APPID Server Stoped"
                        else
                            echo The $APPID Server Stoped
                            echo
                fi
                rm -f $pid

        else
                echo
                echo The $APPID Server already Stoped!
                echo
fi

#log stop

}

status()
{
echo
if [ -f "$pid" ]
        then
                                echo "The $SERVER_NAME Server Running(`cat $pid`)!"
                                echo
        else
                echo
                echo The $APPID Server NOT Running!
                echo
fi
}

case "$1" in
'start')
                start
        ;;
'stop')
                stop
        ;;
'restart')
        stop
        start
    ;;
'status')
                status
        ;;
*)
        echo
        echo
        echo "Usage: $0 {status | start | stop }"
        echo
        echo Status of $APPID Servers ......
                status
        ;;
esac
exit 0
