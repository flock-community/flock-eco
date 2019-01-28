import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import Paper from '@material-ui/core/Paper'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableFooter from '@material-ui/core/TableFooter'
import TableRow from '@material-ui/core/TableRow'
import TablePagination from '@material-ui/core/TablePagination'

const styles = theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing.unit,
  },
})

class GcpRuntimeVariableTable extends React.Component {

  state = {
    list: []
  }
  componentDidMount() {
    this.load()
  }

  load = () => {
    const {config} = this.props
    fetch(`api/gcp/runtimeconfig/configs/${config}/variables`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({list: json})
      })
  }


  render() {
    const {list} = this.state

    return (
      <Paper>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Key</TableCell>
              <TableCell>Value</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {list.map(it => (
              <TableRow
                key={it.name}
                hover
                onClick={event => this.handleRowClick(it)}
              >
                <TableCell component="th" scope="row">
                  {it.key}
                </TableCell>
                <TableCell>{it.value}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    )
  }

}

export default withStyles(styles)(GcpRuntimeVariableTable)
