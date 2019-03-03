import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'

const styles = theme => ({})

class UserDialog extends React.Component {
  handleClose = () => {
    this.props.onClose()
  }

  handleSave = () => {
    this.props.onSave()
  }

  handleDelete = () => {
    this.props.onDelete()
  }

  render() {
    const {classes, open, onClose, selectedValue} = this.props

    return (
      <Dialog
        fullWidth
        maxWidth={'md'}
        open={open}
        onClose={this.handleClose}
        aria-labelledby="simple-dialog-title"
        classes={classes}
      >
        <DialogTitle id="simple-dialog-title">User</DialogTitle>
        <DialogContent>{this.props.children}</DialogContent>
        <DialogActions>
          <Button onClick={this.handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={this.handleDelete} color="secondary">
            Delete
          </Button>
          <Button onClick={this.handleSave} color="primary" autoFocus>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    )
  }
}

export default withStyles(styles)(UserDialog)
