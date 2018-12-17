#/bin/sh



echo `date +%Y%m%d_%H:%M`
DOMAIN_HOME="."
#sh $DOMAIN_HOME/checkConsoleErr.sh $1
mv -f ./console.log ../console_logs/console$1.log
>./console.log
nohup java  -jar -Xmx2048m -Xms2048m naruto_1600.jar>>./console.log &
echo `date +%Y%m%d_%H:%M`
tail -1000f console.log
