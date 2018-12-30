import React from "react";

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableFooter from '@material-ui/core/TableFooter';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TablePagination from '@material-ui/core/TablePagination';
import TableSortLabel from '@material-ui/core/TableSortLabel';

class MemberTable extends React.Component {

  state = {
    data: [],
    count: 0,
    page: 0,
    size: 10,
    specification: {},
    order: 'surName',
    direction: 'asc',
  }

  handleChangePage = (event, page) => {
    this.setState({page: page}, this.loadData)
  }

  handleRowClick = (event, user) => {
    if (this.props.onRowClick)
      return this.props.onRowClick(event, user)
  }

  handleChangeSort = (id) => (event) => {
    const toggleDirection = () => this.state.direction === 'desc' ? 'asc' : 'desc'
    this.setState({
      order: id,
      page: 0,
      direction: this.state.order === id ? toggleDirection() : this.state.direction,
    }, this.loadData)
  }

  loadData = () => {
    const query = Object.entries(this.state.specification)
      .filter(entry => entry[1].length)
      .map(entry => entry[1]
        .map(value => entry[0] + '=' + value)
        .join('&'))
      .join('&')

    fetch(`/api/members?${query && query + '&'}page=${this.state.page}&size=${this.state.size}&sort=${this.state.order},${this.state.direction}`)
      .then(res => {
        this.setState({
          count: parseInt(res.headers.get('x-total'))
        })
        return res.json()
      })
      .then(json => {
        this.setState({data: json});
      })
      .catch(e => {
        this.setState({message: "Cannot load members"})
      })
  }

  componentDidMount() {
    this.loadData();
  }

  componentDidUpdate(prevProps) {
    if (prevProps.specification !== this.props.specification)
      this.setState({
        specification: this.props.specification,
        page: 0,
      }, this.loadData)

    if (prevProps.refresh !== this.props.refresh)
      this.loadData()

  }

  render() {

    const {page} = this.props;

    const rows = [
      {id: 'surName', label: 'Name'},
      {id: 'email', label: 'Email'},
      {id: 'created', label: 'Created'},
      {id: 'status', label: 'Status'},
    ]

    return (
      <Table>
        <TableHead>
          <TableRow>
            {rows.map(row => (<TableCell key={row.id}>
              <TableSortLabel
                active={this.state.order === row.id}
                direction={this.state.direction}
                onClick={this.handleChangeSort(row.id)}
              >{row.label}</TableSortLabel>
            </TableCell>))}
          </TableRow>
        </TableHead>

        <TableBody>{this.state.data.map(it => (
          <TableRow
            key={it.id}
            hover
            onClick={event => this.handleRowClick(it)}
          >
            <TableCell component="th" scope="row">{this.memberToName(it)}</TableCell>
            <TableCell>{it.email}</TableCell>
            <TableCell>{it.created}</TableCell>
            <TableCell>{it.status}</TableCell>
          </TableRow>
        ))}
        </TableBody>

        <TableFooter>
          <TableRow>
            <TablePagination
              count={this.state.count}
              rowsPerPage={this.props.size}
              page={this.state.page}
              rowsPerPageOptions={[]}
              onChangePage={this.handleChangePage}
            />
          </TableRow>
        </TableFooter>
      </Table>
    )
  }


  memberToName(it) {
    if (it.infix) {
      return `${it.firstName} ${it.infix} ${it.surName}`
    }
    return `${it.firstName} ${it.surName}`
  }

}

export default MemberTable;