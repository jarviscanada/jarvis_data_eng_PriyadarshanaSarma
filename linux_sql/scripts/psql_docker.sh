#! /bin/sh

#capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

#if the status of the docker command fails, then it will start the docker
sudo systemctl status docker || systemctl start docker

#check container status
docker container inspect jrvs-psql
container_status=$?

#User switch case to handle create|stop|start options
case $cmd in
  create)

  #check if the container is already created
  if [ $container_status -eq 0 ]; then
    echo 'Container already exists'
		exit 1
  fi

  #check if number of CLI arguments is equal to 3 or not
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  #create container
  docker volume create pgdata
	docker run --name jrvs-psql -e POSTGRES_PASSWORD=db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
	exit $?
	;;

  start|stop)
  #check instance status; exit 1 if container has not been created
  if [ $container_status -ne 0 ]; then
    echo 'Container has not been created'
    exit 1
  fi

  #if the docker is already started/stopped and if the user tries to start/stop it again, then it will exit 1 with an error message
  if [ $cmd == "start" ]; then
    if [ "$( docker container inspect -f '{{.State.Status}}' jrvs-psql )" == "running" ]; then
      echo 'Docker is already running'
      exit 1
    fi
    elif [ $cmd == "stop" ]; then
       if [ "$( docker container inspect -f '{{.State.Status}}' jrvs-psql )" != "running" ]; then
             echo 'Docker is already stopped'
             exit 1
       fi
  fi

  #start or stop the container
  docker container $cmd jrvs-psql
	exit $?
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac

