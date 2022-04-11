import React from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import TableFooter from '@material-ui/core/TableFooter'
import TablePagination from '@material-ui/core/TablePagination'

class MailchimpMemberTable extends React.Component {
  state = {
    count: 0,
    page: 0,
    size: 10,
    list: [],
  }

  componentDidMount() {
    this.loadData()
  }

  handleChangePage = (event, page) => {
    this.setState({page: page}, this.loadData)
  }

  loadData() {
    fetch(
      `/api/mailchimp/members?page=${this.state.page}&size=${this.state.size}`,
    )
      .then((res) => {
        this.setState({
          count: parseInt(res.headers.get('x-total')),
        })
        return res.json()
      })
      .then((json) => {
        this.setState({list: json})
      })
      .catch((e) => {
        this.setState({message: 'Cannot load members'})
      })
  }

  handleRowClick = (event, item) => {
    if (this.props.onRowClick) return this.props.onRowClick(event, item)
  }

  render() {
    return (
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Tags</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {this.state.list.map((it) => (
            <TableRow
              key={it.id}
              hover
              onClick={(event) => this.handleRowClick(it)}
            >
              <TableCell component="th" scope="row">
                {it.fields.FNAME} {it.fields.LNAME}
              </TableCell>
              <TableCell>{it.email}</TableCell>
              <TableCell>{it.status}</TableCell>
              <TableCell>{it.tags.join(', ')}</TableCell>
            </TableRow>
          ))}
        </TableBody>

        <TableFooter>
          <TableRow>
            <TablePagination
              count={this.state.count}
              rowsPerPage={this.state.size}
              page={this.state.page}
              rowsPerPageOptions={[]}
              onChangePage={this.handleChangePage}
            />
          </TableRow>
        </TableFooter>
      </Table>
    )
  }
}

export default MailchimpMemberTable
