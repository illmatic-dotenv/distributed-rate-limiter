# Distributed Rate Limiter Service

A production-style rate limiter microservice that any application can plug into.
Supports 3 throttling algorithms backed by Redis with atomic Lua scripts.

## Algorithms
- **Token Bucket** — allows controlled bursts (Spotify/Uber style)
- **Sliding Window Log** — most accurate, used at FAANG scale
- **Fixed Window Counter** — simple and memory efficient

## Tech Stack
- Java 17 + Spring Boot
- Redis with atomic Lua scripts (prevents race conditions)
- Multiple instances sharing one Redis counter (distributed proof)

## How to Run
1. Start Redis: `redis-server`
2. Run the app: `./mvnw spring-boot:run`
3. Test it:

POST http://localhost:8080/api/v1/check
Content-Type: application/json

{
  "clientId": "user_123",
  "algorithm": "FIXED_WINDOW",
  "maxRequests": 5,
  "windowSeconds": 60
}

## Algorithms Explained
- **Fixed Window** — resets counter every N seconds. Simple but can allow 2x traffic at window boundaries
- **Token Bucket** — tokens refill gradually. Allows bursts up to bucket capacity
- **Sliding Window** — tracks exact timestamps. Most accurate, no boundary spikes

## API Response
```json
{
  "allowed": true,
  "remainingRequests": 4,
  "resetAfterSeconds": 60,
  "message": "Request allowed"
}
```

## Dashboard
Run `python dashboard.py` to see live request counts per client in real time.

