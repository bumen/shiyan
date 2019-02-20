#!/bin/sh

PID_PATH=/tmp/app.pid
# 配置app
APPS=(
pattern
word
)

JVM_OPS="-jar "

start() {
  echo begin start pattern...
  if [ -n "$PID" ]; then
    echo $APP already running pid: $PID. start fail
    return
  fi

  JVM_OPS="$JVM_OPS $APP-*.jar"

  echo $JVM_OPS
  nohup java $JVM_OPS >>nohup.log & echo $! > $PID_FILE

  echo $APP is running pid: $!
}
stop() {
  echo begin stop pattern...
  if [ -z "$PID" ]; then
    echo $APP already stop
    return
  fi

  kill $PID
  echo -n stopping $APP...
  for i in `seq 10`
  do
    echo -n .
    del_invalid_pid_file
    if [ $? -eq 0]; then
      echo stopped
      PID=
      return
    fi
    sleep 1
  done

  kill -9 $PID
  echo force stoped
}

get_pid_by_ps() {
  ps aux | grep java | grep $APP | awk -F " " '{print $2}'
}

check_pid() {
  ps $1 | grep java | grep -q $APP
}

del_invalid_pid_file() {
  if ! check_pid $PID; then
    echo "Removing stale PID file: $PID_FILE"
    rm -f $PID_FILE
    PID=
    return 0
  fi
  return 1
}

get_pid_in_file() {
  PID_FILE=${PID_PATH/app/$APP}
  cat "$PID_FILE" 2>/dev/null || true
}


app_pid() {
  PID=$(get_pid_in_file)
  if [ -n "$PID" ]; then
    echo pid by file
    del_invalid_pid_file
  else
    echo pid by ps
    PID=$(get_pid_by_ps)
  fi
}

init_app() {
  for app in ${APPS[@]}
  do
    if [ $app == $1 ]; then
      APP=$1
      break;
    fi
  done

  if [ -z $APP ]; then
    echo "Unknown app $1"
  fi
}

init_app $1
if [ -z $APP ]; then
  exit 1
fi

app_pid

case "$2" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  pid)
    echo $PID
    ;;
  *)
    echo "Unknown command $2"
    exit 1
    ;;
esac