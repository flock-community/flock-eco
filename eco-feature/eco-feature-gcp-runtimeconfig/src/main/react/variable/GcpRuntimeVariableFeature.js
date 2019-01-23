import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import GcpRuntimeVariableTable from './GcpRuntimeVariableTable'

import Button from '@material-ui/core/Button'

import AddIcon from '@material-ui/icons/Add'

const styles = theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing.unit,
  },
})

class GcpRuntimeVariableFeature extends React.Component {

  render() {
    const {classes} = this.props

    return (
      <div>
        <GcpRuntimeVariableTable config="igudes_prod"/>

        <Button
          variant="fab"
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.handleNewClick}
        >
          <AddIcon />
        </Button>
      </div>
    )
  }

}

export default withStyles(styles)(GcpRuntimeVariableFeature)
