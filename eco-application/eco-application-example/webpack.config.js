const ecoConfig = require('@flock-community/flock-eco-webpack')
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

module.exports = (env, argv) => ({
    ...ecoConfig,
    plugins: [
        ...ecoConfig.plugins,
        argv.mode === 'development' ? new BundleAnalyzerPlugin() : undefined,
    ].filter(it => it !== undefined),
})