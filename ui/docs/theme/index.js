const path = require('path');
const router = require('./config');
let routes = router.routes.map(route => {
  return {
    path: `/posts/${route}/:post`,
    dataPath: `/${route}/:post`,
    component: './template/Post',
  };
});
routes.push({
  path: `/`,
  component: './template/Archive',
})
module.exports = {
  lazyLoad: true,
  pick: {
    posts(markdownData) {
      return {
        meta: markdownData.meta,
        description: markdownData.description,
      };
    },
  },
  plugins: [
    path.join(__dirname, '..', 'node_modules', 'bisheng-plugin-description'),
  ],
  routes,
};
