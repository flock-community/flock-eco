const ecoConfig = require("@flock-community/flock-eco-webpack");

const config = {
    ...ecoConfig,
    devServer: {
        ...ecoConfig.devServer,
        historyApiFallback: true,
        proxy: {
            ...ecoConfig.devServer.proxy,
            '/bootstrap': 'http://localhost:8080',
            '/logout': 'http://localhost:8080',
            '/tasks/*': 'http://localhost:8080',
            '/api/**': {
                target: 'http://localhost:8080',
                onProxyReq: (proxyReq, req, res) => {
                    proxyReq.setHeader('x-tenant', 'FOOBAR');
                }
            },
            '/login': {
                target: 'http://localhost:8080',
                onProxyReq: (proxyReq, req, res) => {
                    proxyReq.setHeader('x-tenant', 'FOOBAR');
                }
            },
        },
    },
    module: {
        rules: [
          ...ecoConfig.module.rules,
        ],
    },
}
module.exports = config;