# Linux Cluster Monitoring Agent

## Introduction
This project will help to record the hardware specification and monitor the resource usages in a node. The data will be stored in a centralized PostgreSQL
database for monitoring and analysis by the the Linux Cluster Administration team. The technologies used in this project were docker, git, PostgreSQL database, bash scripts, Linux[CentOS 7], Google Cloud Platform.

## Quick Start
```
- Start a psql instance using psql_docker.sh
  ./scripts/psql_docker.sh start
  
- Create tables using ddl.sql
  psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
  
- Insert hardware specs data into the DB using host_info.sh
  ./scripts/host_info.sh localhost 5432 host_agent db_username db_password
  
- Insert hardware usage data into the DB using host_usage.sh
   ./scripts/host_usage.sh localhost 5432 host_agent db_username db_password
   
- Crontab setup
  * * * * * bash /home/centos/dev/jarvis_data_eng_PriyadarshanaSarma/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

## Implemenation
The bash script psql_docker.sh create and start a docker container with PostgreSQL database running in it. Then, the ddl.sql script is executed on the host_agent database against the psql instance to create the two tables - host_info and host_usage. Hereafter, a bash script, host_info.sh script will be executed to collect the hardware details of the machine, where it is run. This script will be run everytime when the host starts up. Another bash script, host_usage.sh script will be executed to collect the current host usage (CPU and Memory) of the machine. This script will be executed by crontab every minute. 

## Architecture
![Host usage information architecture](./assets/my_image.jpg)

## Scripts
- psql_docker.sh
  Create, start or stop a docker container that runs PostgreSQL database-host_agent.
  Usage
  ```
  ## create a psql docker container with the given username and password.
  ./scripts/psql_docker.sh create db_username db_password

  ## start the stopped psql docker container
  ./scripts/psql_docker.sh start

  ## stop the running psql docker container
  ./scripts/psql_docker.sh stop
  ```
- host_info.sh
  Collects the host hardware information (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp) of a server and insert it into     the PostgreSQL database.
  Usage
  ```
  ./scripts/host_info.sh localhost 5432 host_agent db_username db_password
  ```
- host_usage.sh
  Gathers the current host usage (timestamp, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) and then insert into the PostgreSQL database.
  Usage
  ```
  ./scripts/host_usage.sh localhost 5432 host_agent db_username db_password
  ```
- crontab
  It runs the host_usage.sh script every minute which collects the host usage information.
  Usage
  ```
  # Edit crontab jobs
  crontab -e
  # Add this to crontab
  * * * * * bash /home/centos/dev/jarvis_data_eng_PriyadarshanaSarma/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
  ```
- queries.sql
  There are three queries in this script. 
  * Group hosts by CPU number and sort by their memory size in descending order(within each cpu_number group).
  * Retrieves average used memory in percentage over 5 mins interval for each host.
  * Detect host failure if cron job inserts less than three data points within 5-min interval in the host_usage table.
  
 ## Database Modeling
- host_info

  |Column Name  | Description| Data Type | 
  | ------------- | ------------- | ------------- | 
  | id  | PRIMARY KEY  |  SERIAL | 
  | hostname   | Full name of the host machine  | VARCHAR   | 
  | cpu_number    | Number of CPU in the host machine  | INTEGER  | 
  | cpu_architecture    | Architecture of the CPU | VARCHAR  | 
  | cpu_model    | Model of the CPU  | VARCHAR  | 
  | cpu_mhz    | CPU speed  | REAL | 
  | L2_cache    | L2_cache size in kb | INTEGER  | 
  | total_mem    | Total memory in kb | INTEGER  |
  | timestamp   | Current time in UTC time zone  | TIMESTAMP  |
  
- host_usage
  
  |Column Name  | Description| Data Type | 
  | ------------- | ------------- | ------------- | 
  | timestamp  | Current time in UTC time zone  | TIMESTAMP  | 
  | host_id    | Unique id for each node(Foreign key) | SERIAL | 
  | memory_free     | Free memory of the CPU in MB  | INTEGER | 
  | cpu_idle     | Amount of CPU capacity that is not being used in percentage | INTEGER | 
  | cpu_kernel     | Provides information on the current running tasks in the CPU | INTEGER | 
  | disk_io     | Number of disk I/O  | INTEGER | 
  | disk_available     | Amount of disk space available in MB  | INTEGER |
  
  ## Test
  For the bash scripts, I have tested with various inputs like giving zero, invalid number of arguments, incorrect arguments like wrong username and password, etc. I     have also cross-verified if the specific data has been inserted into the tables. For the SQL queries, I have manually inserted data into the tables to check the       various results like detecting a host failure when the cronjob inserts less than three data-points in the host_usage table.
  
  ## Deployment
  The application was deployed using the Github, docker and crontab. Github is used to maintain the code versions, docker is used to run a PostgreSQL database and       crontab is used to run the host_usage.sh script every minute.
  
  ## Improvements
  - Since the crontab runs the host_usage.sh script in every minute, so the database table would be soon be filled with too many records. Therefore, it will be useful    to purge older data.
  - Host can gather more fine-grained data.
  - Having a script that automatically runs every minute and alert the system admins if the host has failed.

  

 





