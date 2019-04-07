import React from 'react'
import ReactDOM from 'react-dom'

import ProfileFeature from '@flock-eco/feature-user/src/main/react/profile/ProfileFeature'

class App extends React.Component {
  render() {
    return <ProfileFeature/>
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
