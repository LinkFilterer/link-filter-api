service_name=springboot_app
nginx_container_name=nginx

reload_nginx() {
  docker exec $nginx_container_name /usr/sbin/nginx -s reload
}

update_server() {

  old_container_id=$(docker ps -f name=$service_name -q | tail -n1)

  # create a new instance of the server
  docker-compose up --build -d --no-deps --scale $service_name=2 --no-recreate $service_name
  new_container_id=$(docker ps -f name=$service_name -q | head -n1)

  if [ -z $new_container_id ]; then
    echo "ID NOT FOUND, QUIT !"
    exit
  fi
  new_container_port=$(docker port $new_container_id | cut -d " " -f3 | cut -d ":" -f2)

  if [ -z $new_container_port ]; then
    echo "PORT NOT FOUND, QUIT !"
    exit
  fi

  # ---- server is up ---

  # reload nginx, so it can recognize the new instance
  reload_nginx

  # remove old instance
  docker stop $old_container_id
  docker rm $old_container_id -f

  docker-compose up -d --no-deps --scale $service_name=1 --no-recreate $service_name

  # reload ngnix, so it stops routing requests to the old instance
  reload_nginx

  # remove caches
  docker images | grep none | awk '{ print $3; }' | xargs sudo docker rmi --force

  echo "DONE !"
}

# call func
update_server
