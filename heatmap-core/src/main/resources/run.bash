#!/bin/bash

# settings
DATE_LOG="date.log"
WORK_DIR="/home/everyaware/heatmap-processor"

# variables
config_file=settings.xml
last_date=`tail -n 1 $WORK_DIR/$DATE_LOG`

# parse options
while getopts "c:m:" OPTION
do
  case $OPTION in
    c)
      config_file=$OPTARG
      ;;
    m)
      last_date=$OPTARG
      ;;
  esac
done

echo "config file: $config_file"

# set current date
current_date=`date +"%Y-%m-%d %H:%M:%S"`

echo "last date: $last_date"
echo "current date: $current_date"

# append current date to date log

echo $current_date >> $WORK_DIR/$DATE_LOG

# compile command

log_file=`echo $current_date | sed "s/ /_/g" | sed "s/:/-/g"`

command='
nice -19 
flock --timeout=16 '$WORK_DIR'/lock 
java 
-Xmx16g 
-XX:-UseGCOverheadLimit 
-Dspring.profiles.active="minmax"
-Dmin="'$last_date'" 
-Dmax="'$current_date'" 
-jar heatmap-core-0.0.1-SNAPSHOT.jar '$config_file'
> '$WORK_DIR'/logs/'$log_file'.log 2>&1'

# run command

echo "COMMAND:"
echo $command
eval $command
