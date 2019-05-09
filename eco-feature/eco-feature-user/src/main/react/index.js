import React from 'react'
import ReactDOM from 'react-dom'

import UserFeature from './user/UserFeature'
import ProfileFeature from './profile/ProfileFeature'
import UserGroupFeature from "./user_group/UserGroupFeature";

class App extends React.Component {
  render() {
    return <UserGroupFeature />
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
