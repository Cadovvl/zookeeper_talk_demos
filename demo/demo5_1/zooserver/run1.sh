#!/bin/bash

# ###TO_DEMO###

echo 1 > /tmp/zoo_demo_1/myid

ZOO_PATH=../../../zookeeper/bin

$ZOO_PATH/zkServer.sh start ./conf/zoo1.conf

