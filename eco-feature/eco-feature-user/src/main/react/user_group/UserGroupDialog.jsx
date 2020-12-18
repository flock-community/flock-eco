import React, {useEffect, useState} from 'react'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import {USER_GROUP_FORM_ID, UserGroupForm} from './UserGroupForm'
import Typography from '@material-ui/core/Typography'
import IconButton from '@material-ui/core/IconButton'
import CloseIcon from '@material-ui/icons/Close'
import makeStyles from '@material-ui/core/styles/makeStyles'
import UserGroupClient from './UserGroupClient'
import {ConfirmDialog} from '@flock-community/flock-eco-core/src/main/react/components/ConfirmDialog'
import {Snackbar} from '@material-ui/core'

const useStyles = makeStyles(theme => ({
  closeButton: {
    position: 'absolute',
    right: theme.spacing(1),
    top: theme.spacing(1),
    color: theme.palette.grey[500],
  },
  autoCompleteFix: {overflow: 'visible'},
}))

export function UserGroupDialog({open, code, onComplete}) {
  const classes = useStyles()

  const [openDelete, setOpenDelete] = useState(false)
  const [message, setMessage] = useState(null)
  const [state, setState] = useState(null)

  useEffect(() => {
    if (code !== null) {
      UserGroupClient.findByCode(code)
        .then(userGroup => setState(userGroup))
        .catch(err => {
          setMessage(err.message)
        })
    } else {
      setState(null)
    }
  }, [code])

  const handleCloseDelete = ev => {
    setOpenDelete(false)
  }

  const handleConfirmDelete = ev => {
    setOpenDelete(true)
  }

  const handleSubmit = value => {
    if (!value.id) {
      UserGroupClient.createUserGroup(value)
        .then(it => onComplete && onComplete(it))
        .catch(err => {
          setMessage(err.message)
        })
    } else {
      UserGroupClient.updateUserGroup(state.code, value)
        .then(it => onComplete && onComplete(it))
        .catch(err => {
          setMessage(err.message)
        })
    }
  }

  const handleDelete = () => {
    UserGroupClient.deleteUserGroup(state.code)
      .then(it => {
        onComplete && onComplete(it)
        setOpenDelete(false)
      })
      .catch(err => {
        setMessage(err.message)
      })
  }

  const handleMessageClose = () => {
    setMessage(null)
  }

  const handleClose = () => {
    onComplete && onComplete()
  }

  return (
    <>
      <Dialog
        classes={{paperScrollPaper: classes.autoCompleteFix}}
        fullWidth
        maxWidth={'sm'}
        open={open}
        onClose={handleClose}
      >
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
        <DialogContent className={classes.autoCompleteFix}>
          <UserGroupForm value={state} onSummit={handleSubmit} />
        </DialogContent>
        <DialogActions>
          {state && state.id && (
            <Button onClick={handleConfirmDelete}>Delete</Button>
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
      <ConfirmDialog
        open={openDelete}
        onClose={handleCloseDelete}
        onConfirm={handleDelete}
      >
        <Typography>
          Would you Are you sure you want to delete group: {state && state.name}
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
