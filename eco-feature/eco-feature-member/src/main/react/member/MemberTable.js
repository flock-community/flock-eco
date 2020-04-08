import React, {useEffect, useState} from 'react'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableFooter from '@material-ui/core/TableFooter'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import TablePagination from '@material-ui/core/TablePagination'
import TableSortLabel from '@material-ui/core/TableSortLabel'
import Checkbox from '@material-ui/core/Checkbox'
import * as Member from '../model/Member'
import {useQuery} from '@apollo/react-hooks'
import gql from 'graphql-tag'
import {MemberTableToolbar} from './MemberTableToolbar'
import {CircularProgress, Snackbar} from '@material-ui/core'

export const QUERY = gql`
    query ($search: String!,
        $statuses: [MemberStatus!],
        $groups:[String!],
        $page: Int!,
        $sort: String) {
        list: findAllMembers(filter:{search:$search, statuses:$statuses, groups:$groups}, page:$page, size:10, sort:$sort,) {
            id,
            firstName,
            infix,
            surName,
            email,
            created,
            status,
        }
        count:countMembers(filter:{search:$search, statuses:$statuses, groups:$groups})
    }`

export function MemberTable({specification, size, page, order, direction, onRowClick, onMergeMembers}) {

  const [state, setState] = useState({
    data: [],
    count: 0,
    page: page || 0,
    size: size || 10,
    specification: specification || {},
    order: order || 'surName',
    direction: direction || 'asc',
    selectedIds: [],
  })

  useEffect(() => {
    setState({
      ...state,
      specification: specification || {},
    })
  }, [specification])

  const {data, error, loading} = useQuery(QUERY, {
    variables: {
      search: state.specification.search || "",
      groups: state.specification.groups || [],
      statuses: state.specification.statuses || [],
      page: state.page,
      sort: `${state.order},${state.direction}`,
    },
  })

  const handleChangePage = (event, page) => {
    setState({...state, page: page})
  }

  const handleRowClick = (event, user) => {
    if (onRowClick) onRowClick(event, user)
  }

  const handleChangeSort = id => event => {
    const toggleDirection = () => state.direction === 'desc' ? 'asc' : 'desc'
    setState(
      {
        ...state,
        order: id,
        page: 0,
        direction:
          state.order === id ? toggleDirection() : state.direction,
      },
    )
  }

  const isSelected = id => state.selectedIds.indexOf(id) !== -1

  const onCheckboxChange = (checked, id) => {
    if (checked) {
      setState({
        ...state,
        selectedIds: [...state.selectedIds, id],
      })
    } else {
      setState({
        ...state,
        selectedIds: state.selectedIds.filter(it => it !== id),
      })
    }
  }

  const rows = [
    {id: 'surName', label: 'Name'},
    {id: 'email', label: 'Email'},
    {id: 'created', label: 'Created'},
    {id: 'status', label: 'Status'},
  ]

  if (loading)
    return <CircularProgress/>

  return (
    <>
      <MemberTableToolbar
        onMergeMembers={onMergeMembers}
        selectedIds={state.selectedIds}
      />
      {data && (<Table>
        <TableHead>
          <TableRow>
            <TableCell style={{width: 25}} padding="checkbox"/>
            {rows.map(row => (
              <TableCell key={row.id}>
                <TableSortLabel
                  active={state.order === row.id}
                  direction={state.direction}
                  onClick={handleChangeSort(row.id)}
                >
                  {row.label}
                </TableSortLabel>
              </TableCell>
            ))}
          </TableRow>
        </TableHead>

        <TableBody>
          {data.list.map(it => (
            <TableRow
              key={it.id}
              hover
              selected={isSelected(it.id)}
              aria-checked={isSelected(it.id)}
            >
              <TableCell style={{width: 25}} padding="checkbox">
                <Checkbox
                  onChange={e =>
                    onCheckboxChange(e.target.checked, it.id)
                  }
                  checked={isSelected(it.id)}
                />
              </TableCell>
              <TableCell
                onClick={_ => handleRowClick(it)}
                component="th"
                scope="row"
              >
                {Member.toName(it)}
              </TableCell>
              <TableCell onClick={_ => handleRowClick(it)}>
                {it.email}
              </TableCell>
              <TableCell onClick={_ => handleRowClick(it)}>
                {it.created}
              </TableCell>
              <TableCell onClick={_ => handleRowClick(it)}>
                {it.status}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>

        <TableFooter>
          <TableRow>
            <TablePagination
              count={data.count}
              rowsPerPage={state.size}
              page={state.page}
              rowsPerPageOptions={[]}
              onChangePage={handleChangePage}
            />
          </TableRow>
        </TableFooter>
      </Table>)}

      <Snackbar open={error} message={error && error.message}/>

    </>
  )
}
