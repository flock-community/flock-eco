import React from 'react'
import ReactDOM from 'react-dom'

import GcpRuntimeVariableFeature from './variable/GcpRuntimeVariableFeature'

class App extends React.Component {
  render() {
    return (
      <React.Fragment>
        <GcpRuntimeVariableFeature configs="igudes_prod" />
      </React.Fragment>
    )
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
