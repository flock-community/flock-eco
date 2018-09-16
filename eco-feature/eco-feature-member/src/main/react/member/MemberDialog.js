import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';

import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';

import Snackbar from '@material-ui/core/Snackbar';

import {ValidatorForm} from 'react-material-ui-form-validator';

import MemberForm from './MemberForm';

const styles = theme => ({});

class MemberDialog extends React.Component {

  state = {
    value: this.props.value,
    message: null
  }

  componentDidMount() {
    fetch(`/api/member_groups`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({groups: json});
      })
      .catch(e => {
        this.setState({message: "Cannot load groups"});
      })

    fetch(`/api/member_fields`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({fields: json});
      })
      .catch(e => {
        this.setState({message: "Cannot load fields"});
      })
  }

  componentDidUpdate(prevProps) {
    if (this.props.value !== prevProps.value) {
      this.setState({value: this.props.value});
    }
  }

  handleClose = () => {
    this.props.onComplete();
  };

  handleDelete = () => {
    const opts = {
      method: "DELETE",
    };
    fetch(`/api/members/${this.state.value.id}`, opts)
      .then((res) => {
        if(res.ok){
          this.props.onComplete();
        }else {
          res.json().then(e => {
            this.setState({message: e.message || "Cannot delete member"});
          })
        }
      })
  };

  handleSave = () => {
    if (this.state.value.id) {
      const opts = {
        method: "PUT",
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
        body: JSON.stringify(this.state.value),
      };
      fetch(`/api/members/${this.state.value.id}`, opts)
        .then((res) => {
          if(res.ok){
            this.props.onComplete();
          }else {
            res.json().then(e => {
              this.setState({message: e.message || "Cannot update member"});
            })
          }
        })
    } else {
      const opts = {
        method: "POST",
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
        body: JSON.stringify(this.state.value),
      };
      fetch('/api/members', opts)
        .then((res) => {
          if(res.ok){
            this.props.onComplete();
          }else {
            res.json().then(e => {
              this.setState({message: e.message || "Cannot create member"});
            })

          }
        })
    }
  };

  handleFormUpdate = (value) => {
    this.setState({value: value});
  };


  handleSubmit = () => {
    this.handleSave()
  }

  handleCloseSnackbar = () => {
    this.setState({message: null})
  }

  render() {
    const {classes, open} = this.props;

    return (<React.Fragment>
        <Dialog
          fullWidth
          maxWidth={'md'}
          open={this.state.value !== null}
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
                value={this.state.value}
                groups={this.state.groups}
                fields={this.state.fields}
                onChange={this.handleFormUpdate}
              />
            </ValidatorForm>
          </DialogContent>
          <DialogActions>
            {this.state.value && this.state.value.id && (<Button onClick={this.handleDelete} color="secondary">
              Delete
            </Button>)}
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
          message={this.state.message}/>

      </React.Fragment>
    );
  }
}

export default withStyles(styles)(MemberDialog);