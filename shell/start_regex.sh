#!/bin/sh

# 首先配置app, 同时需要添加start_{app}, stop_{app}方法
APPS=(
pattern
word
)

APP_REGEX="^\(apps\)$"
PID_PATH=/tmp/app.pid

###
##
###
start_pattern() {
  echo begin start pattern...
}
stop_pattern() {
  echo begin stop pattern...
}

###
##
###
start_word() {
  echo begin start word-segment...
}
stop_word() {
  echo begin stop word-segment...
}

get_pid_by_ps() {
  if [ -z $app ]; then
    return 1
  fi

    ps aux | grep java | grep $app | awk -F " " '{print $2}'
}

check_pid() {
  if [ -z $app ]; then
    return 1
  fi

  ps $1 | grep java | grep -q $app
}

get_pid_in_file() {
  if [ -z $app ]; then
    return 1
  fi

  pid_file = ${PID_PATH/app/$app}
  cat "$pid_file" 2>/dev/null || true
}

get_pid() {
  pid=$(get_pid_in_file)
  if [ -n $pid ]; then
    if ! check_pid $pid; then
      echo "Removing stale PID file: $pid_file"
      rm -f $pid_file
      pid=
    fi
  else
    pid=$(get_pid_by_ps)
  fi
}



build_apps_regex() {
  len=${#APPS[@]}
  if [ $len -eq 0 ]; then
    return -1
  fi

  for app in ${APPS[@]}
  do
    regex=$regex\\\|$app
  done

  regex=${regex:2}

  apps=${APP_REGEX/apps/$regex}
}

init_app() {
  build_apps_regex
  if [ $? -ne 0 ]; then
    echo "please config APPS."
    return
  fi

  app=`echo $1 | grep $apps`

  if [ -z $app -a !$? ]; then
    echo "Unknown app: $1"
    echo "support apps: ${APPS[@]}"
    return -1
  fi
}

launcher() {

  init_app $2

  if [ $? -ne 0 ]; then
    return
  fi

  $1_$app
}

app_pid() {
  init_app $1

  if [ $? -ne 0 ]; then
    return
  fi

  get_pid
}

case "$2" in
  start)
    launcher start $1
    ;;
  stop)
    launcher stop $1
    ;;
  pid)
    app_pid $1
    ;;
  *)
    echo "Unknown command $1"
    exit 1
    ;;
esac