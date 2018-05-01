module.exports = {
  sitename: '创意信息',
  tagline: '在线帮助文档',
  footer: 'Copyright 四川创意信息技术股份有限公司',
  navigation: [
    {
      title: 'ANTD',
      link: 'https://ant.design/docs/react/introduce-cn',
    },
  ],
  port: 8001,
  theme: './theme',
  output: './_site',
  routes: ['help', 'deployment', 'BEresource','BEspec','FEresource','FEspec'],
  menu: ['使用指南','部署发布','前端开发', '后端开发', '前端规范', '后端规范'],
};
