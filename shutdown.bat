@echo off
echo ====================================================
echo Stopping and Removing Containers...
echo ====================================================
docker stop restful-container db-container
docker rm restful-container db-container

echo ====================================================
echo Removing Docker Network...
echo ====================================================
docker network rm app-network

echo ====================================================
echo Cleanup finished successfully!
echo ====================================================
pause