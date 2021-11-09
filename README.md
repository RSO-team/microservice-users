# RSO: Microservice Users

Microservice which manages users in our service

## Prerequisites

```bash
docker run -d --name pg-users -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=events -p 5432:5432 postgres:13
```