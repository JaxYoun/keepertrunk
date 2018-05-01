---
title: 在线文档编写规范
tag: 使用指南
author: 成凯
publishDate: 2018-01-14
---

文档svn地址： https://192.168.121.130/svn/Product/troy-keeper/trunk/ui/docs

## 文档目录结构说明

```

  文档目录结构：
    └─posts             // 文档根目录
      ├─BEresource      // 后端开发
      ├─BEspec          // 后端规范
      ├─FEresource      // 前端开发
      ├─FEspec          // 前端规范
      ├─deployment      // 部署发布
      └─help            // 使用指南
```

### 1. 文档命名

文档名称可以使用英文字母、数字；尽量避免使用符号，不能使用“.”

### 2. 文档信息

  文档信息是文档必不可少的部分，包含文档标题、分类、作者、日期，必须放置在文档顶部，以下为示例：

```
  ---
  title: 在线文档编写规范
  tag: ['使用指南']
  author: 成凯
  publishDate: 2018-03-14
  ---
```

### 3. 文档内容

  编写文档内容要求：

    1） 请严格按照文档规范编写
    2） 标题样式请从H2开始使用，示例：  ## 标题

### 4. 提交文档

    编写完成后提交至SVN服务器，暂不支持在线文档自动部署

## 语法参考

[markdown 语法](http://www.appinn.com/markdown/index.html)

## 当前文档示例

````
  ---
  title: 在线文档编写规范
  tag: ['使用指南']
  author: 成凯
  publishDate: 2018-03-14
  ---

  svn地址： https://192.168.121.130/svn/Product/troy-keeper/trunk/ui/docs

  ## 文档目录结构说明

  ```

    目录结构：
      └─docs              // 根目录
        ├─BEresource      // 后端开发
        ├─BEspec          // 后端规范
        ├─FEresource      // 前端开发
        ├─FEspec          // 前端规范
        ├─deployment      // 部署发布
        └─help            // 使用指南
  ```

  ### 1. 文档命名

  文档名称可以使用英文字母、数字；尽量避免使用符号，不能使用“.”

  ### 2. 文档信息

  > 文档信息是文档必不可少的部分，包含文档标题、分类、作者、日期，必须放置在文档顶部，以下为示例：

  ```
  ---
  title: 在线文档编写规范
  tag: ['使用指南']
  author: 成凯
  publishDate: 2018-03-14
  ---
  ```

  ### 3. 文档内容

    编写文档内容要求：

      1） 请严格按照文档规范编写
      2） 标题样式请从H2开始使用，示例：  ## 标题

  ### 4. 提交文档

      编写完成后提交至SVN服务器，暂不支持在线文档自动部署

  ## 语法参考

  [markdown 语法](http://www.appinn.com/markdown/index.html)
````
