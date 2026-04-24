# Pravaha: Event-Driven Microservices with Spring Boot & Kafka

Pravaha (Sanskrit for *Flow* or *Stream*) is a production-ready, event-driven microservices architecture built with Spring Boot 3 and Apache Kafka. It demonstrates highly reliable message production and scalable, concurrent message consumption.

## 🏗 Architecture Overview

The system consists of two independently deployable microservices communicating asynchronously via Kafka:

1. **Pravaha Producer (`pravaha-producer`)**: A REST API that accepts incoming order requests, creates an `OrderEvent` payload, and publishes it to Kafka. Designed for zero message loss using idempotency and `acks=all`.
2. **Pravaha Consumer (`pravaha-consumer`)**: A backend processor that listens to the `order-events` topic. It leverages Kafka consumer groups and partition concurrency to process messages in parallel across multiple threads.

### Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 3.2.x, Spring Kafka
* **Message Broker:** Apache Kafka
* **Containerization:** Docker, Docker Compose (Multi-stage builds)

---

## 📂 Project Structure

```text
pravaha/
├── docker-compose.yml        # Orchestration for the microservices (and optional local Kafka)
├── pravaha-producer/         # Spring Boot Producer API
│   ├── Dockerfile            # Multi-stage Docker build
│   ├── pom.xml
│   └── src/                  
└── pravaha-consumer/         # Spring Boot Consumer Worker
    ├── Dockerfile            # Multi-stage Docker build
    ├── pom.xml
    └── src/                  
```

---

## 🚀 Getting Started

### Prerequisites
* [Docker](https://docs.docker.com/get-docker/) & Docker Compose
* Java 21 & Maven (Optional, if you wish to run/build natively outside of Docker)

### Network Configuration
This project uses env variable, please update the `KAFKA_BOOTSTRAP_SERVERS` environment variables in the `docker-compose.yml` to point to your specific broker IPs.

### Deployment

To build the images and start the distributed system, run the following from the root directory:

```bash
# Compilation
cd pravaha-producer
mvn clean package -DskipTests
cd pravaha-consumer
mvn clean package -DskipTests

# Build and deploy the containers in detached mode
docker-compose up -d --build
```

To verify the containers are running healthy:
```bash
docker ps
```

---

## 🧪 Testing the System

### 1. Create an Order
Use `curl` (or Postman) to trigger the Producer's REST endpoint. This simulates a user placing an order.

```bash
curl -s -X POST "http://localhost:8060/api/orders?amount=250.50"
```

**Expected Response:**
> `Order event published successfully`

### 2. Observe the Producer Logs
Check the producer to verify it successfully established a connection with the Kafka cluster and routed the message.

```bash
docker logs -f pravaha-producer
```
*You should see:*
> `Produced event: OrderEvent[orderId=b0fc1086-9ec8-4aff-83d1-57b92c4482d9, status=CREATED, amount=250.5]`

### 3. Observe the Consumer Logs
Check the consumer to watch the parallel processing in action.

```bash
docker logs -f pravaha-consumer
```
*You should see partition assignment and consumption by a specific thread:*
> `order-processing-group: partitions assigned: [order-events-0, order-events-1, order-events-2]`  
> `Consumed event via thread [org.springframework.kafka.KafkaListenerEndpointContainer#0-2-C-1]: OrderEvent[orderId=b0fc1086-9ec8-4aff-83d1-57b92c4482d9, status=CREATED, amount=250.5]`

---

## 🧠 Core Design Principles

### Producer Reliability
* **`acks: all`**: Guarantees the broker has fully committed the message across all replicas before acknowledging success to the producer.
* **Idempotency (`enable.idempotence: true`)**: Prevents duplicate messages from being written to Kafka in the event of network retries.
* **Key-Based Routing**: The `orderId` is used as the Kafka message key. This guarantees that all subsequent events for the same order (e.g., `SHIPPED`, `DELIVERED`) are routed to the exact same partition, ensuring strict chronological processing order.

### Consumer Scalability
* **Thread Concurrency**: The consumer is configured with `concurrency: 3`. Spring Boot automatically spawns three separate listener threads mapped to the three partitions of the `order-events` topic, multiplying throughput capabilities.
* **Lightweight Runtimes**: Dockerfiles utilize a multi-stage approach. They compile the code using a full JDK but package the final application inside a minimal Alpine JRE image, drastically reducing memory footprint and deployment size.

---

## 🧹 Cleanup

To stop the microservices and remove the containers:
```bash
docker-compose down
```
