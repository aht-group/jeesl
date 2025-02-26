#!/bin/bash

rm -f /var/www/html/helpdesk/.DS_Store

if [ ! "$(ls -A /var/www/html/helpdesk)" ]; then
    echo "Initialize OST directory"
   	
   	unzip /tmp/osTicket.zip "upload/*" -d /var/www/html/helpdesk
   	mv /var/www/html/helpdesk/upload/* /var/www/html/helpdesk
   	rm -rf /var/www/html/helpdesk/upload
   	
   	echo "OST PHP/HTML directory created"
fi


exec "apache2-foreground"