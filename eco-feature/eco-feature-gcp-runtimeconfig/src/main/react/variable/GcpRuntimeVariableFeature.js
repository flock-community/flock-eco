import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import GcpRuntimeVariableTable from './GcpRuntimeVariableTable'

import Fab from '@material-ui/core/Fab'

import AddIcon from '@material-ui/icons/Add'

const styles = theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing(1),
  },
})

class GcpRuntimeVariableFeature extends React.Component {
  render() {
    const {classes, config} = this.props

    return (
      <div>
        <GcpRuntimeVariableTable config={config} />

        <Fab
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.handleNewClick}
        >
          <AddIcon />
        </Fab>
      </div>
    )
  }
}

export default withStyles(styles)(GcpRuntimeVariableFeature)
