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
import {ConfirmDialog} from '@flock-community/flock-eco-core/src/main/react/components/ConfirmDialog'
import {Snackbar} from '@material-ui/core'

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

export function UserDialog({open, code, onComplete, enablePassword}) {
  const classes = useStyles()

  const [state, setState] = useState(null)
  const [message, setMessage] = useState(null)
  const [openDelete, setOpenDelete] = useState(false)
  const [authorities, setAuthorities] = useState(null)

  useEffect(() => {
    if (code !== null) {
      UserClient.findUserByCode(code)
        .then(res => setState(res))
        .catch(err => {
          setMessage(err.message)
        })
    } else {
      setState(null)
    }
  }, [code])

  useEffect(() => {
    UserClient.findAllAuthorities()
      .then(setAuthorities)
      .catch(err => {
        setMessage(err.message)
      })
  }, [])

  const handleDelete = ev => {
    UserClient.deleteUser(state.code)
      .then(res => {
        onComplete && onComplete()
        setOpenDelete(false)
      })
      .catch(err => {
        setMessage(err.message)
      })
  }

  const handleOpenDelete = () => {
    setOpenDelete(true)
  }

  const handleCloseDelete = () => {
    setOpenDelete(false)
  }

  const handleMessageClose = () => {
    setMessage(null)
  }

  const handleReset = ev => {
    UserClient.resetUserPassword(state.code)
      .then(res => onComplete && onComplete())
      .catch(err => {
        setMessage(err.message)
      })
  }

  const handleClose = ev => {
    onComplete && onComplete()
  }

  const handleSubmit = value => {
    if (value.code) {
      UserClient.updateUser(value.code, value)
        .then(() => onComplete && onComplete(state))
        .catch(err => {
          setMessage(err.message)
        })
    } else {
      UserClient.createUser(value)
        .then(() => onComplete && onComplete(state))
        .catch(err => {
          setMessage(err.message)
        })
    }
  }

  return (
    <>
      <Dialog fullWidth maxWidth={'md'} open={open} onClose={handleClose}>
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
          <UserForm
            value={state}
            authorities={authorities}
            onSummit={handleSubmit}
          />
        </DialogContent>
        <DialogActions>
          {enablePassword && state && state.code && (
            <Button onClick={handleReset}>Reset password</Button>
          )}
          {state && state.code && (
            <Button onClick={handleOpenDelete}>Delete</Button>
          )}
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
      <ConfirmDialog
        open={openDelete}
        onClose={handleCloseDelete}
        onConfirm={handleDelete}
      >
        <Typography>
          Would you Are you sure you want to delete user: {state && state.name}
        </Typography>
      </ConfirmDialog>
      <Snackbar
        open={message != null}
        message={message}
        autoHideDuration={6000}
        onClose={handleMessageClose}
      />
    </>
  )
}
