#!/bin/bash

# settings
DATE_LOG_FILE_NAME="date.log"

# variables (defaults)

jar_file_path=$JAR_FILE_PATH
config_file_path=$CONFIG_FILE_PATH
work_dir_path=$WORK_DIR_PATH

last_date="2010-01-01 00:00:00"
spring_profiles_active=

execute=TRUE

echo "defaults:"
echo "- date log file name: $DATE_LOG_FILE_NAME"
echo "- work dir path: $work_dir_path"
echo "- config file path: $config_file_path"
echo "- last date: $last_date"
echo "- spring profiles active: $spring_profiles_active"
echo "- execute: $execute"


# parse options

while getopts "c:w:o:y:a:x" OPTION
do
  case $OPTION in
    c)
      config_file_path=$OPTARG
      ;;
    w)
      work_dir_path=$OPTARG
      ;;
    o)
      getopts_last_date=$OPTARG
      ;;
    y)
      getopts_current_date=$OPTARG
      ;;
    a)
      spring_profiles_active=$OPTARG
      ;;
    x)
      execute=FALSE
  esac
done

echo "options:"
echo "- config file path: $config_file"
echo "- work dir path: $work_dir_path"
echo "- last late: $getopts_last_date"
echo "- spring active profiles: $spring_profiles_active"

# set last date

date_log_file_path=$work_dir_path/$DATE_LOG_FILE_NAME
echo "date log file: $date_log_file_path"

if [[ ! -z $getopts_last_date ]]; then
    last_date=$getopts_last_date
    echo "last date (command line): $last_date"
elif [ -e $date_log_file_path ]; then
    last_date=`tail -n 1 $date_log_file_path`
    echo "last date (date log file): $last_date"
else
    echo "last date (default): $last_date"
fi

# set current date and append it to date log

if [[ -z $getopts_current_date ]]; then
    current_date=`date +"%Y-%m-%d %H:%M:%S"`
    echo "current date (now): $current_date"

    echo "logging current date to date log file: $date_log_file_path"
    echo $current_date >> $date_log_file_path
else
    current_date=$getopts_current_date
    echo "current date (command line): $current_date"
fi


# compile command

## files

opt_jar_file_path=$jar_file_path
opt_config_file_path=$config_file_path

opt_lock_file_path=$work_dir_path/lock
opt_log_file_path=$work_dir_path/logs/`echo $current_date | sed "s/ /_/g" | sed "s/:/-/g"`.log

## spring profiles
opt_spring_profiles_active="-Dspring.profiles.active=$spring_profiles_active"

## dates
opt_min_timestamp="-DminTimestamp=$last_date"
opt_max_timestamp="-DmaxTimestamp=$current_date"

## workdir
opt_work_dir_path="-Dworkdir=file:$work_dir_path"

command='
nice -19 
flock --timeout=16 '$opt_lock_file_path' 
java 
-Xmx16g 
-XX:-UseGCOverheadLimit 
'$opt_spring_profiles_active' 
'$opt_min_timestamp' 
'$opt_max_timestamp' 
'$opt_work_dir_path' 
-jar '$opt_jar_file_path' "file:'$opt_config_file_path'"
> '$opt_log_file' 2>&1'

# run command

echo "COMMAND:"
echo $command
if [ "$execute" = "TRUE" ]; then
    eval $command
fi
