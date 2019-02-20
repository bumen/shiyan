  #!/bin/sh
PID_FILE=run.pid
MAIN_FILE=connection-server-1.0.4.jar
DEBUG_OPS=" -agentlib:jdwp=transport=dt_socket,suspend=n,server=y,address=18001"
JVM_OPS=" -Dzyq -DSPRING_PROFILES_ACTIVE=alpha"
JVM_OPS+=" -Djava.library.path=/usr/local/lib"
JVM_OPS+=$DEBUG_OPS

function start() {
   if [ -f "$PID_FILE" ] 
	then 
 		echo 
		echo THE $MAIN_FILE Server already Started!
		echo 
	else 

		RUN_COMMAND="java -jar $JVM_OPS $MAIN_FILE"
   		echo "Start the Command $RUN_COMMAND"
   		nohup $RUN_COMMAND >>console.log &  echo $! > $PID_FILE
   		tailf  console.log
	
  fi
}

function stop() {
   if [ -f "$PID_FILE" ]
     then
       echo
       echo Stop the $MAIN_FILE Server, Please Waiting...
       echo

       kill `cat $PID_FILE`

       for i in {1..5}
       do
         sleep 1
         n=`expr 6 - $i`
         printf "\rWaiting... now %2d" $n
       done
       echo

       run=`cat $PID_FILE`
       counter=`ps -p $run|grep -v PID |wc -l`

       if [ $counter -eq 1 ]
           then
               echo
               echo The $MAIN_FILE Server NOT Stoped!
               echo please Check the system
               echo
               kill -9 `cat $PID_FILE`
               echo "The $MAIN_FILE Server Stoped"
           else
               echo
               echo The $MAIN_FILE Server Stoped
               echo
       fi
       rm -f $PID_FILE
     else
       echo
       echo The $MAIN_FILE Server already Stoped!
       echo
   fi
}

case "$1" in
  start)
    start 
    ;;
  stop)
    stop
    ;;
 *)
    echo "Unknow command $1"
    exit 1
   ;;
esac
