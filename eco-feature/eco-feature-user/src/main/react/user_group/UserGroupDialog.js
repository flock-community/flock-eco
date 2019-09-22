import React, {useEffect, useState} from 'react'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import {ResourceClient} from '@flock-eco/core'
import {USER_GROUP_FORM_ID, UserGroupForm} from './UserGroupForm'
import Typography from '@material-ui/core/Typography'
import IconButton from '@material-ui/core/IconButton'
import CloseIcon from '@material-ui/icons/Close'
import makeStyles from '@material-ui/core/styles/makeStyles'
import UserGroupClient from './UserGroupClient'

const useStyles = makeStyles(theme => ({
  root: {
    margin: 0,
    padding: theme.spacing(2),
  },
  closeButton: {
    position: 'absolute',
    right: theme.spacing(1),
    top: theme.spacing(1),
    color: theme.palette.grey[500],
  },
}))

export function UserGroupDialog({open, code, onComplete}) {
  const classes = useStyles()

  const [state, setState] = useState(null)

  const client = new ResourceClient('/api/user-groups')

  useEffect(() => {
    if (code !== null) {
      UserGroupClient.findByCode(code)
        .then(userGroup => setState(userGroup))
    } else {
      setState(null)
    }
  }, [code])

  // const handleUsersChange = value => {
  //   const users = value.map(it => it.user)
  //   this.setState({users})
  // }

  const handleClose = () => {
    onComplete && onComplete()
  }

  const handleSubmit = value => {
    if (!value.id) {
      client.create(value)
        .then(it => onComplete && onComplete(it))
    } else {
      client.update(value.code, value)
        .then(it => onComplete && onComplete(it))
    }
  }

  const handleDelete = () => {
    client.delete(value.code)
      .then(it => onComplete && onComplete(it))
  }

  return (
    <Dialog
      fullWidth
      maxWidth={'sm'}
      open={open}
      onClose={handleClose}
      classes={classes}
    >
      <DialogTitle disableTypography>
        <Typography variant="h6">User</Typography>
        <IconButton
          aria-label="close"
          className={classes.closeButton}
          onClick={handleClose}
        >
          <CloseIcon/>
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <UserGroupForm
          value={state}
          onSummit={handleSubmit}/>
      </DialogContent>
      <DialogActions>
        {(state && state.id) && (
          <Button onClick={handleDelete} >
            Delete
          </Button>
        )}
        <Button
          variant="contained"
          color="primary"
          form={USER_GROUP_FORM_ID}
          type="submit"
        >
          Save
        </Button>
      </DialogActions>
    </Dialog>
  )
}
