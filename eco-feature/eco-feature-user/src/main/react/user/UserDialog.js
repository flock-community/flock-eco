import React, {useEffect, useState} from 'react'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import {USER_FORM_ID, UserForm} from './UserForm'
import UserClient from './UserClient'
import * as Yup from 'yup'

export function UserDialog({open, code, onComplete}) {
  const [state, setState] = useState(null)

  useEffect(() => {
    if(code !== null){
      UserClient.findUserByCode(code).then(res =>
        setState(res),
      )
    }else{
      setState(null)
    }

  }, [code])

  const handleDelete = ev => {
    UserClient.deleteUser(state.code)
  }

  const handleReset = ev => {
    UserClient.resetUserPassword(state.code)
  }

  const handleClose = ev => {
    onComplete && onComplete()
  }

  const handleSubmit = value => {
    if (value.code) {
      UserClient.updateUser(value.code, value)
    } else {
      UserClient.createUser(value)
    }
    onComplete && onComplete(state)
  }

  return (
    <Dialog fullWidth maxWidth={'md'} open={open} onClose={handleClose}>
      <DialogTitle>User</DialogTitle>
      <DialogContent>
        <UserForm value={state} onSummit={handleSubmit} />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleReset}>Reset secret</Button>
        <Button onClick={handleClose}>Cancel</Button>
        {state && state.code && (
          <Button onClick={handleDelete}>Delete</Button>
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
  )
}
