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

class UserManager extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      users: props.users || []
    }

    this.rowClick = (user) => {
      this.setState({user})
    }

    this.newClick = () => {
      this.setState({user: {}})
    }

    this.handleClose = value => {
      this.setState({user: null});
    };

    fetch('/api/authorities')
      .then(res => res.json())
      .then(json => {
        console.log(json)
        this.setState({authorities: json});
      })

    fetch('/api/users')
      .then(res => res.json())
      .then(json => {
        console.log(json)
        this.setState({users: json.content});
      })

  }

  render() {

    const {classes} = this.props;

    return (

      <div>
        <UserTable
          data={this.state.users}
          handleRowClick={this.rowClick}
        />

        <UserDialog
          open={this.state.user != null}
          onClose={this.handleClose}
          classes={classes}

        >
          <UserForm
            authorities={this.state.authorities}
            user={this.state.user}
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
};

export default withStyles(styles)(UserManager);