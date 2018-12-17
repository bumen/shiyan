#/bin/bash
BasePath=$(echo $(dirname `pwd`))
ConsoleTime=`date +%Y%m%d%H%M`
HeapDir="${BasePath}/heap_dump"
/bin/mv console.log ../console_logs/console_${ConsoleTime}.log
nohup java -jar -Xmx2048m -Xms2048m -XX:PermSize=64m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${HeapDir} x.jar >> console.log &
tail -f console.log
