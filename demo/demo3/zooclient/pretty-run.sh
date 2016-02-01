#!/bin/bash

javac Main.java -cp ../../../zookeeper/zookeeper-3.4.7.jar

java -cp .:../../../zookeeper/zookeeper-3.4.7.jar:../../../zookeeper/lib/* Main 2>&1 | ../../../re_colorize.sh red '^x .*' blue '^+ .*' yellow '^- .*'

