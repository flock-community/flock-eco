import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import Button from '@material-ui/core/Button'

import AddIcon from '@material-ui/icons/Add'

import UserTable from './UserTable'
import UserForm from './UserForm'
import UserDialog from './UserDialog'
import Paper from '@material-ui/core/es/Paper/Paper'
import Fab from '@material-ui/core/Fab'

const styles = theme => ({
  tablePaper: {
    marginBottom: 50,
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing.unit,
  },
})

class UserFeature extends React.Component {
  state = {
    size: 10,
    members: this.props.members || [],
    item: null,
    groups: [],
    count: 0,
    page: 0,
  }

  componentDidMount() {
    fetch('/api/authorities')
      .then(res => res.json())
      .then(json => {
        this.setState({authorities: json})
      })
    this.load()
  }

  handleChangePage = (event, page) => {
    this.setState({page}, () => {
      this.load()
    })
  }

  handleRowClick = item => {
    console.log('123')
    this.setState({item})
  }

  handleNewClick = () => {
    this.setState({item: {}})
  }

  handleClose = value => {
    this.setState({item: null})
  }

  handleSave = value => {
    if (this.state.item.id) {
      this.update(this.state.item)
    } else {
      this.create(this.state.item)
    }
  }

  handleDelete = value => {
    this.delete(this.state.item)
  }

  handleFormUpdate = value => {
    this.setState({item: value})
  }

  load = () => {
    fetch(`/api/users?page=${this.state.page}&size=${this.state.size}`)
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
        this.setState({message: 'Cannot load users'})
      })
  }

  render() {
    const {classes} = this.props

    return (
      <div>
        <Paper className={classes.tablePaper}>
          <UserTable
            data={this.state.list}
            count={this.state.count}
            page={this.state.page}
            size={this.state.size}
            onRowClick={this.handleRowClick}
            onChangePage={this.handleChangePage}
          />
        </Paper>

        <UserDialog
          open={this.state.item != null}
          onClose={this.handleClose}
          onSave={this.handleSave}
          onDelete={this.handleDelete}
        >
          <UserForm
            authorities={this.state.authorities}
            item={this.state.item}
            onChange={this.handleFormUpdate}
          />
        </UserDialog>

        <Fab
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.handleNewClick}
        >
          <AddIcon/>
        </Fab>
      </div>
    )
  }

  create(item) {
    const opts = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(item),
    }
    fetch('/api/users', opts).then(() => {
      this.setState({item: null})
      this.load()
    })
  }

  update(item) {
    const opts = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(item),
    }
    fetch(`/api/users/${item.id}`, opts).then(() => {
      this.setState({item: null})
      this.load()
    })
  }

  delete(item) {
    const opts = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
    }
    fetch(`/api/users/${item.id}`, opts).then(() => {
      this.setState({item: null})
      this.load()
    })
  }
}

export default withStyles(styles)(UserFeature)
