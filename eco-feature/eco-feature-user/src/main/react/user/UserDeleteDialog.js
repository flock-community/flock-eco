import React, {useEffect, useState} from 'react'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import {USER_FORM_ID, UserForm} from './UserForm'
import UserClient from './UserClient'
import IconButton from '@material-ui/core/IconButton'
import CloseIcon from '@material-ui/icons/Close'
import Typography from '@material-ui/core/Typography'
import makeStyles from '@material-ui/core/styles/makeStyles'

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

export function UserDeleteDialog({open, value, onClose, onDelete}) {

  const classes = useStyles()

  const handleClose = ev => {
    onClose && onClose()
  }

  const handleDelete = ev => {
    onDelete && onDelete()
  }

  return (
    <Dialog fullWidth maxWidth={'xs'} open={open} onClose={handleClose}>
      <DialogTitle disableTypography>
        <Typography variant="h6">User</Typography>
        <IconButton
          aria-label="close"
          className={classes.closeButton}
          onClick={handleClose}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <Typography>Would you Are you sure you want to delete user: {value && value.name}</Typography>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} >Cancel</Button>
        <Button variant="contained" color="primary" onClick={handleDelete} >Delete</Button>
      </DialogActions>
    </Dialog>
  )
}
