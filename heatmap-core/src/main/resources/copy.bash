# settings

# where to read tiles
TILE_FOLDER=/home/everyaware/heatmap-processor/workspace/basic/out/tiles

# where to put archive
OUT_FOLDER=/home/everyaware/heatmap-processor/workspace/basic/out
OUT_FILE_NAME=tiles

# where to copy archive
REMOTE_FOLDER=/var/www/tiles/airprobe
REMOTE_USER=mysql
REMOTE_HOST=cs.everyaware.eu

# code

OUT_FILE=$OUT_FOLDER/$OUT_FILE_NAME.tar.gz

cd $TILE_FOLDER
tar -zcf $OUT_FILE *

# copy to remote and extract

scp $OUT_FILE $REMOTE_USER@$REMOTE_HOST:$REMOTE_FOLDER
ssh $REMOTE_USER@$REMOTE_HOST "cd $REMOTE_FOLDER; tar -xf $OUT_FILE_NAME.tar.gz"
