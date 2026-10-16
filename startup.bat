@echo off
echo ====================================================
echo 1. Creating Isolated Docker Network...
echo ====================================================
docker network create app-network

echo ====================================================
echo 2. Starting Database Container (MySQL)...
echo ====================================================
docker run -d ^
  --name db-container ^
  --network app-network ^
  -v dbdata:/var/lib/mysql ^
  -e MYSQL_ROOT_PASSWORD=rootpassword ^
  -e MYSQL_DATABASE=citizens_db ^
  -e MYSQL_USER=appuser ^
  -e MYSQL_PASSWORD=apppassword ^
  mysql:8.0

echo Waiting for database to initialize (15 seconds)...
timeout /t 15 /nobreak > nul

echo ====================================================
echo 3. Starting RESTful Service Container...
echo ====================================================
docker run -d ^
  --name restful-container ^
  --network app-network ^
  -p 8080:8080 ^
  -e DB_HOST=db-container ^
  -e DB_USER=appuser ^
  -e DB_PASSWORD=apppassword ^
  citizen-rest-service:latest

echo ====================================================
echo Containers started successfully!
echo REST API available at http://localhost:8080
echo ====================================================
pause