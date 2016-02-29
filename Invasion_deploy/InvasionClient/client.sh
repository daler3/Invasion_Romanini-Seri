#!/bin/bash
myDir="$(dirname "$0")"
parentDir="$(dirname "$myDir")"
rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false &
CLASSPATH=.:$myDir/Client.jar:$parentDir/InvasionCondiriso/Condiriso.jar
java -cp $CLASSPATH -Djava.security.policy=$parentDir/java.policy client.Start
