import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import AddIcon from '@material-ui/icons/Add'

import MemberGroupTable from './MemberGroupTable'
import MemberGroupForm from './MemberGroupForm'
import MemberGroupDialog from './MemberGroupDialog'
import Fab from '@material-ui/core/Fab'

const styles = theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing(1),
  },
})

class MemberGroupFeature extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      list: props.list || [],
    }

    this.rowClick = item => {
      this.setState({item})
    }

    this.newClick = () => {
      this.setState({item: {}})
    }

    this.handleFormClose = () => {
      this.setState({item: null})
    }

    this.handleFormSave = () => {
      if (this.state.item.id) {
        this.update(this.state.item)
      } else {
        this.create(this.state.item)
      }
    }

    this.handleFormDelete = () => {
      this.delete(this.state.item)
    }

    this.handleFormUpdate = value => {
      this.setState({item: value})
    }

    this.load()
  }

  render() {
    const {classes} = this.props

    return (
      <div>
        <MemberGroupTable
          list={this.state.list}
          handleRowClick={this.rowClick}
        />

        <MemberGroupDialog
          open={this.state.item != null}
          onClose={this.handleFormClose}
          onSave={this.handleFormSave}
          onDelete={this.handleFormDelete}
        >
          <MemberGroupForm
            item={this.state.item}
            onChange={this.handleFormUpdate}
          />
        </MemberGroupDialog>

        <Fab
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.newClick}
        >
          <AddIcon />
        </Fab>
      </div>
    )
  }

  load() {
    return fetch('/api/member_groups')
      .then(res => res.json())
      .then(json => {
        this.setState({list: json})
      })
  }

  create() {
    const opts = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(this.state.item),
    }
    fetch('/api/member_groups', opts).then(() => {
      this.setState({item: null})
      this.load()
    })
  }

  update() {
    const opts = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(this.state.item),
    }
    fetch(`/api/member_groups/${this.state.item.id}`, opts).then(() => {
      this.setState({item: null})
      this.load()
    })
  }

  delete() {
    const opts = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
    }
    fetch(`/api/member_groups/${this.state.item.id}`, opts).then(() => {
      this.setState({item: null})
      this.load()
    })
  }
}

export default withStyles(styles)(MemberGroupFeature)
