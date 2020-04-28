import React, {useEffect, useState} from 'react'
import {Button, Dialog, DialogActions, DialogContent} from '@material-ui/core'
import {WORKSPACE_FORM_ID, WorkspaceForm} from './WorkspaceForm'
import {WorkspaceClient, WorkspaceInput} from './WorkspaceClient'

interface Props {
  id: string | null,
  open: boolean
  onComplete(): void
}

export function WorkspaceDialog({id, open, onComplete}: Props) {

  const [state, setState] = useState<WorkspaceInput | null>(null)

  useEffect(() => {
    if (id) {
      WorkspaceClient.get(id)
        .then(workspace => {
          setState(workspace)
        })
    } else {
      setState(null)
    }
  }, [id])

  const handleSubmit = (input: WorkspaceInput) => {
    if (id) {
      WorkspaceClient.put(id, input)
        .then(() => onComplete())
    } else {
      WorkspaceClient.post(input)
        .then(() => onComplete())
    }
  }

  const handleDelete = () => {
    if (id) {
      WorkspaceClient.delete(id)
        .then(() => onComplete())
    }
  }

  const handleClose = () => {
    onComplete()
  }

  return (<Dialog
    open={open}
    onClose={handleClose}>
    <DialogContent>
      <WorkspaceForm
        value={state}
        onSubmit={handleSubmit}
      />
    </DialogContent>
    <DialogActions>
      {id && <Button onClick={handleDelete}>Delete</Button>}
      <Button type="submit" form={WORKSPACE_FORM_ID}>Save</Button>
    </DialogActions>
  </Dialog>)
}
