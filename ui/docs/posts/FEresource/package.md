---
title: 前端项目打包
tag: 前端开发
publishDate: 2018-03-13
---

## 安装依赖包

> 请在当前项目的[根目录]中执行以下命令：

```
  npm install
```

## 构建项目

> 请在当前项目的[根目录]中执行以下命令：

```
  npm run build
```

## 配置端口

> ./server/config.js

```
  proxy:[string] 后端服务地址及端口

  示例：proxy: 'http://sc-troy.com:8080'
```

```
  port: [number] 前端服务端口号

  示例： port: 8000
```

## 手动打包目录

> 暂不支持自动打包，后续将增加...
>
> 请将以下文件压缩打包，若发布为离线包请将（node_modules）一并压缩打包

├── /dist/

├── /server/

├── package.json

└── server.js
