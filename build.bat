@echo off
echo ====================================================
echo 1. Building Docker Image for RESTful Service...
echo ====================================================
docker build -t citizen-rest-service:latest .

echo ====================================================
echo 2. Creating Persistent Docker Volume for Database...
echo ====================================================
docker volume create dbdata

echo ====================================================
echo Build process completed successfully!
echo ====================================================
pause