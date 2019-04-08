# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs
echo start config BMN HOME
BMN_USR_PATH=~/data/home/user00/bmn/usr

if [ -e $BMN_USR_PATH ];  then
  echo "found BMN HOME"
  . $BMN_USR_PATH/.bash_profile
else
  echo "can't found BMN HOME: $BMN_USR_PATH"
fi