SCRIPT_FOLDER=.

# parse options
bash $SCRIPT_FOLDER/deploy-run.bash "$@"
bash $SCRIPT_FOLDER/deploy-copy.bash "$@"

