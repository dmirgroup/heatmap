#!/bin/bash

# settings
DATE_LOG_FILE_NAME="date.log"
SPRING_CONFIG_FILE_NAME="config.xml"

# variables (defaults)

# work directory to store process specific files like logs (relative to where this script is executed)
process_work_dir_path=../../../../../../out/basic/work-process

# the path to the jar file (relative to where this script is executed)
jar_file_path=../../../../../../target/release/heatmap-core-0.1.0-SNAPSHOT.jar

# passed to the jar as "workdir" property (relative to where this script is executed)
jar_work_dir_path=../../../../../../out/basic/work-jar
# passed to the jar as "configdir" property (relative to where this script is executed)
jar_config_dir_path=../../../../../../src/main/resources/spring/example/basic/config

# passed to the jar as first command line argument
jar_spring_config_file_path=$jar_config_dir_path/$SPRING_CONFIG_FILE_NAME

# passed to the char as "minTimestamp" property
timestamp_min="2010-01-01 00:00:00"
# passed to the char as "spring.profiles.active" property
spring_profiles_active=

execute="TRUE"

echo "options (defaults):"
echo "- date log file name: $DATE_LOG_FILE_NAME"
echo "- spring config file name: $SPRING_CONFIG_FILE_NAME"

echo "- process work dir path: $process_work_dir_path"
echo "- jar file path: $jar_file_path"
echo "- jar work dir path: $jar_work_dir_path"
echo "- jar config dir path: $jar_config_dir_path"
echo "- jar spring config file path: $jar_spring_config_file_path"
echo "- timestamp min: $timestamp_min"
echo "- spring profiles active: $spring_profiles_active"
echo "- execute: $execute"


# parse options

while getopts "c:o:y:a:x" OPTION
do
  case $OPTION in
    p)
      process_work_dir_path=$OPTARG
      ;;
    j)
      jar_file_path=$OPTARG
      ;;
    w)
      jar_work_dir_path=$OPTARG
      ;;
    c)
      jar_config_dir_path=$OPTARG
      ;;
    s)
      jar_spring_config_file_path=$OPTARG
      ;;
    o)
      getopts_timestamp_min=$OPTARG
      ;;
    y)
      getopts_timestamp_max=$OPTARG
      ;;
    a)
      spring_profiles_active=$OPTARG
      ;;
    x)
      execute="FALSE"
  esac
done

echo "options:"
echo "- process work dir path: $process_work_dir_path"
echo "- jar file path: $jar_file_path"
echo "- jar work dir path: $jar_work_dir_path"
echo "- jar config dir path: $jar_config_dir_path"
echo "- jar spring config file path: $jar_spring_config_file_path"
echo "- timestamp min: $timestamp_min"
echo "- timestamp max: $timestamp_max"
echo "- spring profiles active: $spring_profiles_active"
echo "- execute: $execute"



# create process work and log dir

if [ ! -d "$process_work_dir_path" ]; then
        mkdir -p $process_work_dir_path
fi

process_work_logs_dir_path=$process_work_dir_path/logs
if [ ! -d "$process_work_logs_dir_path" ]; then
        mkdir -p $process_work_logs_dir_path
fi



# set timestamp min

date_log_file_path=$process_work_dir_path/$DATE_LOG_FILE_NAME
echo "date log file: $date_log_file_path"

if [[ ! -z $getopts_timestamp_min ]]; then
    timestamp_min=$getopts_timestamp_min
    echo "timestamp min (command line): $timestamp_min"
elif [ -e $date_log_file_path ]; then
    timestamp_min=`tail -n 1 $date_log_file_path`
    echo "timestamp min (date log file): $timestamp_min"
else
    echo "timestamp min (default): $timestamp_min"
fi



# get current date and set timestamp max
current_date=`date +"%Y-%m-%d %H:%M:%S"`
echo "current date: $current_date"
if [[ -z $getopts_timestamp_max ]]; then
    timestamp_max=$current_date
    echo "timestamp max (now): $timestamp_max"

    echo "logging timestamp max to date log file: $date_log_file_path"
    echo $timestamp_max >> $date_log_file_path
else
    timestamp_max=$getopts_timestamp_max
    echo "timestamp max (command line): $timestamp_max"
fi



# compile command

## directories & files

opt_lock_file_path=$process_work_dir_path/lock
opt_log_file_path=$process_work_logs_dir_path/`echo $current_date | sed "s/ /_/g" | sed "s/:/-/g"`.log


opt_jar_file_path=$jar_file_path
opt_jar_spring_config_file_path=$jar_spring_config_file_path

opt_jar_config_dir_path="-DconfigDir='$jar_config_dir_path'"
opt_jar_work_dir_path="-DworkDir='$jar_work_dir_path'"

## spring profiles
opt_spring_profiles_active="-Dspring.profiles.active='$spring_profiles_active'"

## dates
opt_min_timestamp="-DminTimestamp='$timestamp_min'"
opt_max_timestamp="-DmaxTimestamp='$timestamp_max'"

command='
nice -19 
flock --timeout=16 '$opt_lock_file_path' 
java 
-Xmx16g 
-XX:-UseGCOverheadLimit 
'$opt_spring_profiles_active' 
'$opt_min_timestamp' 
'$opt_max_timestamp' 
'$opt_jar_config_dir_path' 
'$opt_jar_work_dir_path'  
-jar '$opt_jar_file_path' "file:'$opt_jar_spring_config_file_path'" 
> '$opt_log_file_path' 2>&1'

# run command

echo "COMMAND:"
echo $command
if [ "$execute" = "TRUE" ]; then
    eval $command
fi
