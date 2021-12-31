import React, {MouseEventHandler} from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import {MemberField} from './MemberFieldClient'

type MemberTableProps = {
  list: MemberField[]
  onRowClick: (item: MemberField) => void
}
export const MemberFieldTable = ({list, onRowClick}: MemberTableProps) => {
  function handleClick(item) {
    if (!item.disabled) {
      onRowClick?.(item)
    }
  }
  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Name</TableCell>
          <TableCell>Label</TableCell>
          <TableCell>Type</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {list.map(it => (
          <TableRow
            aria-disabled={it.disabled}
            key={it.name}
            hover
            onClick={() => handleClick(it)}
          >
            <TableCell>{it.name}</TableCell>
            <TableCell>{it.label}</TableCell>
            <TableCell>{it.type}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}
