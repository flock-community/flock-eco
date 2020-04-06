import React, {useEffect, useState} from 'react'
import classNames from 'classnames'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableFooter from '@material-ui/core/TableFooter'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import TablePagination from '@material-ui/core/TablePagination'
import TableSortLabel from '@material-ui/core/TableSortLabel'
import Checkbox from '@material-ui/core/Checkbox'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import Tooltip from '@material-ui/core/Tooltip'
import {withStyles} from '@material-ui/core/styles'
import IconButton from '@material-ui/core/IconButton'
import MergeIcon from '@material-ui/icons/CallMerge'
import {lighten} from '@material-ui/core/es/styles/colorManipulator'
import * as Member from '../model/Member'
import {MemberClient} from './MemberClient'
import {useQuery} from '@apollo/react-hooks'
import gql from 'graphql-tag'
import {MemberTableToolbar} from './MemberTableToolbar'
import {Snackbar} from '@material-ui/core'


export const QUERY = gql`
    query ($specification: MemberSpecification!, $page: Int!, $sort: String) {
        list: findAllMembers(specification:$search, page:$page, size:10, sort:$sort,) {
            id,
            firstName,
            infix,
            surName,
            email,
            created,
            status,
        }
        count:countMembers
    }`

export function MemberTable(props) {

  const [state, setState] = useState({
    data: [],
    count: 0,
    page: 0,
    size: props.size || 10,
    specification: props.specification || {},
    order: 'surName',
    direction: 'asc',
    selectedIds: [],
  })

  const {data, error, loading} = useQuery(QUERY, {variables: {
    specification: state.specification ,
    page: state.page,
    sort: `${state.order},${state.direction}`,
  }})

  console.log(error)

  const handleChangePage = (event, page) => {
    setState({...state, page: page})
  }

  const handleRowClick = (event, user) => {
    if (props.onRowClick) props.onRowClick(event, user)
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

  if(loading)
    return null

  return (
    <>
      <MemberTableToolbar
        onMergeMembers={props.onMergeMembers}
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

      <Snackbar open={error} message={error}/>

    </>
  )
}
