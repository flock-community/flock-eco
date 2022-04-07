import React, {createElement, useState} from 'react'
import {Container} from '@material-ui/core'
import ApolloClient from 'apollo-boost'
import {ApolloProvider} from '@apollo/react-hooks'
import {AppDrawer} from './AppDrawer'
import {features, findComponentName} from './data'
import {I18nextProvider, initReactI18next} from 'react-i18next'
import i18n from 'i18next'
import {i18n as i18nUser} from '@flock-community/flock-eco-feature-user'

const languages = ['en_EN', 'nl_NL']

const resources = languages.reduce(
  (acc, cur) => ({
    ...acc,
    [cur]: {
      translation: {...i18nUser[cur]},
    },
  }),
  {},
)

console.log('resources', resources)

const client = new ApolloClient({
  uri: '/graphql',
})

i18n.use(initReactI18next).init({
  resources,
  fallbackLng: 'nl_NL',
  debug: true,
  interpolation: {
    escapeValue: false,
  },
})

export function App() {
  const hash = window.location.hash
  const component = Object.keys(features)
    .reduce((acc, cur) => acc.concat(features[cur]), [])
    .find((it) => `#${findComponentName(it)}` === hash)

  const [state, setState] = useState({
    drawer: !hash,
    hash,
    component,
  })

  const handleFeatureClick = (component) => {
    const hash = `#${findComponentName(component)}`
    setState({
      component,
      drawer: false,
      hash,
    })
    window.location.hash = hash
  }

  const elm = state.component && createElement(state.component, {})

  return (
    <I18nextProvider i18n={i18n}>
      <ApolloProvider client={client}>
        <Container style={{height: '100%'}}>
          <AppDrawer open={state.drawer} onClick={handleFeatureClick} />
          {elm}
        </Container>
      </ApolloProvider>
    </I18nextProvider>
  )
}
