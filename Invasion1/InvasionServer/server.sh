#!/bin/bash
DIR=/home/timmy/Scrivania/Invasion1
CLASSPATH=.:$DIR/InvasionServer/Server.jar:$DIR/InvasionCondiriso/Condiriso.jar:$DIR/InvasionServer/driverdb/derby.jar:$DIR/InvasionServer/driverdb/derbyLocale_it.jar

CODEBASE=file:$DIR/InvasionCondiriso/Condiriso.jar

java -cp $CLASSPATH -Djava.rmi.server.codebase=$CODEBASE -Djava.security.policy=java.policy server.StartServer
