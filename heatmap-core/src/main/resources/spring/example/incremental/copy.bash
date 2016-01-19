# settings

# where to read tiles
DATA_FOLDER=/home/eva-services/env/services/heatmap-processor-v3/workspace/seed/tiles

# where to copy archive
REMOTE_FOLDER=/home/eva-webapp/env/webapp/everyaware/files/private
REMOTE_FOLDER_OVERALL=/home/eva-webapp/env/webapp/everyaware/files/public
REMOTE_USER=eva-webapp
REMOTE_HOST=melkor

# code

if [ ! -d "$DATA_FOLDER" ]; then
        echo "Data folder does not exist: $DATA_FOLDER"
        exit
fi

echo "rsync --------------------------------------------------------"
echo "rsync OVERALL: $(date +"%Y-%m-%d %T")"
rsync -az --progress -e ssh $DATA_FOLDER/OVERALL/ $REMOTE_USER@$REMOTE_HOST:$REMOTE_FOLDER_OVERALL

echo "rsync private: $(date +"%Y-%m-%d %T")"
rsync -az --progress -e ssh $DATA_FOLDER/ $REMOTE_USER@$REMOTE_HOST:$REMOTE_FOLDER

echo "rync done: $(date +"%Y-%m-%d %T")"