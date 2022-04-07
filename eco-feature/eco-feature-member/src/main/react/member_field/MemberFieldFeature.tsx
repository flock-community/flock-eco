import React, {useEffect, useState} from 'react'

import {MemberFieldTable} from './MemberFieldTable'
import {MemberFieldForm} from './MemberFieldForm'
import {MemberFieldDialog} from './MemberFieldDialog'
import {MemberField, MemberFieldClient} from './MemberFieldClient'
import {MemberFieldToolbar} from './MemberFieldToolbar'
import {Card} from '@material-ui/core'

type MemberFieldFeatureState = {
  list: MemberField[]
  item?: MemberField
}

type MemberFieldFeatureProps = {
  list: MemberField[]
}

export function MemberFieldFeature({list}: MemberFieldFeatureProps) {
  const [state, setState] = useState<MemberFieldFeatureState>({
    list: list || [],
  })

  useEffect(() => {
    load()
  }, [])

  const load = async () => {
    const res = await MemberFieldClient.all()
    setState((prev) => ({...prev, list: res.body}))
  }

  const rowClick = (item) => {
    setState((prev) => ({...prev, item}))
  }

  const newClick = () => {
    setState({
      ...state,
      item: {
        name: '',
        label: '',
        type: 'TEXT',
        options: [],
        disabled: false,
        required: false,
      },
    })
  }

  const handleFormClose = () => {
    setState((prev) => ({...prev, item: undefined}))
  }

  const handleFormSave = async () => {
    const id = state?.item?.id
    console.log(state)
    if (id) {
      await MemberFieldClient.put(id, state.item)
    } else {
      await MemberFieldClient.post(state.item)
    }
    setState((prev) => ({...prev, item: undefined}))
    await load()
  }

  const handleFormDelete = async () => {
    if (state.item?.id) {
      await MemberFieldClient.delete(state.item?.id)
      setState((prev) => ({...prev, item: undefined}))
      await load()
    }
  }

  const handleFormUpdate = (value: MemberField) => {
    setState((prev) => ({...prev, item: value}))
  }

  return (
    <Card>
      <MemberFieldToolbar onAdd={newClick} />
      <MemberFieldTable list={state.list} onRowClick={rowClick} />
      <MemberFieldDialog
        open={state.item != null}
        onClose={handleFormClose}
        onSave={handleFormSave}
        onDelete={handleFormDelete}
      >
        {state.item && (
          <MemberFieldForm value={state.item} onChange={handleFormUpdate} />
        )}
      </MemberFieldDialog>
    </Card>
  )
}
