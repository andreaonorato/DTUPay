#!/bin/bash
set -e
mvn clean package
docker-compose build payment-mgmt-service
