#!/bin/bash

# ###TO_DEMO###

[ -d /tmp/zoo_demo_2 ] || mkdir -p /tmp/zoo_demo_2
echo 2 > /tmp/zoo_demo_2/myid

ZOO_PATH=../../../zookeeper/bin

$ZOO_PATH/zkServer.sh start ./conf/zoo2.conf

