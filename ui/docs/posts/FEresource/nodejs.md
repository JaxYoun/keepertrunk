---
title: Nodejs 安装
tag: 前端开发
author: 成凯
publishDate: 2018-03-13
---

> 当前推荐安装版本：nodejs v8.7.0

# Linux 环境安装

## 1. 下载 nodejs 安装包

> 注意：安装环境不能连接互联网请提前下载好安装包，自行拷贝到安装环境中。步骤解释：进入目标目录，下载安装包

```linux
  cd /usr/src/

  wget https://npm.taobao.org/mirrors/node/v8.7.0/node-v8.7.0-linux-x64.tar.gz
```

## 2. 解压 nodejs 安装包

> 步骤解释：解压安装包后删除安装包

```linux
  tar -zxvf node-v8.7.0-linux-x64.tar.gz

  rm -rf node-v8.7.0-linux-x64.tar.gz
```

## 3. 创建 node、npm 命令链接

> 步骤解释：将下载的 node 及 npm 链接指向/usr/local/bin/node 目录，如果没有 local 文件夹，则直接指向/usr/bin 目录即可

```linux
  ln -s /usr/src/node-v8.7.0-linux-x64/bin/node /usr/local/bin/node

  ln -s /usr/src/node-v8.7.0-linux-x64/bin/npm /usr/local/bin/npm
```

## 4. 检查是否安装成功

```linux
  node -v
  npm -v
```

## 5. 设置 npm 仓库源

```linux
  内网：
  npm config set registry=http://10.0.40.57:8081/repository/tnpm

  外网：
  npm config set registry=http://218.88.13.36:8083/repository/tnpm
```

# Windows 环境安装

## 1. 下载 nodejs 安装包

[点击下载 nodejs v8.7.0](https://npm.taobao.org/mirrors/node/v8.7.0/node-v8.7.0-x64.msi)

## 2. 安装 nodejs

运行 node-v8.7.0-x64.msi 根据提示完成安装。

## 3. 检查是否安装成功

```windows
  node -v

  npm -v
```

## 4. 设置 npm 仓库源

```windows
  内网：
  npm config set registry=http://10.0.40.57:8081/repository/tnpm

  外网：
  npm config set registry=http://218.88.13.36:8083/repository/tnpm
```
