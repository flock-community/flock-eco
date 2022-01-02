import React from 'react'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import {DialogTitleClosable} from '@flock-community/flock-eco-core'

type MemberFieldDialogProps = {
  open: boolean
  onClose: () => void
  onSave: () => void
  onDelete: () => void
}

export const MemberFieldDialog: React.FC<MemberFieldDialogProps> = ({
  children,
  open,
  onClose,
  onSave,
  onDelete,
}) => {
  const handleClose = () => {
    onClose()
  }

  const handleSave = () => {
    onSave()
  }

  const handleDelete = () => {
    onDelete()
  }

  return (
    <Dialog
      fullWidth
      maxWidth={'sm'}
      open={open}
      onClose={handleClose}
      aria-labelledby="simple-dialog-title"
    >
      <DialogTitleClosable onClose={handleClose}>
        Member Field
      </DialogTitleClosable>
      <DialogContent>{children}</DialogContent>
      <DialogActions>
        {handleDelete && (
          <Button onClick={handleDelete} color="secondary">
            Delete
          </Button>
        )}
        <Button onClick={handleClose} color="primary">
          Cancel
        </Button>
        <Button
          onClick={handleSave}
          color="primary"
          variant="contained"
          autoFocus
        >
          Save
        </Button>
      </DialogActions>
    </Dialog>
  )
}
