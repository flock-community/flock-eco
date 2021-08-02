import React, {useEffect, useState} from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableFooter from '@material-ui/core/TableFooter'
import TableRow from '@material-ui/core/TableRow'
import {PageableClient} from '@flock-community/flock-eco-core'
import TablePagination from '@material-ui/core/TablePagination'

const DEFAULT_SIZE = 10

export function UserGroupTable({reload, onRowClick, size}) {
  const client = new PageableClient('/api/user-groups', {
    size: size || DEFAULT_SIZE,
  })

  const [count, setCount] = useState(0)
  const [page, setPage] = useState(0)
  const [list, setList] = useState(null)

  useEffect(() => {
    loadList()
  }, [page, reload])

  const handleChangePage = (event, page) => {
    setPage(page)
  }

  const handleRowClick = item => ev => {
    onRowClick && onRowClick(ev, item)
  }

  const loadList = () => {
    return client.findAll(page).then(data => {
      setList(data.list)
      setCount(data.total)
    })
  }

  if (!list) return null

  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Name</TableCell>
          <TableCell>Users</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {list.map(it => (
          <TableRow key={it.name} hover onClick={handleRowClick(it)}>
            <TableCell component="th" scope="row">
              {it.name}
            </TableCell>
            <TableCell>{it.users.length}</TableCell>
          </TableRow>
        ))}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TablePagination
            count={count}
            rowsPerPage={size || DEFAULT_SIZE}
            page={page}
            rowsPerPageOptions={[]}
            onChangePage={handleChangePage}
          />
        </TableRow>
      </TableFooter>
    </Table>
  )
}
