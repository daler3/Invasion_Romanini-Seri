#!/bin/bash
myDir="$(dirname "$0")"
parentDir="$(dirname "$myDir")"
rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false &

CLASSPATH=.:$myDir/Server.jar:$parentDir/InvasionCondiriso/Condiriso.jar:$myDir/driverdb/derby.jar:$myDir/driverdb/derbyLocale_it.jar

CODEBASE=file:$parentDir/InvasionCondiriso/Condiriso.jar

java -cp $CLASSPATH -Djava.rmi.server.codebase=$CODEBASE -Djava.security.policy=java.policy server.StartServer
