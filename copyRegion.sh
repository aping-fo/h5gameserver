#!/bin/bash
if [ $# -eq 0 ]
then
    echo "sh copyRegion.sh 61 | 8"
    exit 0
fi


function ReadMe()
{
		
    
}

serverId=$1
case ${serverId} in
	1)
	  sed -i "s/192.168.7.200:3306\/game_new/192.168.7.200:3306\/game_new/g" ./config/application.xml
	  sed -i "s/127.0.0.1:3306\/game2_log/127.0.0.1:3306\/game_new_log/g" ./config/application.xml
	  sed -i "s/port=10010/port=10011/g" ./config/sys.properties
	  sed -i "s/serverId=10/serverId=19/g" ./config/sys.properties
	  sed -i "s/gmport=20010/gmport=20011/g" ./config/sys.properties
	;;

	61)
          sed -i "s/192.168.7.200:3306\/game_new/127.0.0.1:3306\/game_61/g" ./config/application.xml
          sed -i "s/127.0.0.1:3306\/game2_log/127.0.0.1:3306\/game_61_log/g" ./config/application.xml
          sed -i "s/port=10010/port=10011/g" ./config/sys.properties
          sed -i "s/serverId=10/serverId=19/g" ./config/sys.properties
          sed -i "s/gmport=20010/gmport=20011/g" ./config/sys.properties
	;;

	*)
	  echo ""
	  exit
	  ;;
esac
