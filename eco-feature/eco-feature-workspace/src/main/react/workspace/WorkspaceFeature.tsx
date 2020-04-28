import React, {useEffect, useState} from 'react'
import AddIcon from '@material-ui/icons/Add'
import Fab from '@material-ui/core/Fab'
import {Card} from '@material-ui/core'
import makeStyles from '@material-ui/core/styles/makeStyles'
import {WorkspaceTable} from './WorkspaceTable'
import {WorkspaceDialog} from './WorkspaceDialog'
import {Workspace} from './WorkspaceClient'

const useStyles = makeStyles(theme => ({
  root: {
    paddingBottom: theme.spacing(8),
  },
  add: {
    position: 'absolute',
    right: theme.spacing(1),
    bottom: theme.spacing(1),
  },
}))

interface DialogState {
  open: boolean,
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
    if(dialog.open === false){
      setReload(!reload)
    }
  },[dialog.open])

  const handleClickRow = (item:Workspace) => {
    setDialog({
      open: true,
      id: item.id,
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
    <div className={classes.root}>
      <Card>
        <WorkspaceTable
          reload={reload}
          onRowClick={handleClickRow}/>
      </Card>


      <WorkspaceDialog
        open={dialog.open}
        id={dialog.id}
        onComplete={handleComplete}
      />

      <Fab
        color="primary"
        aria-label="Add"
        className={classes.add}
        onClick={handleClickNew}
      >
        <AddIcon/>
      </Fab>
    </div>
  )
}
