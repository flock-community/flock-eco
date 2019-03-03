import "@babel/polyfill";

import React from 'react'
import ReactDOM from 'react-dom'

import Button from '@material-ui/core/Button'

import AddIcon from '@material-ui/icons/Add'

import UserManager from './features/user/UserFeature'

class App extends React.Component {
  render() {
    const divStyle = {
      background: 'red',
    }
    return <UserManager />
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
