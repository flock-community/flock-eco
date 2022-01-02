import React, {useEffect, useState} from 'react'

import {MemberGroupTable} from './MemberGroupTable'
import {MemberGroupForm} from './MemberGroupForm'
import {MemberGroupDialog} from './MemberGroupDialog'
import {MemberGroup, MemberGroupClient} from './MemberGroupClient'
import {Card} from '@material-ui/core'
import {MemberGroupToolbar} from './MemberGroupToolbar'

type MemberGroupFeatureState = {
  list: MemberGroup[]
  item?: MemberGroup
}

type MemberGroupFeatureProps = {
  list: MemberGroup[]
}
export function MemberGroupFeature({list}: MemberGroupFeatureProps) {
  const [state, setState] = useState<MemberGroupFeatureState>({
    list: list || [],
  })

  useEffect(() => {
    load()
  }, [])

  const rowClick = item => {
    setState({...state, item})
  }

  const newClick = () => {
    setState({
      ...state,
      item: {
        code: '',
        name: '',
      },
    })
  }

  const handleFormClose = () => {
    setState({...state, item: undefined})
  }

  const handleFormSave = async () => {
    if (state.item?.id) {
      await MemberGroupClient.put(state.item.id, state.item)
    } else {
      await MemberGroupClient.post(state.item)
    }
    setState(prev => ({...prev, item: undefined}))
    await load()
  }

  const handleFormDelete = async () => {
    if (state.item?.id) {
      await MemberGroupClient.delete(state.item.id)
    }
    setState(prev => ({...prev, item: undefined}))
    await load()
  }

  const handleFormUpdate = value => {
    setState({...state, item: value})
  }
  const load = () => {
    return fetch('/api/member_groups')
      .then(res => res.json())
      .then(json => {
        setState({list: json})
      })
  }

  return (
    <Card>
      <MemberGroupToolbar onAdd={newClick} />
      <MemberGroupTable list={state.list} onRowClick={rowClick} />
      <MemberGroupDialog
        open={!!state.item}
        onClose={handleFormClose}
        onSave={handleFormSave}
        onDelete={handleFormDelete}
      >
        {state.item && (
          <MemberGroupForm value={state.item} onChange={handleFormUpdate} />
        )}
      </MemberGroupDialog>
    </Card>
  )
}
