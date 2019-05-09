import React from "react";
import { withStyles } from "@material-ui/core/styles";

import Button from "@material-ui/core/Button";

import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import { ResourceClient } from "@flock-eco/core";
import UserAutocomplete from "../user/UserAutocomplete";

const styles = theme => ({
  paper: {
    overflowY:'visible'
  }
});

class UserGroupDialog extends React.Component {

  client = new ResourceClient('/api/user_groups')

  init = {
    name: '',
    users:[]
  };

  state = this.init;

  handleChange = (name) => (ev) => {
    this.setState({[name]: ev.target.value})
  };

  handleUsersChange = (value) => {
    const users = value.map(it => it.user)
    this.setState({users})
  };

  handleClose = () => {
    this.props.onComplete && this.props.onComplete();
  };

  handleSave = () => {
    const {id, onComplete} = this.props;
    const item = {
      name: this.state.name,
      users: this.state.users
        .map(it => it.code)
    }
    if(!id){
      this.client.create(item)
        .then(item => onComplete && onComplete(item))
    }
    else{
      this.client.update(id, item)
        .then(item => onComplete && onComplete(item))
    }
  };

  handleDelete = () => {
    const {id, onComplete} = this.props;
    this.client.delete(id)
      .then(item => onComplete && onComplete(item))
  };

  componentDidUpdate(prev) {
    const {id} = this.props;
    if(prev.id !== id){
      if(!id) {
        this.setState({ ...this.init })
      }
      else {
        this.client.findById(id)
          .then(res => {
            this.setState({
              name: res.name,
              users: res.users,
            })
          })
      }
    }
  }

  render() {
    const {classes, open, id} = this.props;
    return (
      <Dialog
        fullWidth
        maxWidth={"sm"}
        open={open}
        onClose={this.handleClose}
        classes={classes}
      >
        <DialogTitle>User group</DialogTitle>
        <DialogContent className={classes.paper}>
          <form>
            <Grid
              container
              direction="column"
              justify="space-evenly"
              alignItems="stretch"
              spacing={16}
            >
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Name"
                  className={classes.input}
                  value={this.state.name}
                  onChange={this.handleChange("name")}
                />
              </Grid>
              <Grid item xs={12}>
                <UserAutocomplete
                  fullWidth
                  label="Name"
                  className={classes.input}
                  value={this.state.users}
                  onChange={this.handleUsersChange}/>
              </Grid>
            </Grid>
          </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={this.handleClose} color="primary">
            Cancel
          </Button>
          {id && <Button onClick={this.handleDelete} color="secondary">
            Delete
          </Button>}
          <Button onClick={this.handleSave} color="primary" autoFocus>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    );
  }
}

export default withStyles(styles)(UserGroupDialog);
