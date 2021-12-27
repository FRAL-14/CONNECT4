This is the linux backup script.

Steps on how to use this script are very simple.

1. Open the file 'filesBackup.txt' located in /opt/scripts/backup-script/backup
2. Type in all directories you want to be backed up (1 directory/file per line)
3. Very important to check your spelling,
the script will run even if you spell something wrong
(because it will entirely delete that line).
The script will inform you that the line has been deleted in the log

The script is scheduled to run every day at 1am.

You should find all backups in the folder located in /media/backup.

Also note that a weekly script is run to remove one-week-old backups.

You can find the status of the script and if any errors occurred at /var/opt/backup-log/error.log
