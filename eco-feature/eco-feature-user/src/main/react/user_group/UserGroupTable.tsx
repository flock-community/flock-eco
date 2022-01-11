import React, {useEffect, useState} from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableFooter from '@material-ui/core/TableFooter'
import TableRow from '@material-ui/core/TableRow'
import {PageableClient} from '@flock-community/flock-eco-core'
import TablePagination from '@material-ui/core/TablePagination'
import UserGroupClient from './UserGroupClient'
import {UserGroup} from '../graphql/user'

const DEFAULT_SIZE = 10

type UserGroupTableProps = {
  reload?: boolean
  onRowClick?: (item: UserGroup) => void
  size?: number
}
export function UserGroupTable({
  reload,
  onRowClick,
  size,
}: UserGroupTableProps) {
  const [count, setCount] = useState(0)
  const [page, setPage] = useState<number>(0)
  const [list, setList] = useState(null)

  const pageable = {
    page: page,
    size: size ?? DEFAULT_SIZE,
    sort: 'name',
  }

  useEffect(() => {
    loadList()
  }, [page, reload])

  const handleChangePage = (event, page) => {
    setPage(page)
  }

  const handleRowClick = item => () => {
    onRowClick?.(item)
  }

  const loadList = () => {
    return UserGroupClient.findAllByPage(pageable).then(data => {
      setList(data.list)
      setCount(data.count)
    })
  }

  if (!list) return null

  return (
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Name</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {list.map(it => (
          <TableRow key={it.name} hover onClick={handleRowClick(it)}>
            <TableCell component="th" scope="row">
              {it.name}
            </TableCell>
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
            onPageChange={handleChangePage}
          />
        </TableRow>
      </TableFooter>
    </Table>
  )
}
