[![Build Status](https://travis-ci.org/fri-riders/auth.svg?branch=master)](https://travis-ci.org/fri-riders/auth)
# Auth Service

## Run with Docker Compose
1. Build app: `mvn clean package`
1. Run: `docker-compose up --build`

App is accessible on port `8083`

# Registered endpoints
## Auth
* `POST: /v1/auth/issue` Returns list of users
* `POST: /v1/auth/verify` Create new user (params: `email:string, password:string`)
## Config
* `GET: /v1/config` Returns list of config values
* `GET: /v1/config/info` Returns info about project
## Health
* `GET: /health` Returns health status
* `GET: /v1/health-test/instance` Returns info about instance
* `POST: /v1/health-test/update` Update property `healthy` (params: `healthy:boolean`)
* `GET: /v1/health-test/dos/{n}` Execute calculation of n-th Fibonacci number
## Metrics
* `GET: /metrics` Returns metrics
