#!/bin/bash

# ###TO_DEMO###

#[ -d /tmp/zoo_demo_1 ] || mkdir -p /tmp/zoo_demo_1

ZOO_PATH=../../../zookeeper/bin

$ZOO_PATH/zkServer.sh start ./conf/zoo.conf

