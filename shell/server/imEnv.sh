#!/bin/sh


if [ -z "$BZBIN" ]; then
    BZBIN="${BZ_SOURCE-$0}"
    BZBIN="$(dirname "${BZBIN}")"
    BZBINDIR="$(cd "${BZBIN}"; pwd)"
fi

#echo "BZBINDIR: $BZBINDIR"

# server directory
BZSERVER_DIR="${BZBINDIR}"

#echo "BZSERVER_DIR: $BZSERVER_DIR"

# server config directory
if [ "x$BZCFGDIR" = "x" ]
then
  if [ -e "${BZSERVER_DIR}/config" ]; then
    BZCFGDIR="${BZBINDIR}/config"
  else
    BZCFGDIR="${BZBINDIR}"
  fi
fi

#echo "BZCFGDIR: $BZCFGDIR"

# log config file
if [ "x$LOGCFG" = "x" ]
then
    LOGCFG="log4j2.xml"
fi

# log directory
if [ "x$LOG_DIR" = "x" ]
then
    LOG_DIR="$BZSERVER_DIR/logs"
fi

if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    JAVA="$JAVA_HOME/bin/java"
elif type -p java; then
    JAVA=java
else
    echo "Error: JAVA_HOME is not set and java could not be found in PATH." 1>&2
    exit 1
fi

LIBPATH=("${BZBINDIR}"/lib/*.jar)

for i in "${LIBPATH[@]}"
do
    CLASSPATH="$i:$CLASSPATH"
done

case "`uname`" in
    CYGWIN*|MINGW*) cygwin=true ;;
    *) cygwin=false ;;
esac

if $cygwin
then
    CLASSPATH=`cygpath -wp "$CLASSPATH"`
fi

#echo "CLASSPATH=$CLASSPATH"



