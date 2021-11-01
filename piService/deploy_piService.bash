#!/bin/bash

# change this path to your own jar file
jarFolderPath="/home/pi"

pid=$( ps aux | grep piService- | grep java | awk '{print $2}' )

if [ -z "${pid}" ]
then
    printf "\e[36m[INFO] No process of piService found\n";
else
    printf "\e[36m[INFO] Old piService pid: %s\n" "${pid}"
    printf "\e[36m[INFO] Killing old piService process ...\n"
    while kill -SIGTERM $pid >/dev/null 2>&1; do 
        sleep 1
    done

    testPid=$( ps aux | grep piService- | grep java | awk '{print $2}' )

    if [ -z "${testPid}" ]
    then
        printf "\e[32m[SUCCESS] Old piService process killed!\n"
    else
        printf "\e[31m[ERROR] Failed to kill old process!\n"
        printf "\e[0m"
        exit 1
    fi
fi


printf "\e[36m[INFO] Executing %s/piService-%s.jar\n" "$jarFolderPath" "$1"
nohup java -jar "${jarFolderPath}/piService-${1}.jar" >/dev/null 2>&1 &

newPid=$( ps aux | grep piService- | grep java | awk '{print $2}' )
if [ -z "${newPid}" ]
then
    printf "\e[31m[ERROR] Failed to start new piService process!\n"
    printf "\e[0m"
    exit 1
else
    printf "\e[32m[SUCCESS] Successfully deployed new piService\n"
fi

printf "\e[0m"
