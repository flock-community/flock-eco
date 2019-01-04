import React from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'

class MailchimpTemplateTable extends React.Component {
  state = {
    list: [],
  }

  componentDidMount() {
    this.load()
  }

  load() {
    console.log('123')
    fetch(`/api/mailchimp/templates`)
      .then(res => {
        this.setState({
          count: parseInt(res.headers.get('x-total')),
        })
        return res.json()
      })
      .then(json => {
        this.setState({list: json})
      })
      .catch(e => {
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
            <TableCell>Type</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {this.state.list.map(it => (
            <TableRow
              key={it.id}
              hover
              onClick={event => this.handleRowClick(it)}
            >
              <TableCell component="th" scope="row">
                {it.name}
              </TableCell>
              <TableCell>{it.type}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    )
  }
}

export default MailchimpTemplateTable
