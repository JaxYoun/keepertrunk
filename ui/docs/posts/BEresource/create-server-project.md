---
title: 后台项目搭建
tag: 后端开发
publishDate: 2018-03-13
---

## 1. 项目拷贝

基础工程可以在以下 svn 目录中拷贝，工程代码基于 maven 搭建，使用前请确保 maven 配置正确。

svn 地址： https://192.168.121.130/svn/Product/troy-keeper/demo/monomer-demo

## 2. 项目结构

```files
├─src
│ ├─java
│ │ └─com.troy.keeper.monomer.demo
│ │   ├─config                  // 配置文件
│ │   ├─domain                  // 实体类
│ │   ├─dto                     // 数据传输类
│ │   ├─repository              // 数据访问层
│ │   ├─security                // 接口访问控制
│ │   ├─service                 // 数据服务层
│ │   │  └─impl                 // 数据服务层实现类
│ │   ├─utils                   // 工具类
│ │   ├─web                     //
│ │   │  └─rest                 // 前端控制器
│ │   ├─AppApp.java             // 启动类
│ │   └─ApplicationWebXml.java  // 启动配置
│ └─resources
│   └─config
│     ├─application.yml         // 基础配置文件
│     ├─application-dev.yml     // 开发环境配置文件
│     ├─application-perf.yml    // 预发布环境配置文件
│     ├─application-prod.yml    // 产品环境配置文件
│     ├─application-test.yml    // 测试环境配置文件
│     ├─bootstrap.yml           //
│     └─bootstrap-prod.yml      //
└─pom.xml                       //  maven pom文件
```

## 3. 配置文件修改

### 3.1. pom 文件修改

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>

  <!-- 所有单体工程父节点 -->
    <parent>
        <artifactId>monomer</artifactId>
        <groupId>com.troy.keeper</groupId>
        <version>0.0.6-SNAPSHOT</version>

    </parent>

    <groupId>com.troy.keeper</groupId>
    <artifactId>monomer-demo</artifactId>
    <packaging>jar</packaging>
    <name>Monomer-Demo</name>
    <properties>
        <org.mapstruct.version>1.1.0.Final</org.mapstruct.version>
    </properties>

    <dependencies>
    <!-- 单体工程安全模块，系统管理依赖改模块 -->
        <dependency>
            <groupId>com.troy.keeper</groupId>
            <artifactId>monomer-security</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
    <!-- 单体应用一般依赖monomer父工程 -->
        <dependency>
            <groupId>com.troy.keeper</groupId>
            <artifactId>troy-keeper-monomer-parent</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
    <!-- 开发工具 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
            <!-- optional=true,依赖不会传递，该项目依赖devtools；之后依赖myboot项目的项目如果想要使用devtools，需要重新引入 -->
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <!--fork :  如果没有该项配置，可能devtools不会起作用，即应用不会restart -->
            </plugin>
        </plugins>
    </build>
</project>
```

### 3.2. 项目名称配置

* 修改文件 application.yml 中应用名称，示例如下：

```yml
spring:
    application:
        name: demo // 应用名称为 demo
```

### 3.3. 数据库配置

项目默认使用 dev 配置文件启动，修改配置文件时应修改对应的启动文件

#### 3.3.1 Oracle 数据库配置

* 数据源

修改文件 application-dev.yml 中数据源配置，示例如下：

```yml
spring:
    datasource:
        type: com.zaxxer.hikari.HikariDataSource    // 框架使用hikari链接池
        driver-class-name: oracle.jdbc.OracleDriver  // oracle链接驱动类
        url: jdbc:oracle:thin:@192.168.121.222:1521:orcl // 具体数据库链接
        username: demo // 数据库用户名
        password: demo // 数据库密码
        hikari:
            data-source-properties:
                cachePrepStmts: true // 设置是否对预编译使用local cache
                prepStmtCacheSize: 250 // 指定了local cache的大小，使用了LRU进行逐出
                prepStmtCacheSqlLimit: 2048 // 长度限制，默认256。超过该长度后，不使用预编译
                useServerPrepStmts: true // 是否启用预编译
```

* JPA 配置

修改文件 application-dev.yml 中 JPA 配置，示例如下：

```yml
spring:
    jpa:
        database-platform: org.hibernate.dialect.Oracle10gDialect // 数据库方言配置
        database: ORACLE // 数据库类型
        show-sql: true //  是否显示sql语句
        properties:
            hibernate.default_schema: DEMO  // oracle数据库需要配置默认schema
            hibernate.id.new_generator_mappings: true
            hibernate.cache.use_second_level_cache: true // 是否启用二级缓存
            hibernate.cache.use_query_cache: true // 是否启用查询缓存
            hibernate.generate_statistics: true
            hibernate.cache.region.factory_class: com.hazelcast.hibernate.HazelcastCacheRegionFactory
            hibernate.cache.hazelcast.instance_name: demo // 数据库缓存实例名称
            hibernate.cache.use_minimal_puts: true
            hibernate.cache.hazelcast.use_lite_member: true
```

##### 3.3.2 Mysql 数据库配置

* 数据源修改文件 application-dev.yml 中数据源配置，示例如下：

```yml
spring:
    datasource:
        type: com.zaxxer.hikari.HikariDataSource    // 框架使用hikari链接池
        url: jdbc:mysql://192.168.121.222:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false // 具体数据库链接
        username: root // 数据库用户名
        password: root // 数据库密码
        hikari:
            data-source-properties:
                cachePrepStmts: true // 设置是否对预编译使用local cache
                prepStmtCacheSize: 250 // 指定了local cache的大小，使用了LRU进行逐出
                prepStmtCacheSqlLimit: 2048 // 长度限制，默认256。超过该长度后，不使用预编译
                useServerPrepStmts: true // 是否启用预编译
```

* JPA 配置修改文件 application-dev.yml 中 JPA 配置，示例如下：

```yml
spring:
    jpa:
        database-platform: org.hibernate.dialect.MySQL57InnoDBDialect // 数据库方言配置
        database: MYSQL // 数据库类型
        show-sql: true //  是否显示sql语句
        properties:
            hibernate.id.new_generator_mappings: true
            hibernate.cache.use_second_level_cache: true // 是否启用二级缓存
            hibernate.cache.use_query_cache: true // 是否启用查询缓存
            hibernate.generate_statistics: true
            hibernate.cache.region.factory_class: com.hazelcast.hibernate.HazelcastCacheRegionFactory
            hibernate.cache.hazelcast.instance_name: demo // 数据库缓存实例名称
            hibernate.cache.use_minimal_puts: true
            hibernate.cache.hazelcast.use_lite_member: true
```

### 3.4. Redis 配置

* 修改文件 application-dev.yml 中 Redis 配置，示例如下：

```yml
spring:
    redis:
        host: 192.168.121.224 // redis服务ip地址
        port: 6379            // Redis服务端口
        password: demo        // Redis服务密码
        database: 3           // redis数据库序号配置，一般是0至15
```

## 4. 项目运行

在 ide 中选择 AppApp.java 文件，右键 run 或 debug 可以启动应用程序如果是运行打包后的文件，则可使用以下命令启动应用;其中 dev 是要使用的配置文件，monomer-demo-0.0.6-SNAPSHOT.jar 是要运行的 jar 包

```java
nohup java -Dspring.profiles.active=dev -jar monomer-demo-0.0.6-SNAPSHOT.jar
```

## 5. 项目打包

项目是基于 maven 的工程，所以所有打包都使用 maven 命令来完成在项目根目录执行以下 maven 命令完成打包

```cmd
maven clean compile package
```
