import React, {useEffect, useState} from 'react'

import AddIcon from '@material-ui/icons/Add'

import {MemberFieldTable} from './MemberFieldTable'
import {MemberFieldForm} from './MemberFieldForm'
import {MemberFieldDialog} from './MemberFieldDialog'
import Fab from '@material-ui/core/Fab'
import {MemberField, MemberFieldClient} from './MemberFieldClient'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles(theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing(1),
  },
}))

type MemberFieldFeatureState = {
  list: MemberField[]
  item?: Partial<MemberField>
}

type MemberFieldFeatureProps = {
  list: MemberField[]
}

export function MemberFieldFeature({list}: MemberFieldFeatureProps) {
  const classes = useStyles()
  const [state, setState] = useState<MemberFieldFeatureState>({
    list: list || [],
  })

  useEffect(() => {
    load()
  }, [])

  const load = async () => {
    const res = await MemberFieldClient.all()
    setState(prev => ({...prev, list: res.body}))
  }

  const rowClick = item => {
    setState(prev => ({...prev, item}))
  }

  const newClick = () => {
    setState({
      ...state,
      item: {
        name: '',
        label: '',
        type: 'TEXT',
        options: [],
      },
    })
  }

  const handleFormClose = () => {
    setState(prev => ({...prev, item: undefined}))
  }

  const handleFormSave = async () => {
    if (state.item?.id) {
      await MemberFieldClient.put(state.item.id, state.item)
    } else {
      await MemberFieldClient.post(state.item)
    }
    setState(prev => ({...prev, item: undefined}))
    await load()
  }

  const handleFormDelete = async () => {
    if (state.item?.id) {
      await MemberFieldClient.delete(state.item.id)
      setState(prev => ({...prev, item: undefined}))
      await load()
    }
  }

  const handleFormUpdate = (value: MemberField) => {
    setState(prev => ({...prev, item: value}))
  }

  return (
    <div>
      <MemberFieldTable list={state.list} onRowClick={rowClick} />

      <MemberFieldDialog
        open={state.item != null}
        onClose={handleFormClose}
        onSave={handleFormSave}
        onDelete={handleFormDelete}
      >
        <MemberFieldForm value={state.item} onChange={handleFormUpdate} />
      </MemberFieldDialog>

      <Fab
        color="primary"
        aria-label="Add"
        className={classes.button}
        onClick={newClick}
      >
        <AddIcon />
      </Fab>
    </div>
  )
}
