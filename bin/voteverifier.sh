#!/bin/sh
# Batch file for running the VoteVerifier application, Java 6 or 7 on Linux assumed
#

# Define installation location. Installer assumed
# defining a value for $INSTALL_PATH.
INSTALL_DIR=%{INSTALL_PATH}

JARFILE=$INSTALL_DIR/VoteVerifier-%{VERSION}-jar-with-dependencies.jar

# Set current directory to INSTALL_DIR
cd $INSTALL_DIR

# Determine JAVA_HOME (Java 6 or 7 assumed)
if [ -z "$JAVA_HOME" ] ; then
	JAVA_HOME=`/usr/libexec/java_home`
fi

# echo $JAVA_HOME/bin/java -jar $JARFILE $*
$JAVA_HOME/bin/java -jar $JARFILE $*
