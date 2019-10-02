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
import {UserDeleteDialog} from './UserDeleteDialog'

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

export function UserDialog({open, code, onComplete}) {

  const classes = useStyles()

  const [confirmOpen, setOpenConfirm] = useState(false)
  const [state, setState] = useState(null)
  const [authorities, setAuthorities] = useState(null)

  useEffect(() => {
    if (code !== null) {
      UserClient.findUserByCode(code).then(res => setState(res))
    } else {
      setState(null)
    }
  }, [code])

  useEffect(() => {
    UserClient.findAllAuthorities().then(setAuthorities)
  }, [])

  const handleConfirmDelete = ev => {
    setOpenConfirm(true)
  }

  const handleDelete = ev => {
    UserClient.deleteUser(state.code)
      .then(res => {
        onComplete && onComplete()
        setOpenConfirm(false)
      })
  }

  const handleCloseDelete = ev => {
    setOpenConfirm(false)
  }

  const handleReset = ev => {
    UserClient.resetUserPassword(state.code)
      .then(res => {
        onComplete && onComplete()
      })
  }

  const handleClose = ev => {
    onComplete && onComplete()
  }

  const handleSubmit = value => {
    if (value.code) {
      UserClient.updateUser(value.code, value)
        .then(() => onComplete && onComplete(state),
        )
    } else {
      UserClient.createUser(value)
        .then(() => onComplete && onComplete(state))
    }
  }

  return (<>
      <Dialog fullWidth maxWidth={'md'} open={open} onClose={handleClose}>
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
          <UserForm
            value={state}
            authorities={authorities}
            onSummit={handleSubmit}
          />
        </DialogContent>
        <DialogActions>
          {state && state.code && (
            <Button onClick={handleReset}>Reset password</Button>
          )}
          {state && state.code && <Button onClick={handleConfirmDelete}>Delete</Button>}
          <Button
            variant="contained"
            color="primary"
            form={USER_FORM_ID}
            type="submit"
          >
            Save
          </Button>
        </DialogActions>
      </Dialog>
      <UserDeleteDialog
        open={confirmOpen}
        value={state}
        onClose={handleCloseDelete}
        onDelete={handleDelete}/>
    </>
  )
}
