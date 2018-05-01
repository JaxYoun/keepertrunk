const path = require('path');
const router = require('./theme/config');
const { navigation, tagline, sitename, theme, output, port, footer } = router;

module.exports = {
  port,
  output,
  theme,
  themeConfig: {
    home: '/',
    sitename,
    tagline,
    navigation,
    footer,
  },
  devServerConfig: {},
  webpackConfig(config) {
    return config;
  },
};
