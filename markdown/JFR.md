### java11 JFR
 * 开启JFR
   + #JVMFLAGS="-XX:StartFlightRecording=disk=true,delay=16s,dumponexit=true,filename=recoding.jfr,maxsize=1024m,maxage=1d -XX:FlightRecorderOptions=repository=/home/playcrab/playcrab/im/jfr $JVMFLAGS"
   +  jcmd [pid] JFR.start disk=true dumponexit=true filename=recording.jfr maxsize=1024m maxage=1d settings=profile name=im01 
   
 * 其它命令查看imJFR.sh