# Session Service - Quarkus Project

## Abstract

The **Session Service** is a Quarkus-based backend application that facilitates session management
for the Food2Gether platform. It provides RESTful APIs to handle sessions and orders efficiently
using Hibernate ORM, Panache repositories, and other Quarkus features.

## How to Develop

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker (optional, for containerized development or for running quarkus in dev mode)
- Kubernetes (optional, for local cluster testing)

### Steps to Run Locally
Run the application in development mode:
```sh
./mvnw quarkus:dev
```

Access the DevUI at http://localhost:8080/q/dev/ \
The API will be available at http://localhost:8080/api/v1/sessions

## How to Deploy

### Docker Deployment JVM

1. Build the Quarkus application:
   ```sh
   ./mvnw package
   ```
2. Build and tag the Docker image:
   ```sh
   docker build -f src/main/docker/Dockerfile.jvm -t ghcr.io/food2gether/session-service:jvm .
   ```
3. Run the container:
   ```sh
   docker run -i --rm -p 8080:8080 ghcr.io/food2gether/session-service:jvm
   ```

### Docker Deployment Native

1. Build the Quarkus application:
   ```sh
   ./mvnw package -Pnative -Dquarkus.native.container-build=true
   ```
2. Build and tag the Docker image:
   ```sh
   docker build -f src/main/docker/Dockerfile -t ghcr.io/food2gether/session-service .
   ```
3. Run the container:
   ```sh
   docker run -i --rm -p 8080:8080 ghcr.io/food2gether/session-service
   ```


### Kubernetes Deployment

1. Ensure you have `kubectl` and `kustomize` installed.
2. Deploy to Kubernetes:
   ```sh
   kubectl apply -k k8s/deploy
   ```
3. Verify the deployment:
   ```sh
   kubectl get pods -n food2gether
   ```
4. Expose the service:
   ```sh
   kubectl port-forward service/session-service 8080:80 -n food2gether
   ```
5. Access the API at http://localhost:8080/api/v1/sessions

For more details, refer to the [Quarkus documentation](https://quarkus.io).

