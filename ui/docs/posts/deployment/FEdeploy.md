---
title: 前端项目部署
tag: ['部署发布']
publishDate: 2018-03-13
---

## 1. 解压项目部署包

> 注意：解压后请查看是否包含依赖包文件夹（node_modules），若无依赖包请参照第 3 步进行安装依赖包

## 2. 安装 nodejs

> 注意：如已安装 nodejs 则跳过此步骤，否则请按照[《Nodejs 安装文档》](/docs/feresource/nodejs-cn) 进行安装
>
> 查看 nodejs 版本命令： node --version

## 3. 安装项目依赖包

> 注意：如已有依赖包文件夹(node_modules)则跳过此步骤，否则请执行以下命令安装
>
> 常用依赖包名称：express ejs http-proxy-middleware

```
  npm install --production
```

## 4. 配置 pm2

> 注意：当前系统已配置 pm2 则跳过此步骤，否则请将 pm2 安装至当前系统中
>
> 查看 pm2 是否已配置命令：pm2 --version

* 方法 1 >>> 将 pm2 安装包拷贝至当前系统中，执行以下命令
* 方法 2 >>> 执行 npm install pm2 下载安装包至当前系统中（建议在当前系统软件目录安装），再执行以下命令

```
  ln -s {pm2安装包路径}/node_modules/pm2/bin/pm2 /usr/local/bin/pm2
```

## 5. 启动项目

> 请在当前项目的根目录中执行以下命令

```
  npm run server
```

## 6.常用命令

> 请在当前项目的根目录中执行以下命令：

### 1). 查看当前服务器所有前端项目服务

```
  npm run list
```

### 2). 重新启动当前项目

```
  npm run restart
```

### 3). 停止当前项目

```
  npm run stop
```

### 4). 查看当前项目日志

```
  npm run logs
```
