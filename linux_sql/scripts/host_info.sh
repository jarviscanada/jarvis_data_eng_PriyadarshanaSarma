#! /bin/bash

db_host=$1
db_port=$2
db_name=$3
db_user=$4
db_password=$5

#Check number of arguments
if [ $# -ne  5 ]; then
  echo "Requires the database's host, port, name, username and password"
  exit 1
fi

#save hostname to a variable
hostname=$(hostname -f)

#save memory information to a variable
meminfo=`cat /proc/meminfo`

#save CPU information to a variable
lscpu_out=`lscpu`

#getting the hardware specifications
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model\sname:" | awk '{print $3, $4, $5, $6, $7}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU\sMHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "^L2\scache:" | awk '{print $3}' | xargs)
total_mem=$(echo "$meminfo"  | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(echo "$(date '+%Y-%m-%d %H:%M:%S')")

#creating insert statement
host_info_insert_query="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, \"timestamp\")
VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, ${l2_cache::-1}, $total_mem, '$timestamp');"

export PGPASSWORD=$db_password

#Insert hardware specifications data into the database
psql -h $db_host -p $db_port -d $db_name -U $db_user -c "$host_info_insert_query"
exit $?