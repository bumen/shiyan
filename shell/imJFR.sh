BZBIN="${BZ_SOURCE-$0}"
BZBIN="$(dirname "${BZBIN}")"
BZBINDIR="$(cd "${BZBIN}"; pwd)"


JFR_NAME="im01"

BZPIDFILE="$BZBINDIR/im_server.pid"
if [ -f "$BZPIDFILE" ]; then
    SERVER_PID=`cat ${BZPIDFILE}`
else
    echo "SERVER DID NOT START"
    exit 1
fi

#heap
DUMP_TIME=$(date "+%Y_%m_%d_%H_%M_%S")

case $1 in
start)
    echo  "Starting IM JFR ${JFR_NAME} ..."
    jcmd ${SERVER_PID} JFR.start disk=true dumponexit=true filename=recording.jfr maxsize=1024m maxage=1d settings=profile name=${JFR_NAME}
    ;;
dump)
    echo  "Dump IM JFR ${JFR_NAME} ..."
    jcmd ${SERVER_PID} JFR.dump
    ;;
stop)
    echo  "Stopping IM JFR ${JFR_NAME} ..."
    jcmd ${SERVER_PID} JFR.stop name=${JFR_NAME}
    ;;
heap_dump)
    echo "Heap dump IM ..."
    HEAP_DUMP_FILE="hotspot-pid-${SERVER_PID}-${DUMP_TIME}.hprof"
    jcmd ${SERVER_PID} GC.heap_dump ${HEAP_DUMP_FILE}
    ;;
jstack)
   echo "Jstack IM ..."
   DUMP_FILE="hotspot-pid-${SERVER_PID}-${DUMP_TIME}.slog"
   jstack ${SERVER_PID} > ${DUMP_FILE}
   ;;
*)
    echo "Usage: $0 {start|dump|stop|heap_dump|jstack}" >&2

esac
