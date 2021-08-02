module.exports = {
  client: {
    service: {
      name: 'flock-eco-workspace',
      localSchemaFile: [
        '../../eco-core/src/main/graphql/schema.graphqls',
        './src/main/graphql/workspace.graphqls',
      ],
    },
    includes: ['src/main/react/**/*'],
  },
}
