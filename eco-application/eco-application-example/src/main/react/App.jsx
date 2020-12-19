import React, {createElement, useState} from 'react'
import {Container} from '@material-ui/core'
import ApolloClient from 'apollo-boost'
import {ApolloProvider} from '@apollo/react-hooks'
import {AppDrawer} from './AppDrawer'
import {features, findComponentName} from './data'
import {MemberTable} from '@flock-community/flock-eco-feature-member'
const client = new ApolloClient({
  uri: '/graphql',
})

export function App() {
  const hash = window.location.hash
  const component = Object.keys(features)
    .reduce((acc, cur) => acc.concat(features[cur]), [])
    .find(it => `#${findComponentName(it)}` === hash)

  const [state, setState] = useState({
    drawer: !hash,
    hash,
    component,
  })

  const handleFeatureClick = component => {
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
    <ApolloProvider client={client}>
      <Container style={{height: '100%'}}>
        <AppDrawer open={state.drawer} onClick={handleFeatureClick} />
        {elm}
      </Container>
    </ApolloProvider>
  )
}
