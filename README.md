# Message Plus

Message Plus is a simple service to **create, manage, and search message templates** for use in customer-facing applications.  
It makes it easy and convenient to manage recurring interactions — for example, sending similar or identical messages to different customers on platforms like AirBnB.

## Features

- ✍️ Create, update, and delete reusable message templates
- 🌍 Multi-language support (currently **English** and **Portuguese**)
- 🔍 Search and organize templates efficiently
- ⚡ Designed to simplify customer interactions in apps and services

## Getting Started

### Prerequisites
- Java 21+ (for local development without Docker)
- Gradle (or the included wrapper)
- PostgreSQL (or H2 for quick local testing)

### Run locally (no Docker)
```bash
./gradlew bootRun
```
By default the app starts on: http://localhost:8080

### Example endpoints

GET /api/templates → list all message templates

POST /api/templates → create a new template

GET /api/templates/search?lang=en&query=welcome → search by language and keyword
(in development)

### Docker setup
If you prefer a one-command setup using Docker and Docker Compose, see:
[Docker](/etc/docs/Docker.md)