# Seat Robotic Mower Control

Centralized application for controlling new mowers at the SEAT Martorell Factory.
- [Architecture](#architecture)
    - [ADR 1. Design assumptions](<doc/ADR-1-Design-assumptions.md>)
    - [ADR 2. CLI vs API rest](<doc/ADR-2-CLI-vs-REST-API.md>)
    - [ADR 3. Design patterns](<doc/ADR-3-Design-patterns.md>)
- [Run the service locally](#run-the-service-locally)
    - [Pre-requisites](#pre-requisites)
    - [Run the mower control service in the host machine](#run-the-mower-control-service-in-the-host-machine)
    - [Run the mower control service inside docker container](#run-the-mower-control-service-inside-docker-container)
    - [Test the workflow API](#test-the-workflow-api)
- [Future Improvements](#future-improvements)
- [Contact](#contact)

## Architecture
![Hexagonal-Design.drawio.png](doc/images/Hexagonal-Design.drawio.png)

This is the list of the architecture design records:
- [ARD 1. Design assumptions](<doc/ADR-1-Design-assumptions.md>)
- [ARD 2. CLI vs API rest](<doc/ADR-2-CLI-vs-REST-API.md>)
- [ARD 3. Design patterns](<doc/ADR-3-Design-patterns.md>)

## Run the service locally
### Pre-requisites
- [Make](https://www.gnu.org/software/make/)
- [Maven](https://maven.apache.org/)
- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Curl](https://curl.se/download.html)
- [Docker](https://docs.docker.com/engine/install)

The `make help` utility is available to the developer and provides a list of useful commands.

### Run the mower control service in the host machine
First, build the seat-robotic-mower-control-0.0.1-SNAPSHOT.jar:

```bash
make build
```

You can also execute unit and e2e tests by:

```bash
make test
```

Then, run the service (it builds up again anyways):

```bash
make run
```

### Run the mower control service inside docker container
Run the server inside a docker container.
```bash
make container-run
```
To stop the server you can run:
```bash
make container-terminate
```

### Test the workflow API
When the service is running you should be able to access the workflow API http://localhost:8080/api/workflow. 

Since it is a **public** API you should be able to test it using curl:
```bash
curl -X POST -F file=@"./src/test/resources/test-assessment-workflow.txt" http://localhost:8080/api/workflow
```

You can also use the `make` utility to automatically execute a curl operation given a desired input file path:
```bash
make create-test-workflow f=./src/test/resources/test-assessment-workflow.txt
```

Finally, and maybe the easiest, you can always use the **Swagger API** at http://localhost:8080/swagger-ui/index.html#/mowing-workflow-controller/createWorkflowFromFile

PS: At **src/test/resources** you can find a useful collection of input file examples!

## Future improvements
- Include CI/CD workflows.
- Add BDD (behavioral design driven) testing framework to fil the gap between product and tech vision.

## Contact
You can contact the creator via e-mail at: `gonzalopmb@gmail.com`