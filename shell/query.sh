#!/bin/bash
LogName=`date -d today +"%Y-%m-%d"`.log
CurLog="/var/log/${LogName}"
CurPath=`pwd`
ConfigXml="${CurPath}/xml/config.xml"
#get config.xml
PlatForm=`grep Platform ${ConfigXml} | sed 's/</>/g' | awk -F'>' '{print $3}'`
ServerID=`grep ServerId ${ConfigXml} | sed 's/</>/g' | awk -F'>' '{print $3}'`
HibernateConfig="${CurPath}/hibernateconfig/hibernate_${PlatForm}_${ServerID}.cfg.xml"
#get hibernateconfig
DbItems=(`grep jdbc:mysql: ${HibernateConfig}|sed 's/\//:/g'|awk -F: '{print $5,$6,$7}'`)
MysqlHost=${DbItems[0]}
MysqlPort=${DbItems[1]}
MysqlDb=${DbItems[2]}
MysqlUser=$(grep connection.username ${HibernateConfig}|sed 's/</>/g'|awk -F\> '{print $3}')
MysqlPass=$(grep connection.password ${HibernateConfig}|sed 's/</>/g'|awk -F\> '{print $3}')
#print DB items to log
echo "---------------------------------" >> ${CurLog}
echo ${MysqlHost} >> ${CurLog}
echo ${MysqlPort} >> ${CurLog}
echo ${MysqlUser} >> ${CurLog}
echo ${MysqlPass} >> ${CurLog}
echo ${MysqlDb} >> ${CurLog}
echo "---------------------------------" >> ${CurLog}
#create back database
NewDb=${MysqlDb}_back
rm -rf ../repair/*
echo "query data from $NewDb" >> ${CurLog}
/usr/bin/mysql -h${MysqlHost} -P${MysqlPort} -u${MysqlUser} --password=${MysqlPass} -N -e "use ${NewDb};select playerid,itemid from energy_kernel where source =1 order by playerid;" > ../repair/result.txt
