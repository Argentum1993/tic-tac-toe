const  { createProxyMiddleware }  = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/game',
        createProxyMiddleware({
            target: 'http://localhost:8080',
            ws: true,
        })
    );

    app.use(
        '/sockjs-node',
        createProxyMiddleware({
            target: 'http://localhost:8080',
            ws: true,
        })
    );
};