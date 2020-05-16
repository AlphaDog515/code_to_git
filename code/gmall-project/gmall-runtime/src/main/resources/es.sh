#!/bin/bash
es_home=/opt/module/es/bin
kibana_home=/opt/module/bin

case $1 in
start)
  for host in hadoop102 hadoop103 hadoop104; do
    ssh $host "source etc/profile; es_home/elasticserach"
  done

  ssh hadoop102 kibana_home/kibana
  ;;

stop)
  for host in hadoop102 hadoop103 hadoop104; do
    ssh $host "source /etc/profile; ps -ef | grep elasticserach | grep -v grep | awk -F ' ' '{print \$2}' | xargs kill -9"

  done
  ssh hadoop102 jps | grep kibana | awk '{print $1}' | xargs kill -9
  ;;

*)
  echo "参数不对"
  ;;

esac
