# 作业报告: 自动化仓库管理系统的Restful API开发

## 1. 项目概述

本项目的目标是开发一个自动化仓库管理系统，采用前后端分离的架构。后端部分的核心任务是定义并实现一套Restful API，以支持仓库管理功能。

## 2. 基本要求的实现

1. 遵循Assignment 1中定义的Restful API。
2. 使用Java和Spring WebFlux API实现API。
3. 实现 reactive service 层 和 dao 层。
4. 使用WebTestClient API进行测试。
5. 使用Spring Security和JWT进行API认证。
6. 使用 functional api。

## 3. 加分实现

1. Session Control
2. Log
3. Rate Limiting

## 4. 项目结构

```
Reactive-Warehouse [warehouse]
├── .idea
├── .mvn
│   └── wrapper
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
├── src
│   ├── main
│   │   └── java
│   │       └── man.api.warehouse
│   │           ├── common
│   │           │   ├── exception
│   │           │   └── utils
│   │           ├── config.common
│   │           └── system
│   │               ├── controller
│   │               ├── mapper
│   │               ├── model
│   │               ├── repository
│   │               └── service
│   ├── resources
│   │   ├── application.properties
│   └── test
│       └── java
│           └── man.api.warehouse
│               ├── ProductHandlerTests
│               └── WarehouseApplicationTests
├── WarehouseApplication
└── WarehouseApplicationTests
```

## 5. 实现细节

### 5.1 API实现

- 使用Spring WebFlux实现非阻塞的Restful API。<img width="718" alt="截屏2024-05-31 18 16 14" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/e2ad8ff2-f8b5-4b40-8fd6-c23695425ae2">
- 使用函数式API定义路由和处理程序。<img width="767" alt="截屏2024-05-31 18 16 29" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/8f2817d9-7ff8-4639-a087-2b84aa3491f2">

### 5.2 响应式服务层和DAO层

- 使用Reactor库实现响应式编程模型。
  <img width="522" alt="截屏2024-05-31 18 15 38" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/3e956341-eb08-4a48-9dcd-ab0f854d75af">

- 使用=Reactive Repositories进行数据访问。
  <img width="791" alt="截屏2024-05-31 18 15 14" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/21eb33b7-8efe-4fcb-a038-79f4856e5da5">

### 5.3 API认证

使用Spring Security和JWT进行API认证。
<img width="992" alt="截屏2024-05-31 18 14 28" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/7bdd48bc-cab7-4a24-8ace-c772d62fa83b">

### 5.4 会话控制

实现了基于JWT的会话管理

### 5.5 日志

使用SLF4J和Logback进行日志记录

### 5.6 流量限制

- 使用Alibaba的Sentinel实现限流和接口访问流量可视化
<img width="571" alt="截屏2024-05-31 17 55 37" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/4c578a28-981d-4d16-9c97-12326d180130">

- 在工程的pom文件加上sentinel的Spring Cloud起步依赖
<img width="430" alt="截屏2024-05-31 17 53 29" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/187ea04b-ab85-4837-aa2b-d84c3e9356e1">

- 指定流量控制规则，限制QPS为10进行流量限制
<img width="822" alt="截屏2024-05-31 17 58 20" src="https://github.com/Gaby11235/Reactive-Warehouse/assets/88358084/70a7c12b-4720-4a75-9719-71ee4d9e8d0a">

对接口进行保护

## 6. 运行和测试

### 6.1 运行项目

1. 克隆项目仓库：

   ```
   git clone https://github.com/your-repo/warehouse-management-system.git
   ```

2. 进入项目目录并运行：

   ```
   cd warehouse-management-system
   ./mvnw spring-boot:run
   ```

### 6.2 测试项目

