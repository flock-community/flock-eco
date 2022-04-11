import React, {useEffect, useState} from 'react'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import TablePagination from '@material-ui/core/TablePagination'
import TableSortLabel from '@material-ui/core/TableSortLabel'
import Checkbox from '@material-ui/core/Checkbox'
import * as Member from '../model/Member'
import {useQuery} from '@apollo/react-hooks'
import gql from 'graphql-tag'
import {Snackbar} from '@material-ui/core'
import {AlignedLoader} from '@flock-community/flock-eco-core/src/main/react/components/AlignedLoader'
import Card from '@material-ui/core/Card'
import TableContainer from '@material-ui/core/TableContainer'
import {Direction, Specification} from './MemberModel'

export const QUERY = gql`
  query (
    $search: String!
    $statuses: [MemberStatus!]
    $groups: [String!]
    $page: Int!
    $sort: String
    $order: String
  ) {
    list: findAllMembers(
      filter: {search: $search, statuses: $statuses, groups: $groups}
      page: $page
      size: 10
      sort: $sort
      order: $order
    ) {
      id
      firstName
      infix
      surName
      email
      created
      status
    }
    count: countMembers(
      filter: {search: $search, statuses: $statuses, groups: $groups}
    )
  }
`

type MemberTableState = {
  page: number
  size: number
  specification: Specification
  order: string
  direction: Direction
  selectedIds: string[]
}

type MemberTableProps = {
  specification?: Specification
  refresh?: boolean
  size?: number
  page?: number
  order?: string
  direction?: Direction
  onRowClick?: (event: any) => void
  onRowSelect?: (ids: string[]) => void
}

export function MemberTable({
  specification,
  refresh,
  size,
  page,
  order,
  direction,
  onRowClick,
  onRowSelect,
}: MemberTableProps) {
  const initSpecification: Specification = {
    search: '',
    groups: [],
    statuses: [],
  }
  const initState: MemberTableState = {
    page: page || 0,
    size: size || 10,
    specification: specification || initSpecification,
    order: order || 'surName',
    direction: direction || 'asc',
    selectedIds: [],
  }

  const [state, setState] = useState<MemberTableState>(initState)

  const {data, error, loading, refetch} = useQuery(QUERY, {
    variables: {
      search: state.specification.search || '',
      groups: state.specification.groups || [],
      statuses: state.specification.statuses || [],
      page: state.page,
      sort: state.order,
      order: state.direction,
    },
    fetchPolicy: 'no-cache',
  })

  useEffect(() => {
    refetch()
    setState((prev) => ({
      ...prev,
      selectedIds: [],
    }))
  }, [refresh])

  useEffect(() => {
    setState((prev) => ({
      ...prev,
      page: page || 0,
    }))
  }, [page])

  useEffect(() => {
    setState((prev) => ({
      ...prev,
      page: 0,
      specification: specification || initSpecification,
    }))
  }, [specification])

  const handleChangePage = (event, page) => {
    setState({...state, page: page})
  }

  const handleRowClick = (event: any) => {
    if (onRowClick) onRowClick(event)
  }

  const handleChangeSort = (id) => (event) => {
    const toggleDirection = () => (state.direction === 'desc' ? 'asc' : 'desc')
    setState({
      ...state,
      order: id,
      page: 0,
      direction: state.order === id ? toggleDirection() : state.direction,
    })
  }

  const isSelected = (id) => state.selectedIds.indexOf(id) !== -1

  const onCheckboxChange = (checked: boolean, id: string) => {
    if (checked) {
      const selectedIds = [...state.selectedIds, id] || []
      setState({
        ...state,
        selectedIds,
      })
      onRowSelect?.(selectedIds)
    } else {
      const selectedIds = state.selectedIds.filter((it) => it !== id)
      setState({
        ...state,
        selectedIds,
      })
      onRowSelect?.(selectedIds)
    }
  }

  const rows = [
    {id: 'surName', label: 'Name'},
    {id: 'email', label: 'Email'},
    {id: 'created', label: 'Created'},
    {id: 'status', label: 'Status'},
  ]

  if (loading)
    return (
      <Card>
        <AlignedLoader height={250} />
      </Card>
    )

  return (
    <TableContainer>
      {data && (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell style={{width: 25}} padding="checkbox" />
              {rows.map((row) => (
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
            {data.list.map((it) => (
              <TableRow
                key={it.id}
                hover
                selected={isSelected(it.id)}
                aria-checked={isSelected(it.id)}
              >
                <TableCell style={{width: 25}} padding="checkbox">
                  <Checkbox
                    onChange={(e) => onCheckboxChange(e.target.checked, it.id)}
                    checked={isSelected(it.id)}
                  />
                </TableCell>
                <TableCell
                  onClick={(_) => handleRowClick(it)}
                  component="th"
                  scope="row"
                >
                  {Member.toName(it)}
                </TableCell>
                <TableCell onClick={(_) => handleRowClick(it)}>
                  {it.email}
                </TableCell>
                <TableCell onClick={(_) => handleRowClick(it)}>
                  {it.created}
                </TableCell>
                <TableCell onClick={(_) => handleRowClick(it)}>
                  {it.status}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      <TablePagination
        count={data?.count}
        rowsPerPage={state.size}
        page={state.page}
        rowsPerPageOptions={[]}
        onPageChange={handleChangePage}
        component="div"
      />

      <Snackbar open={!!error} message={error && error.message} />
    </TableContainer>
  )
}
