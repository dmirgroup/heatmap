# settings

# where to read tiles
DATA_FOLDER=./out/tiles

# where to put archive
ARCHIVE_FOLDER=./out/archives
ARCHIVE_FILE_NAME=tiles

# where to copy archive
REMOTE_BASE_FOLDER=/tmp/tiles
REMOTE_USER=becker
REMOTE_HOST=localhost

# code

if [ ! -d "$DATA_FOLDER" ]; then
	echo "Folder does not exist: $DATA_FOLDER"
	exit
fi

if [ ! -d "$ARCHIVE_FOLDER" ]; then
	mkdir $ARCHIVE_FOLDER
fi

ARCHIVE_FILE=$ARCHIVE_FOLDER/$ARCHIVE_FILE_NAME.tar.gz

cd $DATA_FOLDER
tar -zcf $ARCHIVE_FILE *

# copy to remote and extract

scp $OUT_FILE $REMOTE_USER@$REMOTE_HOST:$REMOTE_FOLDER
ssh $REMOTE_USER@$REMOTE_HOST "cd $REMOTE_FOLDER; tar -xf $OUT_FILE_NAME.tar.gz"
