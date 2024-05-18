# microservices

workflow

mvn clean package -DskipTests
./build_docker_images.sh
docker-compose down
docker-compose up -d
