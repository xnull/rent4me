#!/bin/sh

#set up it in /etc/crontab
# add following line
# m h dom mon dow user  command
#39 * * * *   root    /home/dionis/cron_backup_postgre.sh

DATE=$(date +"%Y%m%d%H%M%S")

echo "Backup: $DATE"
DB='realty_proddb'
echo "Dumping db: ${DB}"

FULL_PATH_TO_BACKUP_DIR="/home/dionis/backups"

BACKUP_FILE="${FULL_PATH_TO_BACKUP_DIR}/${DB}_${DATE}.backup"

echo 'Who Am I?'

whoami



echo 'Authenticating as postgres'

CMD="pg_dump -i -U postgres -F c -b -v -f '${BACKUP_FILE}' '${DB}'"

echo "Will execute following command as postgres user: [${CMD}]"

su postgres -c "${CMD}"

PATH_TO_DROPBOX_BACKUP_UTILITY="/home/dionis/realty-db-tool-1.0.jar"

echo 'Starting backup upload to Dropbox'

java -jar "${PATH_TO_DROPBOX_BACKUP_UTILITY}" backup "${BACKUP_FILE}" rent4_me_prod

echo 'Ending backup upload to Dropbox'