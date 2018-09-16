import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';

import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';

import {ValidatorForm} from 'react-material-ui-form-validator';

import MemberForm from './MemberForm';

const styles = theme => ({});

class MemberDialog extends React.Component {

  state = {
    value: this.props.value
  }

  componentDidMount() {
    fetch(`/api/member_groups`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({groups: json});
      })

    fetch(`/api/member_fields`)
      .then(res => {
        return res.json()
      })
      .then(json => {
        this.setState({fields: json});
      })
  }

  handleClose = () => {
    this.props.onComplete();
  };

  handleDelete = () => {
    const opts = {
      method: "DELETE",
    };
    fetch(`/api/members/${this.state.value.id}`, opts)
      .then(() => {
        this.setState({member: null});
        this.props.onComplete();
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
        .then(() => {
          this.setState({member: null});
          this.props.onComplete();
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
        .then(() => {
          this.setState({member: null});
          this.props.onComplete();
        })
    }
  };

  handleFormUpdate = (value) => {
    this.setState({value: value});
  };


  handleSubmit = () => {
    this.handleSave()
  }

  render() {
    const {classes, open} = this.props;

    return (
      <Dialog
        fullWidth
        maxWidth={'md'}
        open={true}
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
          <Button onClick={this.handleDelete} color="secondary">
            Delete
          </Button>
          <Button onClick={this.handleClose} color="primary">
            Cancel
          </Button>
          <Button type="submit" form="member-form" color="primary" autoFocus>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    );
  }
}

export default withStyles(styles)(MemberDialog);