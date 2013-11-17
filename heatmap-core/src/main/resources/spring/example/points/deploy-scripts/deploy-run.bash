#!/bin/bash

# variables
work_dir="/home/everyaware/heatmap-processor/workspace/basic"
config_file="/home/everyaware/heatmap-processor/config/basic/config.xml"
jar_dir="/home/everyaware/heatmap-processor"
jar_file="heatmap-core-0.1.0-SNAPSHOT.jar"

# parse options
while getopts "c:m:w:a:j:" OPTION
do
  case $OPTION in
    c)
      config_file=$OPTARG
      ;;
    w)
      work_dir=$OPTARG
      ;;
    j)
      jar_dir=$OPTARG
      ;;
  esac
done

echo "config file: $config_file"

# compile command

log_file=$work_dir/logs/`echo $current_date | sed "s/ /_/g" | sed "s/:/-/g"`.log

command='
nice -19 
flock --timeout=16 '$work_dir'/lock 
java 
-Xmx16g 
-XX:-UseGCOverheadLimit 
-Dworkdir="file:'$work_dir'" 
-jar '$jar_dir'/'$jar_file' "file:'$config_file'"
> '$log_file' 2>&1'

# run command

echo "COMMAND:"
echo $command
eval $command

