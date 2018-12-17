#!/bin/bash

#server 2
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update

#server 3
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update

#server 4
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update

#server 5
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update

#server 6
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::twoinone

#server 7
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update

#server 8
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::twoinone

#server 9
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update

#server 10
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update

#server 11
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::twoinone

#server 12
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::twoinone

#server 13
rsync -avz -P --exclude "config.xml"  --exclude "console.log" --password-file=/etc/rsyncd.password /mnt/data/java/jar rsyncback@127.0.0.1::update
