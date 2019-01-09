# .bash_profile

echo start config sai path
# Get the aliases and functions

SAI_USR_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo sai usr path is $SAI_USR_PATH

if [ -f $SAI_USR_PATH/.bashrc ]; then
	. $SAI_USR_PATH/.bashrc
fi

# User specific environment and startup programs


FIRST_PATH=`echo $PATH | cut -d : -f 1`

echo first path is $FIRST_PATH

if [ "$SAI_USR_PATH/bin" = "$FIRST_PATH" ] ; then 
	echo path already had: $FIRST_PATH;
	return;
fi

PATH=$SAI_HOME/bin:$PATH

export PATH

echo sai home path init success
