const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'https://10.177.44.94:9091',
      secure: false,
      changeOrigin: true,
    })
  );
};
