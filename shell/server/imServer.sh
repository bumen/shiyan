BZBIN="${BZ_SOURCE-$0}"
BZBIN="$(dirname "${BZBIN}")"
BZBINDIR="$(cd "${BZBIN}"; pwd)"

LOGCFG=log4j2-test.xml

#
. "$BZBINDIR"/imEnv.sh

if $cygwin
then
    # cygwin has a "kill" in the shell itself, gets confused
    KILL=/bin/kill
else
    KILL=kill
fi


if [ -z "$BZPIDFILE" ]; then
    BZPIDFILE="$BZBINDIR/im_server.pid"
else
    # ensure it exists, otw stop will fail
    mkdir -p "$(dirname "$BZPIDFILE")"
fi

if [ "x$BZCFG" = "x" ]
then
    BZCFG="wildfirechat.conf"
fi

if [ ! -w "$LOG_DIR" ] ; then
mkdir -p "$LOG_DIR"
fi


if $cygwin
then
    LOG_DIR=`cygpath -wp "$LOG_DIR"`
    BZCFGDIR=`cygpath -wp "$BZCFGDIR"`
fi


IM_JVM_OPTS="-Dlogging.path=${LOG_DIR} -Dlog4j.configurationFile=${BZCFGDIR}/${LOGCFG}"

IM_JVM_OPTS="-Dwildfirechat.path=${BZCFGDIR}/${BZCFG} ${IM_JVM_OPTS}"

IM_JVM_OPTS="-Dcom.mchange.v2.c3p0.cfg.xml=${BZCFGDIR}/c3p0-config.xml ${IM_JVM_OPTS}"

IM_JVM_OPTS="-Djava.security.egd=file:/dev/./urandom ${IM_JVM_OPTS}"

IM_JVM_OPTS="-javaagent:lib/CHR-1.0.2.jar -DClassHotReloadJarPath=config/reload.conf -DClassHotReloadLogDir=logs/reload ${IM_JVM_OPTS}"

IM_JVM_OPTS="-Dbolt.netty.buffer.pooled=true ${IM_JVM_OPTS}"

# hazelcast jdk9+ support
JVMFLAGS="--add-modules java.se \
  --add-exports java.base/jdk.internal.ref=ALL-UNNAMED \
  --add-opens java.base/java.lang=ALL-UNNAMED \
  --add-opens java.base/java.nio=ALL-UNNAMED \
  --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
  --add-opens java.management/sun.management=ALL-UNNAMED \
  --add-opens jdk.management/com.ibm.lang.management.internal=ALL-UNNAMED \
  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED"


JVMFLAGS="$JVMFLAGS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs \
    -XX:-OmitStackTraceInFastThrow -XX:AutoBoxCacheMax=20000 -XX:+ExplicitGCInvokesConcurrent \
    -Xlog:gc*=debug:file=logs/gc_%t.log:time,uptime,tags \
    -XX:ErrorFile=logs/hs_error%p.log \
    -XX:+UseG1GC -XX:G1ReservePercent=10 \
    -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m \
    -Xms512m -Xmx512m $JVMFLAGS"

BZMAIN="cn.Server"

_BZ_DAEMON_OUT="$LOG_DIR/im-$USER-server-$HOSTNAME.out"

case $1 in
start)
    echo -n "Starting IM ..."
    if [ -f "$BZPIDFILE" ]; then
        if kill -0 `cat "$BZPIDFILE"` > /dev/null 2>&1; then
            echo $command already runnning as process `cat "$BZPIDFILE"`.
            exit 1
        fi
    fi
    nohup "$JAVA" "-Dim-server" \
    $IM_JVM_OPTS \
    $JVMFLAGS \
    -cp "$CLASSPATH" $BZMAIN > $_BZ_DAEMON_OUT 2>&1 < /dev/null &
    if [ $? -eq 0 ]
    then
        /bin/echo -n $! > "$BZPIDFILE"
        if [ $? -eq 0 ];
        then
            sleep 1
            pid=$(cat "${BZPIDFILE}")
            if ps -p "${pid}" > /dev/null 2>&1; then
                echo STARTED
            else
                echo FAILED TO START
                exit 1
            fi
        else
            echo FAILED TO WRITE PID
            exit 1
        fi

    else
        echo SERVER DID NOT START
        exit 1
    fi
    ;;
start-fg)
    BZ_CMD=(exec "$JAVA")
    if [ "${BZ_NOEXEC}" != "" ]; then
        BZ_CMD=("$JAVA")
    fi
    "${BZ_CMD[@]}" "-Dim-server" \
    $IM_JVM_OPTS \
    $JVMFLAGS \
    -cp "$CLASSPATH" $BZMAIN
    ;;
print-cmd)
    echo "\"$JAVA\" -Dim-server \
    $IM_JVM_OPTS \
    $JVMFLAGS \
    -cp \"$CLASSPATH\" $BZMAIN > \"$_BZ_DAEMON_OUT\" 2>&1 < /dev/null"
    ;;
stop)
    echo -n "Stopping IM ..."
    if [ ! -f "$BZPIDFILE" ]
    then
        echo "no im to stop (could not find file $BZPIDFILE)"
    else
        . "$BZBINDIR"/shutdown.sh
        pid=$(cat "${BZPIDFILE}")
        for i in `seq 10`
        do
            if ps -p "${pid}" > /dev/null 2>&1; then
                echo -n .
                sleep 1
            else
                pid=
                break
            fi
        done
        if [ -z "$pid" ]; then
            rm -f "$BZPIDFILE"
            echo STOPPED
        else
            echo "FAILED TO STOP: $pid"
        fi
    fi
    exit 0
    ;;
restart)
    shift
    "$0" stop ${@}
    sleep 3
    "$0" start ${@}
    ;;
*)
    echo "Usage: $0 {start|start-fg|stop|restart|print-cmd}" >&2

esac
