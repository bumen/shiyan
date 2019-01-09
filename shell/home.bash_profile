# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs

SAI_HOME=~/data/home/user00/sai/usr

SAI_PROFILE=$SAI_HOME/.bash_profile
echo $SAI_PROFILE
if [ -f $SAI_PROFILE ]; then
	. "$SAI_PROFILE";
	echo init $SAI_PROFILE success
fi
	

PATH=$PATH:$HOME/.local/bin:$HOME/bin

export PATH
