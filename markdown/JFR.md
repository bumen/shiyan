### java11 JFR
 * 开启JFR
   + #JVMFLAGS="-XX:StartFlightRecording=disk=true,delay=16s,dumponexit=true,filename=recoding.jfr,maxsize=1024m,maxage=1d -XX:FlightRecorderOptions=repository=/home/playcrab/playcrab/im/jfr $JVMFLAGS"
   +  jcmd [pid] JFR.start disk=true dumponexit=true filename=recording.jfr maxsize=1024m maxage=1d settings=profile name=im01 
   
 * 其它命令查看imJFR.sh
 
 * dump heap 
   + jhsdb jmap 总失败，不能用。网上没有找到处理方案
   ``` 
    [playcrab@v-im05 im]$ jhsdb jmap  --binaryheap --dumpfile pid.bin --pid `cat im_server.pid`
    Attaching to process ID 3383, please wait...
    Debugger attached successfully.
    Server compiler detected.
    JVM version is 11.0.5+10-LTS
    Exception in thread "main" java.lang.NullPointerException
            at jdk.hotspot.agent/sun.jvm.hotspot.utilities.HeapHprofBinWriter.writeSymbolID(HeapHprofBinWriter.java:1121)
            at jdk.hotspot.agent/sun.jvm.hotspot.utilities.HeapHprofBinWriter.dumpStackFrame(HeapHprofBinWriter.java:752)
            at jdk.hotspot.agent/sun.jvm.hotspot.utilities.HeapHprofBinWriter.dumpStackTraces(HeapHprofBinWriter.java:725)
            at jdk.hotspot.agent/sun.jvm.hotspot.utilities.HeapHprofBinWriter.write(HeapHprofBinWriter.java:434)
            at jdk.hotspot.agent/sun.jvm.hotspot.tools.JMap.writeHeapHprofBin(JMap.java:182)
            at jdk.hotspot.agent/sun.jvm.hotspot.tools.JMap.run(JMap.java:97)
            at jdk.hotspot.agent/sun.jvm.hotspot.tools.Tool.startInternal(Tool.java:260)
            at jdk.hotspot.agent/sun.jvm.hotspot.tools.Tool.start(Tool.java:223)
            at jdk.hotspot.agent/sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
            at jdk.hotspot.agent/sun.jvm.hotspot.tools.JMap.main(JMap.java:176)
            at jdk.hotspot.agent/sun.jvm.hotspot.SALauncher.runJMAP(SALauncher.java:326)
            at jdk.hotspot.agent/sun.jvm.hotspot.SALauncher.main(SALauncher.java:455)
   ```
   + jcmd 3383 GC.heap_dump xxx.hprof
     - 可以使用