#!/bin/bash
DestLogIP="127.0.0.1"
YesDate=`date -d yesterday +"%Y%m%d"`
rm -f /tmp/PlatformLog.log.*

for eachdir in java java1
do
    if [ -d "/mnt/data/${eachdir}" ];then
        ConfigXml="/mnt/data/${eachdir}/jar/xml/config.xml"
        PlatForm=$(grep Platform ${ConfigXml}|sed 's/</>/g'|awk -F\> '{print $3}')
        ServerID=$(grep ServerId ${ConfigXml}|sed 's/</>/g'|awk -F\> '{print $3}')
        rm -f /tmp/${PlatForm}${ServerID}-${YesDate}.log
        /bin/cp /mnt/data/${eachdir}/logs/PlatformLog.log.20150208 /tmp/${PlatForm}${ServerID}-${YesDate}.log
        rsync -aqzP --password-file=/etc/rsyncd_baw.password /tmp/${PlatForm}${ServerID}-${YesDate}.log baw@${DestLogIP}::mx/log/
    fi
done
