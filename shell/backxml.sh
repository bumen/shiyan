#!/bin/bash

DIR=/home/youai/xmlbackup/
DATE=`xml_(date +%F)`
BACDDIR=/mnt/data/java/jar/xml/

cd $DIR && mkdir $DATE
cd $DATE && cp $BACDDIR -r ./



