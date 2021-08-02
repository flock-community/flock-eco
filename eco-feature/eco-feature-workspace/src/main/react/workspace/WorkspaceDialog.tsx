import React, {ChangeEvent, useEffect, useState} from 'react'
import {
  AppBar,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  Tab,
  Tabs,
  Typography,
} from '@material-ui/core'
import {WORKSPACE_FORM_ID, WorkspaceForm} from './WorkspaceForm'
import {WorkspaceClient} from './WorkspaceClient'
import {WorkspaceUserTable} from './WorkspaceUsersTable'
import {WorkspaceUsersForm} from './WorkspaceUsersForm'

import {ConfirmDialog} from '@flock-community/flock-eco-core'
import {Workspace, WorkspaceInput} from '../graphql/workspace'

interface Props {
  id: string | null
  open: boolean

  onComplete(): void
}

export function WorkspaceDialog({id, open, onComplete}: Props) {
  const [tab, setTab] = useState<number>(0)
  const [state, setState] = useState<Workspace | null>(null)
  const [deleteOpen, setDeleteOpen] = useState<boolean>(false)

  useEffect(() => {
    if (id) {
      loadState(id)
    } else {
      setState(null)
    }
  }, [id])

  const loadState = (id: string) => {
    WorkspaceClient.get(id).then(workspace => {
      setState(workspace.body)
    })
  }

  const handleSubmit = (input: WorkspaceInput) => {
    if (id) {
      WorkspaceClient.put(id, input).then(() => onComplete())
    } else {
      WorkspaceClient.post(input).then(() => onComplete())
    }
  }

  const handleDelete = () => {
    if (id) {
      WorkspaceClient.delete(id).then(() => {
        onComplete()
        setDeleteOpen(false)
      })
    }
  }

  const handleOpenDelete = () => {
    setDeleteOpen(true)
  }

  const handleCloseDelete = () => {
    setDeleteOpen(false)
  }

  const handleChangeTab = (event: ChangeEvent<{}>, newValue: number) => {
    setTab(newValue)
  }

  const handleClose = () => {
    onComplete()
  }

  const handleCompleteUserForm = () => {
    if (id) {
      loadState(id)
    }
  }

  return (
    <>
      <Dialog fullWidth open={open} onClose={handleClose}>
        <AppBar position="static">
          <Tabs value={tab} onChange={handleChangeTab}>
            <Tab label="Form" />
            {id && <Tab label="Users" />}
          </Tabs>
        </AppBar>
        <DialogContent hidden={tab !== 0}>
          <WorkspaceForm value={state} onSubmit={handleSubmit} />
        </DialogContent>
        <DialogContent hidden={tab !== 1}>
          {id && (
            <WorkspaceUsersForm id={id} onComplete={handleCompleteUserForm} />
          )}
          {state && <WorkspaceUserTable value={state.users} />}
        </DialogContent>
        <DialogActions>
          {id && <Button onClick={handleOpenDelete}>Delete</Button>}
          <Button type="submit" form={WORKSPACE_FORM_ID}>
            Save
          </Button>
        </DialogActions>
      </Dialog>

      <ConfirmDialog
        open={deleteOpen}
        onClose={handleCloseDelete}
        onConfirm={handleDelete}
      >
        <Typography>Delete workspace: {state && state.id}</Typography>
      </ConfirmDialog>
    </>
  )
}
