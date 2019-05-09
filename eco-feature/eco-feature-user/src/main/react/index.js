import React from 'react'
import ReactDOM from 'react-dom'

import Button from '@material-ui/core/Button'

import AddIcon from '@material-ui/icons/Add'

import UserFeature from './user/UserFeature'
import ProfileFeature from './profile/ProfileFeature'
import UserGroupFeature from "./user_group/UserGroupFeature";

class App extends React.Component {
  render() {
    return <UserGroupFeature />
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
