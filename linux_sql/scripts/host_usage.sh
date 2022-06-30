#!/bin/bash

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

#save vmstat information to a variable
vmstat_out_t=`vmstat -t`
vmstat_out_m=`vmstat --unit M`
vmstat_out_d=`vmstat -d`

#disk availability information
disk_free=`df -BM /`

#getting the linux resource usage data
timestamp=$(echo "$vmstat_out_t" | tail -n 1 | awk '{print $18, $19}' | xargs)
host_id="(SELECT MAX(id) FROM host_info WHERE hostname='$hostname')"
memory_free=$(echo "$vmstat_out_m" | tail -n 1 | awk '{print $4}' | xargs)
cpu_idle=$(echo "$vmstat_out_m" | tail -n 1 | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_out_m" | tail -n 1 | awk '{print $14}' | xargs)
disk_io=$(echo "$vmstat_out_d" | tail -n 1 | awk '{print $10}' | xargs)
disk_available=$(echo "$disk_free" | tail -n 1 | awk '{print $4}' | xargs)

#creating insert statement
host_usage_insert_query="INSERT INTO host_usage (\"timestamp\", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES ('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, ${disk_available::-1});"

export PGPASSWORD=$db_password

#Insert resource usage data into the database
psql -h $db_host -p $db_port -d $db_name -U $db_user -c "$host_usage_insert_query"
exit $?