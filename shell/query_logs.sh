#!/bin/bash

cd ../logs
cat ActionLog.log.20150204|awk -F'[ ,]' '$4>"18:00:00"{print}'|grep '能量'|grep '\[53,56\]' > ../repair/pieceInfo.log

cat ActionLog.log.20150205|grep '能量'|grep '\[53,56\]' >> ../repair/pieceInfo.log

cat ActionLog.log.20150206|grep '能量'|grep '\[53,56\]' >> ../repair/pieceInfo.log

cat ActionLog.log|grep '能量'|grep '\[53,56\]' >> ../repair/pieceInfo.log


cat ActionLog.log.20150204|awk -F'[ ,]' '$4>"18:00:00"{print}'|grep '融合'|grep '成功' > ../repair/megerInfo.txt

cat ActionLog.log.20150205|grep '融合'|grep '成功' >> ../repair/megerInfo.txt

cat ActionLog.log.20150206|grep '融合'|grep '成功' >> ../repair/megerInfo.txt

cat ActionLog.log|grep '融合'|grep '成功' >> ../repair/megerInfo.txt
