
usage() {
 echo "--------- Usage ----------------------"
 echo "Install a service: $(basename $0) <service run with username> [service name] "
 echo " or "
 echo "Uninstall service: $(basename $0) -u [service name]"
 echo "--------------------------------------"
}

service_name=""
user_name="jboss"
is_uninstall=0
if [ $# -eq 1 ]; then
 service_name=$1 
else 
  if [ $# -eq 2 ]; then
	 until [ $# -eq 0 ]
	 do
	  if [ "$1" == "-u" ]; then
		is_uninstall=1
	  else 
		service_name=$1
	  fi
	  shift
	 done
	 
	 if [ $is_uninstall -eq 0 ]; then 
	  echo "Invalid inputs!"
	  usage
	  exit 1;
	 fi
	else   
	  echo "Invalid inputs!"
	  usage
	  exit 1;
	fi 
fi 

if [ $# -eq 2 ]; then
  user_name="$1"
  service_name="$2" 
fi

service_file="/etc/init.d/$service_name"

uninstall() {
 echo $"Uninstalling $service_name .."
 if [ -f $service_file ]; then
   chkconfig --del $service_name>/dev/null 1>&2
  test -f $service_file && rm -f $service_file
  test -e systemctl && "Reload service by 'systemctl'..." && systemctl daemon-reload
 fi 
 echo $"OK"
}

if [ $is_uninstall -eq 1 ]; then
 uninstall
 exit 0;
fi


#install service 
touch $service_file
chmod 744 $service_file

#Write script content.
cat>$service_file<<'EOF'
#! /bin/bash
#chkconfig: 2345 99 01
EOF

service_dir=$(cd `dirname $0` && pwd)
cat>>$service_file<<EOF
#description: Docker compose - '$service_name'
SERVICE_INSTALL_DIR="$service_dir"
service_name="$service_name"
user_name="$user_name"
EOF


cat>>$service_file<<'EOF'
# Source function library. 
if [ -f "/etc/rc.d/init.d/functions" ]; then
	. /etc/rc.d/init.d/functions
fi 



RETVAL=0
# if [ ! -f $SERVICE_INSTALL_DIR/docker-compose.yml ]; then 
#   echo "'docker-compose.yml' is not exist!"
#   exit 6
# fi
 
##
usage ()
{
	echo $"Usage: $0 {start|stop|status|restart|condrestart}" 1>&2
	RETVAL=2
}
status ()
{ 
    echo $"Status of service '$service_name'..." 1>&2
    su - $user_name -c "cd $SERVICE_INSTALL_DIR && ./taillog.sh" 
	cd ->/dev/null 2>&1
}
start ()
{  

    echo $"Starting service '$service_name'..." 1>&2 
	su - $user_name -c "cd $SERVICE_INSTALL_DIR && ./start.sh" 
	cd ->/dev/null 2>&1
}
stop ()
{
    echo $"Stoping service '$service_name'" 1>&2
	su - $user_name -c "cd $SERVICE_INSTALL_DIR && ./stop.sh"
	cd ->/dev/null 1>&2
} 
restart ()
{
	stop
	start
}

##
case "$1" in
    stop) stop ;;
    status) status ;;
    start|restart|reload|force-reload) restart ;;
    condrestart) condrestart ;;
    *) usage ;;
esac
EOF

#Load this service globally.
chkconfig --add $service_name
test -e systemctl && "Reload service by 'systemctl'..." && systemctl daemon-reload
echo "Installed service '$service_name' OK!"