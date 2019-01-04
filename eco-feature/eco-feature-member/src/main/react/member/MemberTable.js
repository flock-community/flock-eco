import React from 'react'
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

const EnhancedTableToolbar = withStyles(theme => ({
  root: {
    paddingRight: theme.spacing.unit,
  },
  highlight:
    theme.palette.type === 'light'
      ? {
          color: theme.palette.secondary.main,
          backgroundColor: lighten(theme.palette.secondary.light, 0.85),
        }
      : {
          color: theme.palette.text.primary,
          backgroundColor: theme.palette.secondary.dark,
        },
  spacer: {
    flex: '1 1 100%',
  },
  actions: {
    color: theme.palette.text.secondary,
  },
  title: {
    flex: '0 0 auto',
  },
}))(({classes, onMergeMembers, selectedIds}) => {
  const numSelected = selectedIds.length
  return (
    <Toolbar
      className={classNames(classes.root, {
        [classes.highlight]: numSelected > 0,
      })}
    >
      <div className={classes.title}>
        {numSelected > 0 ? (
          <Typography color="inherit" variant="subtitle1">
            {numSelected} selected
          </Typography>
        ) : (
          <Typography variant="h6" id="tableTitle">
            Members
          </Typography>
        )}
      </div>
      <div className={classes.spacer} />
      <div className={classes.actions}>
        {numSelected >= 2 ? (
          <Tooltip title="Merge">
            <IconButton
              aria-label="Merge"
              onClick={() => onMergeMembers(selectedIds)}
            >
              <MergeIcon />
            </IconButton>
          </Tooltip>
        ) : null}
      </div>
    </Toolbar>
  )
})

class MemberTable extends React.Component {
  state = {
    data: [],
    count: 0,
    page: 0,
    size: 10,
    specification: {},
    order: 'surName',
    direction: 'asc',
    selectedIds: [],
  }

  handleChangePage = (event, page) => {
    this.setState({page: page}, this.loadData)
  }

  handleRowClick = (event, user) => {
    if (this.props.onRowClick) return this.props.onRowClick(event, user)
  }

  handleChangeSort = id => event => {
    const toggleDirection = () =>
      this.state.direction === 'desc' ? 'asc' : 'desc'
    this.setState(
      {
        order: id,
        page: 0,
        direction:
          this.state.order === id ? toggleDirection() : this.state.direction,
      },
      this.loadData,
    )
  }

  loadData = () => {
    const query = Object.entries(this.state.specification)
      .filter(entry => entry[1].length)
      .map(entry => entry[1].map(value => entry[0] + '=' + value).join('&'))
      .join('&')

    fetch(
      `/api/members?${query && query + '&'}page=${this.state.page}&size=${
        this.state.size
      }&sort=${this.state.order},${this.state.direction}`,
    )
      .then(res => {
        this.setState({
          count: parseInt(res.headers.get('x-total')),
        })
        return res.json()
      })
      .then(json => {
        this.setState({data: json})
      })
      .catch(e => {
        this.setState({message: 'Cannot load members'})
      })
  }

  componentDidMount() {
    this.loadData()
  }

  componentDidUpdate(prevProps) {
    if (prevProps.specification !== this.props.specification)
      this.setState(
        {
          specification: this.props.specification,
          page: 0,
        },
        this.loadData,
      )

    if (prevProps.refresh !== this.props.refresh) {
      this.loadData()
    }
    if (prevProps.refreshSelection !== this.props.refreshSelection)
      this.setState({
        selectedIds: [],
      })
  }

  isSelected = id => this.state.selectedIds.indexOf(id) !== -1

  onCheckboxChange = (checked, id) => {
    if (checked) {
      this.setState({selectedIds: [...this.state.selectedIds, id]})
    } else {
      this.setState({
        selectedIds: this.state.selectedIds.filter(it => it !== id),
      })
    }
  }

  render() {
    const {onMergeMembers} = this.props

    const rows = [
      {id: 'surName', label: 'Name'},
      {id: 'email', label: 'Email'},
      {id: 'created', label: 'Created'},
      {id: 'status', label: 'Status'},
    ]

    return (
      <div>
        <EnhancedTableToolbar
          onMergeMembers={onMergeMembers}
          selectedIds={this.state.selectedIds}
        />
        <Table>
          <TableHead>
            <TableRow>
              <TableCell style={{width: 25}} padding="checkbox" />
              {rows.map(row => (
                <TableCell key={row.id}>
                  <TableSortLabel
                    active={this.state.order === row.id}
                    direction={this.state.direction}
                    onClick={this.handleChangeSort(row.id)}
                  >
                    {row.label}
                  </TableSortLabel>
                </TableCell>
              ))}
            </TableRow>
          </TableHead>

          <TableBody>
            {this.state.data.map(it => (
              <TableRow
                key={it.id}
                hover
                selected={this.isSelected(it.id)}
                aria-checked={this.isSelected(it.id)}
              >
                <TableCell style={{width: 25}} padding="checkbox">
                  <Checkbox
                    onChange={e =>
                      this.onCheckboxChange(e.target.checked, it.id)
                    }
                    checked={this.isSelected(it.id)}
                  />
                </TableCell>
                <TableCell
                  onClick={_ => this.handleRowClick(it)}
                  component="th"
                  scope="row"
                >
                  {Member.toName(it)}
                </TableCell>
                <TableCell onClick={_ => this.handleRowClick(it)}>
                  {it.email}
                </TableCell>
                <TableCell onClick={_ => this.handleRowClick(it)}>
                  {it.created}
                </TableCell>
                <TableCell onClick={_ => this.handleRowClick(it)}>
                  {it.status}
                </TableCell>
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
      </div>
    )
  }
}

export default MemberTable
