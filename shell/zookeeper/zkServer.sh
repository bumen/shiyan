#!/usr/bin/env bash

# use POSTIX interface, symlink is followed automatically
# 替换变量，未定义就是$0

ZOOBIN="${BASH_SOURCE-$0}"
ZOOBIN="$(dirname "${ZOOBIN}")"
# 目的是进入到脚本所在目录
ZOOBINDIR="$(cd "${ZOOBIN}"; pwd)"

if [ -e "$ZOOBIN/../libexec/zkEnv.sh" ]; then
  . "$ZOOBINDIR/../libexec/zkEnv.sh"
else
  . "$ZOOBINDIR/zkEnv.sh"
fi

echo $0, $ZOOBIN, $ZOOBINDIR

