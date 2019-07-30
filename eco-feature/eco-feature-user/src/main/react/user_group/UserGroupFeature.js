import React from "react";
import { withStyles } from "@material-ui/core/styles";

import AddIcon from "@material-ui/icons/Add";

import UserGroupTable from "./UserGroupTable";
import UserGroupDialog from "./UserGroupDialog";
import Paper from "@material-ui/core/es/Paper/Paper";
import Fab from "@material-ui/core/Fab";
import UserAutocomplete from "../user/UserAutocomplete";

const styles = theme => ({
  tablePaper: {
    marginBottom: 50,
    width: "100%",
    marginTop: theme.spacing(3),
    overflowX: "auto"
  },
  button: {
    position: "fixed",
    right: 20,
    bottom: 20,
    margin: theme.spacing(1)
  }
});

class UserGroupFeature extends React.Component {
  state = {
    open: false,
    item: null,
    reload: false
  };

  handleRowClick = (ev, item) => {
    this.setState({ open: true, item });
  };

  handleNewClick = () => {
    this.setState({
      open: true,
      item: {} });
  };

  handleComplete = value => {
    this.setState({
      open: false,
      item: null,
      reload: !this.state.reload });
  };

  handleSave = value => {
    if (this.state.item.id) {
      this.update(this.state.item);
    } else {
      this.create(this.state.item);
    }
  };

  handleDelete = value => {
    this.delete(this.state.item);
  };

  handleFormUpdate = value => {
    this.setState({ item: value });
  };

  load = () => {
    fetch(`/api/users?page=${this.state.page}&size=${this.state.size}`)
      .then(res => {
        this.setState({
          count: parseInt(res.headers.get("x-total"))
        });
        return res.json();
      })
      .then(json => {
        this.setState({ list: json });
      })
      .catch(e => {
        this.setState({ message: "Cannot load users" });
      });
  };

  render() {

    const { classes } = this.props;
    const { reload } = this.state;

    return (
      <div>
        <Paper className={classes.tablePaper}>

          <UserGroupTable
            onRowClick={this.handleRowClick}
            reload={reload}
          />
        </Paper>

        <UserGroupDialog
          id={this.state.item && this.state.item.code}
          open={this.state.open}
          onComplete={this.handleComplete}
        />

        <Fab
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.handleNewClick}
        >
          <AddIcon/>
        </Fab>
      </div>
    );
  }

  create(item) {
    const opts = {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=utf-8"
      },
      body: JSON.stringify(item)
    };
    fetch("/api/users", opts).then(() => {
      this.setState({ item: null });
      this.load();
    });
  }

  update(item) {
    const opts = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json; charset=utf-8"
      },
      body: JSON.stringify(item)
    };
    fetch(`/api/users/${item.id}`, opts).then(() => {
      this.setState({ item: null });
      this.load();
    });
  }

  delete(item) {
    const opts = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json; charset=utf-8"
      }
    };
    fetch(`/api/users/${item.id}`, opts).then(() => {
      this.setState({ item: null });
      this.load();
    });
  }
}

export default withStyles(styles)(UserGroupFeature);
