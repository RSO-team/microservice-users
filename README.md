# RSO: Microservice Users

Microservice which manages users in our service

## Prerequisites

```bash
docker run -d --name pg-users -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=events -p 5432:5432 postgres:13
```

Only one instance is needed for all MSs
```bash
docker run -d --name=dev-consul -p 8500:8500 -e CONSUL_BIND_INTERFACE=eth0 consul
```
To add API token to Consul set KEY/VALUE to:
KEY: environments/dev/services/basketball-users/1.0.0/config/rest-config/api-token
VALUE: string for token