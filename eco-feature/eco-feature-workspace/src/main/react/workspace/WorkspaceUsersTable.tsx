import React, {useEffect} from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import gql from 'graphql-tag'
import {useQuery} from '@apollo/react-hooks'
import {WorkspaceUserTableQuery, WorkspaceUserTableQueryVariables} from '../Apollo'
// @ts-ignore
import {AlignedLoader} from '@flock-eco/core/src/main/react/components/AlignedLoader'

interface User {
  id: string,
  name: string
  role: string
}

interface Props {
  value: User[]
  onRowClick?(workspaceUserId: String): void
}

interface Fields {
  key: keyof User,
  label: string
}

export function WorkspaceUserTable({value, onRowClick}: Props) {

  const handleRowClick = (it: string) => () => {
    onRowClick && onRowClick(it)
  }

  if (!value)
    return (<AlignedLoader/>)

  const fields: Fields[] = [
    {key: 'name', label: 'Name'},
    {key: 'role', label: 'Role'},
  ]

  const renderTable = (list: User[]) => (
    <Table>
      <TableHead>
        <TableRow>
          {fields.map(field => (<TableCell key={field.key}>
            {field.label}
          </TableCell>))}
        </TableRow>
      </TableHead>
      <TableBody>
        {list.map(it => (
          <TableRow key={it.name} hover onClick={handleRowClick(it.id)}>
            {fields.map(field => (<TableCell key={field.key}>{it[field.key]}</TableCell>))}
          </TableRow>
        ))}
      </TableBody>
    </Table>)

  return renderTable(value)
}
