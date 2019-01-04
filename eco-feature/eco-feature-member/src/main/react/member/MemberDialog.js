import React from 'react'
import {withStyles} from '@material-ui/core/styles'

import Button from '@material-ui/core/Button'

import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'

import Snackbar from '@material-ui/core/Snackbar'

import {ValidatorForm} from 'react-material-ui-form-validator'

import MemberForm from './MemberForm'

const styles = theme => ({})

class MemberDialog extends React.Component {
  state = {
    item: null,
    message: null,
    action: null,
  }

  componentDidUpdate(prevProps) {
    if (prevProps.action !== this.props.action) {
      const action = this.props.action && this.props.action.toUpperCase()
      this.setState({
        action: action,
      })
    }

    if (prevProps.id !== this.props.id) {
      if (this.props.id === null) this.setState({item: {}})
      else
        fetch(`/api/members/${this.props.id}`)
          .then(res => {
            return res.json()
          })
          .then(json => {
            this.setState({item: json})
          })
          .catch(e => {
            this.setState({message: 'Cannot load groups'})
          })
    }
  }

  componentDidMount() {
    fetch(`/api/member_groups`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({groups: json})
      })
      .catch(e => {
        this.setState({message: 'Cannot load groups'})
      })

    fetch(`/api/member_fields`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({fields: json})
      })
      .catch(e => {
        this.setState({message: 'Cannot load fields'})
      })
  }

  handleClose = () => {
    this.props.onComplete()
  }

  handleDelete = () => {
    const opts = {
      method: 'DELETE',
    }
    fetch(`/api/members/${this.state.item.id}`, opts).then(res => {
      if (!res.ok) {
        res.json().then(e => {
          this.setState({message: e.message || 'Cannot delete member'})
        })
      }
      this.props.onComplete()
    })
  }

  handleSave = () => {
    if (this.state.action === 'EDIT') {
      const opts = {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json; charset=utf-8',
        },
        body: JSON.stringify(this.state.item),
      }
      fetch(`/api/members/${this.state.item.id}`, opts).then(res => {
        if (!res.ok) {
          res.json().then(e => {
            this.setState({message: e.message || 'Cannot update member'})
          })
        }
        this.props.onComplete()
      })
    }
    if (this.state.action === 'NEW') {
      const opts = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json; charset=utf-8',
        },
        body: JSON.stringify(this.state.item),
      }
      fetch('/api/members', opts).then(res => {
        if (!res.ok) {
          res.json().then(e => {
            this.setState({message: e.message || 'Cannot create member'})
          })
        }
        this.props.onComplete()
      })
    }
  }

  handleFormUpdate = value => {
    this.setState({item: value})
  }

  handleSubmit = () => {
    this.handleSave()
  }

  handleCloseSnackbar = () => {
    this.setState({message: null})
  }

  render() {
    return (
      <React.Fragment>
        <Dialog
          fullWidth
          maxWidth={'md'}
          open={this.state.action !== null}
          onClose={this.handleClose}
          aria-labelledby="simple-dialog-title"
        >
          <DialogTitle id="simple-dialog-title">Member</DialogTitle>
          <DialogContent>
            <ValidatorForm
              id="member-form"
              onSubmit={this.handleSubmit}
              onError={errors => console.log(errors)}
            >
              <MemberForm
                value={this.state.item}
                groups={this.state.groups}
                fields={this.state.fields}
                onChange={this.handleFormUpdate}
              />
            </ValidatorForm>
          </DialogContent>
          <DialogActions>
            {this.state.item && this.state.item.id && (
              <Button onClick={this.handleDelete} color="secondary">
                Delete
              </Button>
            )}
            <Button onClick={this.handleClose} color="primary">
              Cancel
            </Button>
            <Button type="submit" form="member-form" color="primary" autoFocus>
              Save
            </Button>
          </DialogActions>
        </Dialog>

        <Snackbar
          open={this.state.message !== null}
          autoHideDuration={5000}
          onClose={this.handleCloseSnackbar}
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'left',
          }}
          message={this.state.message}
        />
      </React.Fragment>
    )
  }
}

export default withStyles(styles)(MemberDialog)
