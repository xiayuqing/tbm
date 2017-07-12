#!/usr/bin/env bash
#================================================================================
# tbm script by Jason <xiayuqing123@hotmail.com>
# log collect service
# $Id$
#================================================================================

APPLICATION_HOME="${BASH_SOURCE-$0}"
APPLICATION_HOME="$(dirname "${APPLICATION_HOME}")"
APPLICATION_HOME="$(cd "${APPLICATION_HOME}"; pwd)"

APPLICATION_BASE="$(cd "${APPLICATION_HOME}/../"; pwd)"
APPLICATION_CONFIG="$(cd "${APPLICATION_HOME}/../conf"; pwd)"

JVM_OPTS="-Dfile.encoding=UTF-8 -Dapplication.base=$APPLICATION_BASE -Dapplication.home=$APPLICATION_HOME -Dcatalina.base=$APPLICATION_BASE"

if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA=java
fi

for i in "$APPLICATION_HOME"/../lib/*.jar
do
  CLASSPATH="$i:$CLASSPATH"
done

mkdir -p $APPLICATION_BASE/logs
mkdir -p $APPLICATION_BASE/pid

APPLICATION_MAIN="org.tbm.startup.server.ServerStartup"

APPLICATION_PID_FILE="$APPLICATION_HOME/../pid/app.pid"

echo "Application Starting ..."
if [ -f "$APPLICATION_PID_FILE" ]; then
    if kill -0 `cat "$APPLICATION_PID_FILE"` > /dev/null 2>&1; then
        echo $command already running as process `cat "$APPLICATION_PID_FILE"`.
        exit 0;
    fi
fi
"$JAVA" -cp "$CLASSPATH" ${JVM_OPTS} ${APPLICATION_MAIN} "${APPLICATION_CONFIG}" &

if [ $? -eq 0 ]
then
  if /bin/echo -n $! > "$APPLICATION_PID_FILE"
  then
    sleep 1
    echo "APPLICATION_HOME=$APPLICATION_HOME"
    echo "APPLICATION_BASE=$APPLICATION_BASE"
    echo "APPLICATION_CONFIG=$APPLICATION_CONFIG"
    echo "STARTED"
  else
    echo "FAILED TO WRITE PID"
    exit 1
  fi
else
  echo "APPLICATION DID NOT START"
    exit 1
fi
