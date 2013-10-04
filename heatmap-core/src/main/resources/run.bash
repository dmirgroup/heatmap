#!/bin/bash

# settings
DATE_LOG="date.log"
WORK_DIR="/home/everyaware/heatmap-processor"

# read dates

last_date=`tail -n 1 $DATE_LOG`
current_date=`date +"%Y-%m-%d_%H:%M:%S"`
echo "last date: $last_date"
echo "current date: $current_date"

# append current date to date log

echo $current_date >> $DATE_LOG

# compile command

command="
nice -19 
flock --timeout=16 $WORK_DIR/lock 
java 
-Xmx16g 
-XX:-UseGCOverheadLimit 
-Dspring.profiles.active=\"minmax\"
-Dmin=\"$last_date\" 
-Dmax=\"$current_date\" 
-jar heatmap-core-0.0.1-SNAPSHOT.jar 
> $WORK_DIR/logs/$current_date.log 2>&1"

# run command

bash $command