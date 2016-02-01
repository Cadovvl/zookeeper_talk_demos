#!/bin/bash

# ###TO_DEMO###

[ -d /tmp/zoo_demo_3 ] || mkdir -p /tmp/zoo_demo_3
echo 3 > /tmp/zoo_demo_3/myid

ZOO_PATH=../../../zookeeper/bin

$ZOO_PATH/zkServer.sh start ./conf/zoo3.conf

