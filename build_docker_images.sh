#!/bin/bash

# Array of directories
services=("auth" "cart-orders" "eurekaserver" "gateway" "product" "review")

# Loop through each directory and build the Docker image
for service in "${services[@]}"; do
  echo "Building Docker image for $service..."
  cd $service || { echo "Directory $service not found"; exit 1; }
  docker build -t ${service}-service .
  if [ $? -ne 0 ]; then
    echo "Failed to build Docker image for $service"
    exit 1
  fi
  cd ..
done

echo "All Docker images built successfully."
