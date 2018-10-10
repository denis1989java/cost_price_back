#!/bin/bash
JARFile="$2"
PIDFile="$3"
JVM_OPTS="$4"


function check_if_process_is_running {
  if ps -p $(print_process) > /dev/null
  then
     return 0
  else
     return 1
  fi
}

function print_process {
    echo $(<"$PIDFile")
}

case "$1" in
  status)
    if check_if_process_is_running
    then
      echo $(print_process)" is running"
    else
      echo "Process not running: $(print_process)"
    fi
    ;;
  stop)
    if ! check_if_process_is_running
    then
      echo "Process $(print_process) already stopped"
      exit 0
    fi
    kill -TERM $(print_process)
    echo -ne "Waiting for process to stop"
    NOT_KILLED=1
    for i in {1..20}; do
      if [ ! -f $PIDFile ]
      then
        NOT_KILLED=0
        break
      fi

      if check_if_process_is_running
      then
        echo -ne "."
        sleep 1
      else
        NOT_KILLED=0
        break
      fi
    done
    echo
    if [ $NOT_KILLED = 1 ]
    then
      echo "Cannot kill process $(print_process)"
      exit 1
    fi
    echo "Process stopped"
    ;;
  start)
    if [ -f $PIDFile ] && check_if_process_is_running
    then
      echo "Process $(print_process) already running"
      exit 1
    fi
    nohup java $JVM_OPTS -jar $JARFile >/dev/null 2>/dev/null &
    echo "Process started"
    ;;
  run)
    if [ -f $PIDFile ] && check_if_process_is_running
    then
      echo "Process $(print_process) already running"
      exit 1
    fi
    java $JVM_OPTS -jar $JARFile
    ;;
  start-force)
    $0 stop "$2" "$3" "$4"
    $0 start "$2" "$3" "$4"
    ;;
  restart)
    $0 stop  "$2" "$3" "$4"
    if [ $? = 1 ]
    then
      exit 1
    fi
    $0 start  "$2" "$3" "$4"
    ;;
  *)
    echo "Usage: $0 {start|run|stop|restart|status|start-force}"
    exit 1
esac

exit 0
