# .bash_profile

# Get the aliases and functions

BMN_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo bmn usr home is $BMN_HOME

if [ -f $BMN_HOME/.bashrc ]; then
	. $BMN_HOME/.bashrc
fi

# User specific environment and startup programs


FIRST_PATH=`echo $PATH | cut -d : -f 1`

echo first path is $FIRST_PATH

if [ "$BMN_HOME/bin" = "$FIRST_PATH" ] ; then
	echo path already had: $FIRST_PATH;
	return;
fi

PATH=$BMN_HOME/bin:$PATH

export PATH

echo BMN home path init success