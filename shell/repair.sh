#/bin/sh
echo `date +%Y%m%d_%H:%M`
DOMAIN_HOME="."
#nohup java -jar -Xms1024m -Xmx2048m repair.jar "../console_x.log">>./console.log &
nohup java -jar -Xms1024m -Xmx2048m repair.jar "/mnt/data/console_x.log" >>./console.log
echo `date +%Y%m%d_%H:%M`
tail -f console.log
