#!/bin/bash
ROOT_DIR=/home/eva-services/env/services/heatmap-processor-v3

(
	flock -n --timeout=16 9 || exit 1
	mkdir -p $ROOT_DIR/workspace/logs/`date +"%Y/%m/%d"`
	nice -n 19 java \
		-Xmx6g \
		-classpath $ROOT_DIR/bin/heatmap-core-0.1.3-SNAPSHOT.jar:$ROOT_DIR/bin/lib/* \
		-DconfigDir="$ROOT_DIR/config" \
		-DworkDir="$ROOT_DIR/workspace" \
		-DminTime="2013-11-04 00:00:00" \
		de.uniwue.dmir.heatmap.EntryPointIncremental 2>&1 \
		| gzip > $ROOT_DIR/workspace/logs/`date +"%Y/%m/%d/%Y-%m-%d_%H-%M-%S"`.log.gz
	bash $ROOT_DIR/bin/copy.bash
	
	# ... commands executed under lock ...
) 9>$ROOT_DIR/workspace/lock
