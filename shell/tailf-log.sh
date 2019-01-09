#!/bin/sh


LOG_FILE=console.log

if [ -f "$LOG_FILE" ]
then
  echo
  echo Clear the $LOG_FILE ok!
  cat /dev/null > $LOG_FILE
  tail -f $LOG_FILE
else
    echo "$LOG_FILE File already Cleared!"
fi


