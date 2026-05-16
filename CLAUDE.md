# CLAUDE.md

本文件为 Claude Code（claude.ai/code）在此仓库中工作时提供指导。

## 构建与运行

```bash
# 构建
mvnw.cmd clean package

# 运行
mvnw.cmd spring-boot:run
# 或者
java -jar target/grocery-shop-0.0.1-SNAPSHOT.jar

# 运行测试
mvnw.cmd test

# 根据数据库 Schema 生成 MyBatis Mapper 代码
mvnw.cmd mybatis-generator:generate

# 构建 Docker 镜像
mvnw.cmd clean package docker:build
```

- 应用运行在 **8090** 端口。Swagger UI：`http://localhost:8090/swagger-ui.html` / `http://localhost:8090/doc.html`
- 应用启动时还会独立启动一个 **Netty 服务器**（通过 `NettyServer`），监听 **8000** 端口。

## 架构

本项目是一个 Spring Boot 2.1.7 / Java 8 应用，包含三个松耦合模块：

### 1. 杂货铺 REST API（主模块）
标准分层 MVC：`Controller → Service → Mapper（MyBatis）→ MySQL`

- 所有响应统一封装在 `base/CommonResult<T>` 中（成功/错误信封）。
- 分页使用 PageHelper，返回 `base/CommonPageInfo<T>`。
- 全局异常处理通过 `base/ExceptionControllerAdvice`（`@RestControllerAdvice`）实现。
- 业务错误统一使用自定义 `CustomerException` 抛出，并在全局处理器中捕获。
- `LoginController` 是主控制器，尽管名称如此，它实际处理商品、二维码和验证码，而不仅仅是登录。

### 2. 火车票集成模块（`trainticket/`）
独立子模块，使用 `RestTemplate` 代理请求至中国 12306 系统。无共享 Service 或 Mapper 依赖，仅使用 `TrainTicketService/Impl` 及其自有 DTO。

### 3. Netty 示例（`netty/`）
独立的教学示例。`NettyServer` 独立于 Spring 启动（非 Spring Bean），监听 8000 端口，与 REST 层无任何集成。

## 主要约定

- **JSON 序列化**：使用阿里巴巴 FastJSON（在 `FastJsonConfiguration` 中配置），而非 Jackson。
- **数据库**：MySQL，通过 Druid 连接池访问。配置位于 `application-dev.properties`（激活的 Profile 为 `dev`）。
- **MyBatis**：Mapper 接口位于 `mapper/`，XML 文件位于 `src/main/resources/mapper/`，实体类位于 `entity/` 并使用 Lombok `@Data`。
- **Swagger**：控制器使用 SpringFox `@Api`/`@ApiOperation` 注解，新增控制器应遵循此规范。
- **拦截器与过滤器**：`CustomerFilter`（Servlet 过滤器）记录所有请求日志；`CustomerInterceptor`（Spring 拦截器）执行请求前后的处理逻辑。两者均已注册，避免在两者之间重复逻辑。
- **异步任务示例**：`service/MyFutureTask.java` 是 `FutureTask` 的教学示例，不属于任何生产流程。
