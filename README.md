# spring-boot-public-and-private-chat
Using Stomp and socketjs
# Some docker cmd
## without network
### mysql
docker run --name mysqldb -e MYSQL_ROOT_PASSWORD=pass@123 -e MYSQL_DATABASE=test -p 3308:3306 -d mysql
### spring boot app
docker run -e MYSQL_HOST=Ipaddress -e MYSQL_PORT=no_need -e MYSQL_USER=root/whatever -e MYSQL_PASSWORD=your_password -d -p 9090:8080 springboot_docker_app


## with network
### create network
docker network create network_name_as_you_want
### mysql
docker run --name mysqldb -e MYSQL_ROOT_PASSWORD=pass@123 -e MYSQL_DATABASE=test --network name_of_network_you_created -d mysql
### spring boot app
docker run -e MYSQL_HOST=name_of_database/mysql_container -e MYSQL_PORT=no_need -e MYSQL_USER=root/whatever -e MYSQL_PASSWORD=your_password -d -p 9090:8080 --network name_of_network_you_created springboot_docker_app


# Run runner in background on ec2 aws instance
sudo ./svc.sh install
sudo ./svc.sh start
