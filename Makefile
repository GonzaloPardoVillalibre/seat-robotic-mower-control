# LINUX Makefile
PROJECT_NAME := seat-robotic-mower-control

define help

Usage: make <command>

Commands:
  help:                   Show this help information
  build:                  Build the server
  test:                   Run the tests
  run:                    Run the server
  create-test-workflow:   Create a test workflow providing the input file: f=inputFilePath

endef
export help

help:
	@echo "$$help"

build:
	@echo "Building Mowers App"
	mvn clean package -DskipTests=true

test:
	@echo "Running Tests for Mowers App"
	mvn clean test

run:
	make build
	@echo "Running Mowers App with input file"
	cd target && java -jar seat-robotic-mower-control-0.0.1-SNAPSHOT.jar

create-test-workflow:
	curl -X POST -F file=@"$(f)" http://localhost:8080/api/workflow

