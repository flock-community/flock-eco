import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'

const styles = theme => ({})

class MemberDialog extends React.Component {
  constructor(props) {
    super(props)

    this.handleClose = () => {
      this.props.onClose()
    }

    this.handleSave = () => {
      this.props.onSave()
    }

    this.handleDelete = () => {
      this.props.onDelete()
    }
  }

  render() {
    const {classes, open} = this.props

    return (
      <Dialog
        fullWidth
        maxWidth={'sm'}
        open={open}
        onClose={this.handleClose}
        aria-labelledby="simple-dialog-title"
        classes={classes}
      >
        <DialogTitle id="simple-dialog-title">Member Field</DialogTitle>
        <DialogContent>{this.props.children}</DialogContent>
        <DialogActions>
          <Button onClick={this.handleClose} color="primary">
            Cancel
          </Button>
          {this.handleDelete && (
            <Button onClick={this.handleDelete} color="secondary">
              Delete
            </Button>
          )}
          <Button onClick={this.handleSave} color="primary" autoFocus>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    )
  }
}

export default withStyles(styles)(MemberDialog)
