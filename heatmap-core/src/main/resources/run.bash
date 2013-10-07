#!/bin/bash

# settings
DATE_LOG="date.log"

# variables
work_dir="/home/everyaware/heatmap-processor/workspace/basic"
config_file="/home/everyaware/heatmap-processor/config/basic/config.xml"
last_date=`tail -n 1 $work_dir/$DATE_LOG`
active_profiles="minmax"
jar_dir="/home/everyaware/heatmap-processor"

# parse options
while getopts "c:m:w:a:j:" OPTION
do
  case $OPTION in
    c)
      config_file=$OPTARG
      ;;
    m)
      last_date=$OPTARG
      ;;
    w)
      work_dir=$OPTARG
      ;;
    a)
      active_profiles=$OPTARG
      ;;
    j)
      jar_dir=$OPTARG
      ;;
  esac
done

echo "config file: $config_file"

# set current date
current_date=`date +"%Y-%m-%d %H:%M:%S"`

echo "last date: $last_date"
echo "current date: $current_date"

# append current date to date log

echo $current_date >> $work_dir/$DATE_LOG

# compile command

log_file=$work_dir/logs/`echo $current_date | sed "s/ /_/g" | sed "s/:/-/g"`.log

command='
nice -19 
flock --timeout=16 '$work_dir'/lock 
java 
-Xmx16g 
-XX:-UseGCOverheadLimit 
-Dspring.profiles.active="'$active_profiles'"
-Dmin="'$last_date'" 
-Dmax="'$current_date'" 
-Dworkdir="file:'$work_dir'" 
-jar '$jar_dir'/heatmap-core-0.0.1-SNAPSHOT.jar "file:'$config_file'"
> '$log_file' 2>&1'

# run command

echo "COMMAND:"
echo $command
eval $command

