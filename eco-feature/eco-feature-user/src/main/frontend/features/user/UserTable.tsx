import * as React from 'react'
import {Component} from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableFooter from '@material-ui/core/TableFooter'
import TableRow from '@material-ui/core/TableRow'
import TablePagination from '@material-ui/core/TablePagination'

import {User} from '../../models/user.model.json'

import {UserClient} from '../../clients/userClient'
import {Pageable} from '../../utils/Pagination'

interface Props<T> {
  onRowClick?(event: React.MouseEvent<HTMLTableRowElement> | null, item: T): void,
  onChangePage?(event: React.MouseEvent<HTMLButtonElement> | null, page: number): void
}


interface State<T> {
  count: number,
  page: number,
  size: number,
  list: T[],
  item: T
}

class UserTable extends Component<Props<User>, State<User>> {

  state: State<User> = {
    count: 0,
    page: 0,
    size: 10,
    list: [],
    item: null,
  }

  private handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, page: number): void => {
    this.setState({page}, () => {
      this.props.onChangePage && this.props.onChangePage(event, page)
      this.loadData()
    })

  }

  private handleRowClick = (event: React.MouseEvent<HTMLTableRowElement>, item: User): void => {
    item.reference
    if (this.props.onRowClick) return this.props.onRowClick(event, item)
  }

  private async loadData() {
    const {page, size} = this.state
    const pageable = new Pageable(page, size)
    const res = await UserClient.findUsers(pageable)

    return this.setState({
      list: res.data,
      page: res.page,
      count: res.total,
    })
  }

  componentDidMount(): void {
    this.loadData()
  }

  render() {
    const {list, page, count, size} = this.state
    return (
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Authorities</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {list.map(it => (
            <TableRow
              key={it.name}
              hover
              onClick={ev => this.handleRowClick(ev, it)}
            >
              <TableCell>{it.name}</TableCell>
              <TableCell>{it.email}</TableCell>
              <TableCell>{it.authorities.length}</TableCell>
            </TableRow>
          ))}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              count={count}
              rowsPerPage={size}
              page={page}
              rowsPerPageOptions={[]}
              onChangePage={this.handleChangePage}
            />
          </TableRow>
        </TableFooter>
      </Table>
    )
  }
}

export default UserTable
