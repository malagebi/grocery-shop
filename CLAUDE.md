# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build
mvnw.cmd clean package

# Run
mvnw.cmd spring-boot:run
# OR
java -jar target/grocery-shop-0.0.1-SNAPSHOT.jar

# Run tests
mvnw.cmd test

# Generate MyBatis mapper code from DB schema
mvnw.cmd mybatis-generator:generate

# Build Docker image
mvnw.cmd clean package docker:build
```

- App runs on port **8090**. Swagger UI: `http://localhost:8090/swagger-ui.html` / `http://localhost:8090/doc.html`
- A standalone **Netty server** also starts on port **8000** at application startup (via `NettyServer`).

## Architecture

This is a Spring Boot 2.1.7 / Java 8 application with three loosely coupled modules:

### 1. Grocery Shop REST API (primary)
Standard layered MVC: `Controller → Service → Mapper (MyBatis) → MySQL`

- All responses are wrapped in `base/CommonResult<T>` (success/error envelope).
- Pagination uses PageHelper and returns `base/CommonPageInfo<T>`.
- Global exception handling via `base/ExceptionControllerAdvice` (`@RestControllerAdvice`).
- Custom `CustomerException` is the convention for throwing business errors that get caught there.
- `LoginController` is the main controller despite its name — it handles commodities, QR codes, and CAPTCHA, not just login.

### 2. Train Ticket Integration (`trainticket/`)
Self-contained sub-module that proxies requests to China's 12306 system using `RestTemplate`. No shared service or mapper dependencies — it only uses `TrainTicketService/Impl` and its own DTOs.

### 3. Netty Demo (`netty/`)
Standalone educational demo. `NettyServer` starts independently of Spring (not a Spring bean) and listens on port 8000. It has no integration with the REST layer.

## Key Conventions

- **JSON serialization**: Alibaba FastJSON (configured in `FastJsonConfiguration`), not Jackson.
- **Database**: MySQL via Druid connection pool. Config in `application-dev.properties` (active profile is `dev`).
- **MyBatis**: Mapper interfaces in `mapper/`, XML files in `src/main/resources/mapper/`. Entities in `entity/` use Lombok `@Data`.
- **Swagger**: Controllers are annotated with SpringFox `@Api`/`@ApiOperation`. New controllers should follow this pattern.
- **Interceptor vs Filter**: `CustomerFilter` (servlet filter) logs all requests. `CustomerInterceptor` (Spring interceptor) runs pre/post handler logic. Both are registered — avoid duplicating logic between them.
- **Future tasks**: `service/MyFutureTask.java` is an educational demo of `FutureTask`; not part of any production flow.
