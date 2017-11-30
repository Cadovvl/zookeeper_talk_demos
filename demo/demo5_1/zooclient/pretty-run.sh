#!/bin/bash

javac Main.java -cp ../../../zookeeper/zookeeper-3.4.11.jar

java -cp .:../../../zookeeper/zookeeper-3.4.11.jar:../../../zookeeper/lib/* Main "$@" 2>&1 | ../../../re_colorize.sh red '^x .*' blue '^+ .*' yellow '^- .*' 

