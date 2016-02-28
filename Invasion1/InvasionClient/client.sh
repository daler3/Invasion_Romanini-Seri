#!/bin/bash
DIR=/home/timmy/Scrivania/Invasion1
CLASSPATH=.:$DIR/InvasionClient/Client.jar:$DIR/InvasionCondiriso/Condiriso.jar
java -cp $CLASSPATH -Djava.security.policy=$DIR/java.policy client.Start
