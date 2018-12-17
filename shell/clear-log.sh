#!/bin/sh


LOG_FILE=console.log

if [ -f "$LOG_FILE" ]
then
  echo
  echo Clear the $LOG_FILE ...
  echo "clear ok"> $LOG_FILE
  tailf $LOG_FILE
else
    echo "$LOG_FILE File already Cleared!"
fi


