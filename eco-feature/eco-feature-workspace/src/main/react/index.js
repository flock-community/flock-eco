import React from 'react'
import ReactDOM from 'react-dom'
import UserGroupFeature from './user_group/UserGroupFeature'

class App extends React.Component {
  render() {
    return <UserGroupFeature />
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
