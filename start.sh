#!/bin/bash
# Wait for MySQL
./wait-for-it.sh mysql:3306 --timeout=30 --strict -- echo "MySQL is up"

# Wait for Redis
./wait-for-it.sh redis:6379 --timeout=30 --strict -- echo "Redis is up"

# Start the Spring Boot app
java -jar app.jar