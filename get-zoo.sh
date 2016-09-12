#!/bin/bash
echo -e "\e[1;33m"
wget  http://apache-mirror.rbc.ru/pub/apache/zookeeper/zookeeper-3.4.8/zookeeper-3.4.8.tar.gz
echo 'Unpacking zookeeper'
mkdir zookeeper && tar -xf zookeeper-3.4.8.tar.gz -C ./zookeeper --strip-components 1
echo 'Clear temp file'
rm ./zookeeper-3.4.8.tar.gz
echo -e "\e[39m"
