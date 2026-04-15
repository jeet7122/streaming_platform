# 🎬 Streaming Platform

A scalable, microservices-based video streaming platform inspired by YouTube and Netflix.

This project demonstrates how modern streaming systems work internally - from video upload to distributed processing and adaptive streaming using HLS.

---

## 🚀 Overview

This platform allows users to upload videos, which are then asynchronously processed into streamable formats and served efficiently using chunk-based streaming.

The system is designed with scalability, fault tolerance, and real-world architecture patterns in mind.

---

## 🧠 Key Highlights

- Microservices architecture using Spring Boot
- Event-driven processing using Kafka
- Cloud storage integration using Cloudflare R2 (S3-compatible)
- Video transcoding using FFmpeg
- HLS (HTTP Live Streaming) generation for adaptive playback
- Scalable and decoupled system design

---

## 🏗️ Architecture

The platform is built using a microservices architecture where each service is responsible for a specific domain. Services communicate via REST APIs and asynchronous Kafka events.

---

### 🔧 Services

- **API Gateway**
    - Single entry point for all client requests
    - Handles routing and authentication headers

- **User Service**
    - Manages authentication and user data
    - Issues JWT tokens

- **Video Service**
    - Stores video metadata
    - Publishes events when a video is uploaded
    - Tracks processing status and manifest URL

- **Upload Service**
    - Handles video uploads
    - Stores raw videos in Cloudflare R2

- **Processing Service**
    - Consumes Kafka events
    - Downloads raw video from R2
    - Transcodes video using FFmpeg
    - Generates HLS (m3u8 + .ts segments)
    - Uploads processed files back to R2
    - Updates video status
    - Handles Garbage Collection

- **Like Service**
    - Handles like/unlike functionality
    - Tracks like counts

- **Comment Service**
    - Manages video comments

- **Event Library**
    - Shared module for Kafka event schemas

---

## Video Processing Workflow

```mermaid
flowchart TD
    A[Client Upload Request] --> B[API Gateway]

    B --> C[Upload Service]
    C -->|Pre-signed URL| D[Cloud Storage (R2/S3)]

    D --> E[Video Service]
    E -->|Save Metadata| F[(Database)]
    E -->|Emit Event: video.uploaded| G[Kafka]

    G --> H[Processing Service (Consumer)]
    H --> I[Download Video from R2]

    I --> J[FFmpeg Processing]
    J -->|Generate HLS (m3u8 + segments)| K[Upload to R2]

    K --> L[Video Service]
    L -->|Update Status: READY + Manifest URL| F

    L --> M[Client Streaming Request]
    M --> N[HLS Playback]

---

## ⚙️ Tech Stack

### 🖥️ Backend
- Java 21
- Spring Boot
- Spring Security (JWT Authentication)
- Spring Data JPA

### 🗄️ Database
- PostgreSQL

### ⚡ Messaging
- Apache Kafka

### ☁️ Storage
- Cloudflare R2 (S3-compatible object storage)

### 🎥 Video Processing
- FFmpeg (HLS transcoding)

### 🌐 API & Gateway
- Spring Cloud Gateway

### 🐳 Dev & Deployment
- Docker (Kafka setup)
- Maven (multi-module project)

---

## 📦 Features

### 🎥 Streaming & Playback
- Master playlist (`master.m3u8`) for adaptive bitrate streaming
- Frontend video player using HLS.js
- Video buffering optimization

---

### 🔐 Authentication
- User registration and login
- JWT-based authentication
- Secure API access via API Gateway

---

### 🎬 Video Management
- Upload videos to cloud storage (R2)
- Store video metadata in database
- Track video processing status

---

### ⚙️ Asynchronous Processing
- Event-driven architecture using Kafka
- Decoupled upload and processing pipeline
- Fault-tolerant video processing workflow

---


### 🔐 Security & Reliability
- Retry mechanisms for failed processing jobs
- Dead-letter queue (Kafka) for failed events

---

### 🎥 Video Processing (HLS)
- Transcoding using FFmpeg
- HLS (HTTP Live Streaming) generation
- Segment-based streaming (`.m3u8` + `.ts` files)

---

### ❤️ Engagement System
- Like / Unlike videos
- Like count tracking
- Comment system for videos

---

### ☁️ Cloud Storage
- Raw video storage in Cloudflare R2
- Processed video segments stored in R2
- Structured storage using object keys

---

### 🚀 Scalable Architecture
- Microservices-based design
- Independent service deployment
- Easy horizontal scaling

---

## 🚧 Roadmap

The following features and improvements are planned to evolve the platform further:

---

### 🖼️ Media Enhancements
- [ ] Thumbnail generation using FFmpeg
- [ ] Preview clips (short snippets)
- [ ] Video duration extraction

---

### ⚡ Performance & Scalability
- [ ] Redis caching for frequently accessed data
- [ ] CDN integration for faster video delivery
- [ ] Parallel video processing (multi-worker support)
- [ ] Batch processing optimizations

---

### 🔐 Security & Reliability
- [ ] Rate limiting at API Gateway

---

### 📊 Observability
- [ ] Logging improvements (structured logs)
- [ ] Metrics & monitoring (Prometheus/Grafana)
- [ ] Processing status tracking

---

### 🌐 Platform Features
- [ ] Video search functionality
- [ ] Recommendation system (basic)
- [ ] Subscription / follow system
- [ ] Share feature (v2)

---

### 🐳 DevOps & Deployment
- [ ] Full Docker Compose setup
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Kubernetes deployment

---

## 📄 License

This project is licensed under the MIT License.

You are free to use, modify, and distribute this software with proper attribution.

---

## ⭐ Support

If you find this project useful:

- ⭐ Star the repository
- 🍴 Fork and contribute
- 🧠 Share ideas and improvements

---

## 📬 Contact

For discussions, suggestions, or collaboration:

- Open an issue
- Start a discussion

---

## 🚀 Final Note

This project is a continuous effort to explore and implement real-world distributed system design for video streaming platforms.

Contributions, feedback, and ideas are always welcome!

---
