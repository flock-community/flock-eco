import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';

import AddIcon from '@material-ui/icons/Add';

import UserTable from "./UserTable";
import UserForm from "./UserForm";
import UserDialog from "./UserDialog";


const styles = theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing.unit,
  }
});

class itemManager extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      list: props.list || []
    }

    this.rowClick = (item) => {
      this.setState({item})
    }

    this.newClick = () => {
      this.setState({item: {}})
    }

    this.handleClose = value => {
      this.setState({item: null});
    };

    this.handleSave = value => {
      if(this.state.item.id){
        this.update(this.state.item)
      }else{
        this.create(this.state.item)
      }
    };


    this.handleFormUpdate = (value) => {
      console.log("handleFormUpdate", value)
      this.setState({item: value});
    };

    this.load();

  }

  render() {

    const {classes} = this.props;

    return (

      <div>
        <UserTable
          data={this.state.list}
          handleRowClick={this.rowClick}
        />

        <UserDialog
          open={this.state.item != null}
          onClose={this.handleClose}
          onSave={this.handleSave}

        >
          <UserForm
            authorities={this.state.authorities}
            item={this.state.item}
            onChange={this.handleFormUpdate}
          />
        </UserDialog>

        <Button
          variant="fab"
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.newClick}
        >
          <AddIcon/>
        </Button>
      </div>
    )
  }

  load() {
    const users = fetch('/api/users')
      .then(res => res.json())
      .then(json => {
        console.log(json)
        this.setState({list: json.content});
      })
    const authorities = fetch('/api/authorities')
      .then(res => res.json())
      .then(json => {
        console.log(json)
        this.setState({authorities: json});
      })

    return Promise.all([users, authorities])
  }

  create(item) {
    const opts = {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      body: JSON.stringify(item),
    };
    fetch('/api/user', opts)
      .then(() => {
        this.setState({item: null});
        this.load();
      })
  }

  update(item) {

    const opts = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      body: JSON.stringify(item),
    };
    fetch(`/api/users/${item.id}`, opts)
      .then(() => {
        this.setState({item: null});
        this.load();
      })
  }

  delete(item) {
    const opts = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
    };
    fetch(`/api/member_groups/${item.id}`, opts)
      .then(() => {
        this.setState({item: null});
        this.load();
      })
  }
};

export default withStyles(styles)(itemManager);