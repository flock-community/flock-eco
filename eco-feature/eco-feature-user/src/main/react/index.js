import React from 'react'
import ReactDOM from 'react-dom'

import Button from '@material-ui/core/Button'

import AddIcon from '@material-ui/icons/Add'

import UserManager from './user/UserFeature'
import ProfileFeature from './profile/ProfileFeature'

class App extends React.Component {
  render() {
    return <ProfileFeature />
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
