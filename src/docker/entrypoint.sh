#!/bin/sh
SYSTEM_PROPERTIES=""

if [ ! -z $GRID_SECRET ]; then
	SYSTEM_PROPERTIES="$SYSTEM_PROPERTIES -DinMemoryGrid.secret=$GRID_SECRET"	
fi
if [ ! -z $GRID_IAMROLE ]; then
	SYSTEM_PROPERTIES="$SYSTEM_PROPERTIES -DinMemoryGrid.iamRole=$GRID_IAMROLE"
fi
if [ ! -z $GRID_INTERFACES ]; then
	SYSTEM_PROPERTIES="$SYSTEM_PROPERTIES -DinMemoryGrid.interfaces=$GRID_INTERFACES"
fi

if [ ! -d /home/config ]; then
  mkdir /home/config
fi
ln -f -s /run/secrets/churchclerk /home/config/application.properties
java $SYSTEM_PROPERTIES -cp /home -jar /home/app.jar $APP_ARG