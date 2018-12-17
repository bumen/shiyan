#!/bin/bash
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
#echo DB items
echo "---------------------------------"
echo ${MysqlHost}
echo ${MysqlPort}
echo ${MysqlUser}
echo ${MysqlPass}
echo ${MysqlDb}
echo "---------------------------------"
#conn mysql
/usr/bin/mysql -h${MysqlHost} -P${MysqlPort} -u${MysqlUser} --password=${MysqlPass} ${MysqlDb}
