#!/usr/bin/env bash
#================================================================================
# BootContainer script by Jason <xiayuqing123@hotmail.com>
# auto deployment and run the application that base on dubbo.
# $Id$
#================================================================================

APPLICATION_HOME="${BASH_SOURCE-$0}"
APPLICATION_HOME="$(dirname "${APPLICATION_HOME}")"
APPLICATION_HOME="$(cd "${APPLICATION_HOME}"; pwd)"

APPLICATION_PID_FILE="$APPLICATION_HOME/../pid/app.pid"

echo "Application Stopping  ... "
case $1 in
later)
    if [ ! -f "$APPLICATION_PID_FILE" ]
    then
        echo "No APPLICATION To Started (could not find file $APPLICATION_PID_FILE)"
    else
        kill $(cat "$APPLICATION_PID_FILE")
        rm "$APPLICATION_PID_FILE"
        echo "Application Stopped"
    fi
    exit 0
;;
*)
    if [ ! -f "$APPLICATION_PID_FILE" ]
    then
        echo "No APPLICATION To Started (could not find file $APPLICATION_PID_FILE)"
    else
        kill -9 $(cat "$APPLICATION_PID_FILE")
        rm "$APPLICATION_PID_FILE"
        echo "Application Stopped"
    fi
exit 0
;;
esac