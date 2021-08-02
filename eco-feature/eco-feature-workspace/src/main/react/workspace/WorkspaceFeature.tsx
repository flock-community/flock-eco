import React, {useEffect, useState} from 'react'
import AddIcon from '@material-ui/icons/Add'
import Fab from '@material-ui/core/Fab'
import {Card, Theme} from '@material-ui/core'
import {WorkspaceTable} from './WorkspaceTable'
import {WorkspaceDialog} from './WorkspaceDialog'
import {createStyles, makeStyles} from '@material-ui/core/styles'

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      position: 'relative',
      height: '100%',
    },
    content: {
      maxHeight: '100%',
    },
    add: {
      position: 'absolute',
      right: theme.spacing(1),
      bottom: theme.spacing(1),
    },
  }),
)

interface DialogState {
  open: boolean
  id: string | null
}

export function WorkspaceFeature() {
  const classes = useStyles()

  const [reload, setReload] = useState<boolean>(false)
  const [dialog, setDialog] = useState<DialogState>({
    open: false,
    id: null,
  })

  useEffect(() => {
    if (!dialog.open) {
      setReload(!reload)
    }
  }, [dialog.open])

  const handleClickRow = (workspaceId: string) => {
    setDialog({
      open: true,
      id: workspaceId,
    })
  }

  const handleClickNew = () => {
    setDialog({
      open: true,
      id: null,
    })
  }

  const handleComplete = () => {
    setDialog({
      open: false,
      id: null,
    })
  }

  return (
    <>
      <div className={classes.root}>
        <div className={classes.content}>
          <Card>
            <WorkspaceTable reload={reload} onRowClick={handleClickRow} />
          </Card>
        </div>
        <Fab
          color="primary"
          aria-label="Add"
          className={classes.add}
          onClick={handleClickNew}
        >
          <AddIcon />
        </Fab>
      </div>
      <WorkspaceDialog
        open={dialog.open}
        id={dialog.id}
        onComplete={handleComplete}
      />
    </>
  )
}
