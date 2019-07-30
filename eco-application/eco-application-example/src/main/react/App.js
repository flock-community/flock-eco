import React from 'react'
import {withStyles} from '@material-ui/core'
import FeatureList from './FeatureList'

import {features} from './data'

const styles = theme => ({})

class App extends React.Component {

  state = {
    component: null,
  }

  handleFeatureChange = component => {
    this.setState({component})
  }



  render() {
    return <React.Fragment>
      <FeatureList onChange={this.handleFeatureChange}/>
      {this.state.component}
      </React.Fragment>
  }
}

export default withStyles(styles)(App)