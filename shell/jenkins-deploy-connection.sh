#!/bin/sh

PROJECT=connection-server
PID_FILE=/tmp/${PROJECT}.pid

JVM_OPTS="$JVM_OPTS -Dsun.jnu.encoding=UTF-8 -Dfile.encoding=UTF-8 -server"
JVM_OPTS="$JVM_OPTS -Duser.timezone=GMT+8"
JVM_OPTS="$JVM_OPTS -Xmx4g -Xms4g -Xmn3g"
JVM_OPTS="$JVM_OPTS -XX:SurvivorRatio=8 -XX:InitialSurvivorRatio=4 -XX:SoftRefLRUPolicyMSPerMB=0"
JVM_OPTS="$JVM_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp "
JVM_OPTS="$JVM_OPTS -Dcom.sun.management.jmxremote"
JVM_OPTS="$JVM_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
JVM_OPTS="$JVM_OPTS -Dcom.sun.management.jmxremote.port=9999"
JVM_OPTS="$JVM_OPTS -Dcom.sun.management.jmxremote.ssl=false"
JVM_OPTS="$JVM_OPTS -jar /home/jenkins/connection-server/connection-*.jar"

check_pid() {
    ps $1 | grep java | grep -q $PROJECT
}

get_pid_in_file() {
    cat "$PID_FILE" 2>/dev/null || true
}

get_pid_by_ps() {
    ps aux | grep java | grep $PROJECT | awk -F " " '{print $2}'
}

pid=$(get_pid_in_file)
if [ -n "$pid" ]; then
    if ! check_pid $pid; then
        echo Removing stale PID file.
        rm -f $PID_FILE
        pid=
    fi
fi


start() {
    if [ -z "$pid" ]; then
        export LANG=zh_CN.UTF-8
        export LD_LIBRARY_PATH=/usr/local/lib;
    export SPRING_PROFILES_ACTIVE=$1
    echo "stage is", $1

        echo $JVM_OPTS
        java $JVM_OPTS & echo $! >$PID_FILE
    fi
    echo $PROJECT is running, pid: $(get_pid_in_file)
}

stop() {
    if [ -z "$pid" ]; then
        pid=$(get_pid_by_ps)
        if [ -z "$pid" ]; then
            echo $PROJECT is not running
            return 0
        fi
    fi
    kill -9 $pid
    echo -n stopping ${PROJECT}..
    for i in `seq 10`
    do
        echo -n .
        if ! check_pid $pid; then
            rm -f $PID_FILE
            echo stopped
            pid=
            return 0
        fi
        sleep 2
    done
    echo still running
    return 1
}

case "$1" in
    start)
    case "$2" in
        alpha|beta|gamma|sandbox|prod)
        ;;
        *)
        echo "Unknown stage $2"
        exit 1
    esac
        start $2
        ;;
    stop)
        stop
        ;;
    status)
        if [ "$pid" ]; then
                echo $PROJECT is running, pid: $pid
        else
                echo $PROJECT is stopped
        fi
        ;;
    pid)
        echo $pid
        ;;
    *)
        echo "Unknown command $1"
        exit 1
        ;;
esac