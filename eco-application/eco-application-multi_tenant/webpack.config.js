const ecoConfig = require("@flock-community/flock-eco-webpack");
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

module.exports = (env, argv) => ({
    ...ecoConfig,
    plugins: [
        ...ecoConfig.plugins,
        argv.mode === 'development' ? new BundleAnalyzerPlugin() : undefined,
    ].filter(it => it !== undefined),
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
})