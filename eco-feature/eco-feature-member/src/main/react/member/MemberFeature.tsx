import React, {useState} from 'react'

import Card from '@material-ui/core/Card'
import {MemberTable} from './MemberTable'
import {MemberDialog} from './MemberDialog'
import {MemberMerger} from './MemberMerger'
import {MemberToolbar} from './MemberToolbar'
import {Specification} from './MemberModel'

type MemberFeatureState = {
  page: number
  size: number
  openDialog: boolean
  item: any | null
  search: string
  filter: Pick<Specification, 'statuses' | 'groups'>
  refresh: boolean
  mergeMemberIds: string[] | null
  specification: Specification
  selectedIds: string[]
}
type MemberFeatureProps = {
  search: string
}
export function MemberFeature({search}: MemberFeatureProps) {
  const init = {
    search,
    statuses: [],
    groups: [],
  }
  const [state, setState] = useState<MemberFeatureState>({
    page: 0,
    size: 10,
    openDialog: false,
    item: null,
    search: search || '',
    filter: {
      statuses: [],
      groups: [],
    },
    refresh: false,
    mergeMemberIds: null,
    specification: init,
    selectedIds: [],
  })

  const handleRowClick = item => {
    setState({
      ...state,
      item,
      openDialog: true,
    })
  }

  const handleRowSelect = selectedIds => {
    setState({
      ...state,
      selectedIds,
    })
  }

  const handleNewClick = () => {
    setState(prev => ({
      ...prev,
      item: null,
      openDialog: true,
    }))
  }

  const handleFilterChange = filter => {
    setState(prev => ({
      ...prev,
      page: 0,
      specification: {
        ...prev.specification,
        ...filter,
      },
    }))
  }

  const handleComplete = () => {
    setState(prev => ({
      ...prev,
      item: null,
      openDialog: false,
      refresh: !prev.refresh,
      mergeMemberIds: null,
    }))
  }

  const handleMergeComplete = () =>
    setState(prev => ({
      ...prev,
      mergeMemberIds: null,
      refresh: !prev.refresh,
    }))

  const mergeMembers = mergeMemberIds =>
    setState(prev => ({
      ...prev,
      mergeMemberIds,
    }))

  const handleMergerCancel = () =>
    setState(prev => ({
      ...prev,
      mergeMemberIds: null,
    }))

  return (
    <>
      <Card>
        <MemberToolbar
          specification={init}
          onMergeMembers={mergeMembers}
          selectedIds={state.selectedIds}
          onAddMembers={handleNewClick}
          onSpecificationChange={handleFilterChange}
        />
        <MemberTable
          specification={state.specification}
          page={state.page}
          size={state.size}
          refresh={state.refresh}
          onRowClick={handleRowClick}
          onRowSelect={handleRowSelect}
        />
      </Card>

      <MemberDialog
        id={state.item && state.item.id}
        open={state.openDialog}
        onComplete={handleComplete}
      />

      <MemberMerger
        mergeMemberIds={state.mergeMemberIds}
        onComplete={handleMergeComplete}
        onCancel={handleMergerCancel}
      />
    </>
  )
}
