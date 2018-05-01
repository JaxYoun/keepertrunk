## troy-admin 系统管理开发指南


#### 1. 目录结构
```
├── /lib/            # 项目输出目录
├── /src/            # 项目源码目录
│ ├── /routes/       # 路由组件
│ ├── /models/       # 数据模型
│ ├── /services/     # 数据接口
│ ├── /utils/        # 工具函数
│ │ ├── api.js       # 项目api配置
│ │ ├── config.js    # 项目常规配置
│ └─└─ index.js      # 项目配置
├── package.json     # 项目信息
└── .babelrc         # babel配置
```
#### 2. 编译项目
```
  npm run build
```

#### 3. 发布项目
```
  npm login --registry=http://10.0.40.57:8081/repository/npm-local/
  
  user: ***
  password: ***
  email: ***@sc-troy.com

  npm publish --registry=http://10.0.40.57:8081/repository/npm-local/
```

#### 4. 流程规范
  
  1. 开发
  2. 编译项目  
  3. 更新日志  CHANGELOG.md
  4. 更新版本  package.json
  5. 发布项目