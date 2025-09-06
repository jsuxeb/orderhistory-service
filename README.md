#API-ORDER-SERVICE
#Docker Build
docker build -f Dockerfile -t galaxytraining/orderhistory-service:1.0.0 .

# DOCKER RUN EN WINDOWS (POWERSHELL)

docker run -d `
  --name orderhistory-service `
-p 8084:8008 `
  -e CONFIG_SCHEMA_REGISTRY_URL=http://192.168.1.48:8081 `
-e CONFIG_KAFKA_BROKERS=PLAINTEXT://192.168.1.48:19092 `
galaxytraining/orderhistory-service:1.0.0
