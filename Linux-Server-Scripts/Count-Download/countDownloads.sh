#!/bin/bash
#Written by Seifeldin Ismail - Group 1 - NightOwls

#Starttime for the script
startTime=$(date +%s)

#file path of html file
htmlLocation=/home/${USER}/download.html

#Date: sed deletes the 0 as specified on Infra Document
dateFunction=$(date +%d\ %b  | sed 's%/0%/%g' | tr '[:upper:]' '[:lower:]')

#Format of the "last updated on task" detail on infra document
lastUpdatedDate=$(date +%d\ %b\ %H:%M | tr '[:upper:]' '[:lower:]')


#Name of zip-file to look for in logs
zipName=Welcomescreen.png

apacheLog=/var/log/apache2/access.log


#grep -c stands for --count: counts lines matching string
amountOfDownloads=$(grep -Ec "$(date +%d/%b/%Y)(.)+($zipName)" ${apacheLog})


if [ ! -e "${htmlLocation}" ]
then
  echo "creating html file"
  touch "${htmlLocation}"
  echo "created html file"
  chmod 664 "${htmlLocation}"

  echo -e "<!DOCTYPE html> <html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Download Statistics</title>\n</head>\n<body>\n" >> "${htmlLocation}"
else
if [[ $(cat "${htmlLocation}" | grep "${dateFunction}") == "${dateFunction}" ]]
then
  echo "pattern matches, we should delete that line and make a new one"
  sed -i "/$(date +%d\ %b  | sed 's%/0%/%g' | tr '[:upper:]' '[:lower:]')/d" "${htmlLocation}"
fi
  sed -i '/Total/d' "${htmlLocation}"
  sed -i '/This script took/d' "${htmlLocation}"
  sed -i '/Last updated on/d' "${htmlLocation}"
fi


# shellcheck disable=SC2129
echo "<p>${dateFunction} - ${amountOfDownloads} downloads</p>" >> "${htmlLocation}"
# shellcheck disable=SC2002
totalDownload=$(cat "${htmlLocation}" | grep "<p>" | grep -E "[^0-9][0-9]+" | awk '{ SUM += $4;} END { print SUM;}')
echo -e "<p>Total: ${totalDownload} downloads.</p>" >> "${htmlLocation}"

endTime=$(date +%s)
runTime=$((endTime-startTime))

echo -e "<p>This script took ${runTime} seconds to run</p>" >> "${htmlLocation}"
echo -e "<p>Last updated on ${lastUpdatedDate}</p></body></html>" >> "${htmlLocation}"
