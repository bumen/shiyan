#!/bin/sh


function run() {
    runHour=21
    count=0
    while [ `date +%H` -lt $runHour ]
    do
      ./main 1 1 11000 test.pcm

      count=`expr $count + 1`
      echo "run success count: $count"

      sleep 0.5
    done

}


case "$1" in
 start)
   run
   ;;
 *)
   echo "error command $1"
   exit 1
   ;;
esac