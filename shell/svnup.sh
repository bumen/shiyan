#/bin/bash

DATE=`date +%Y%m%d%H%M%S`

#cp dddd.jar ddd_$DATE.jar

svn st | awk '{if ($1 == "?") {print $2}}' | xargs svn add

svn commit --username sba --password ba -m 'ci version_commit!'
