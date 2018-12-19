#/bin/sh

version="$(thrift -version | cut -d' ' -f3)"
if [ "$1" != "$version" ]; then
	echo "[ERROR] Required thrift version: $1"
	echo "[ERROR]   Actual thrift version: $version"
	exit 1
fi
