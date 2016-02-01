#!/bin/bash

# ###TO_DEMO###

echo 2 > /tmp/zoo_demo_2/myid

ZOO_PATH=../../../zookeeper/bin

$ZOO_PATH/zkServer.sh start ./conf/zoo2.conf

