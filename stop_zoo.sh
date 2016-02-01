#!/bin/bash

ZOO_PATH=./zookeeper/bin

for i in `find . -regex '.*zoo[0-9]?\.conf'`; do
	$ZOO_PATH/zkServer.sh stop $i;
done;
