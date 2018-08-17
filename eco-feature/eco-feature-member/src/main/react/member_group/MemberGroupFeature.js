import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';

import AddIcon from '@material-ui/icons/Add';

import MemberGroupTable from "./MemberGroupTable";
import MemberGroupForm from "./MemberGroupForm";
import MemberGroupDialog from "./MemberGroupDialog";


const styles = theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing.unit,
  }
});

class MemberManager extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      list: props.list || []
    };

    this.rowClick = (item) => {
      this.setState({item})
    };

    this.newClick = () => {
      this.setState({item: {}})
    };

    this.handleFormClose = () => {
      console.log("handleClose", this.state.item)
      this.setState({item: null});
    };

    this.handleFormSave = () => {
      console.log("handleSave", this.state.item)
      if(this.state.item.id){
        this.update(this.state.item)
      }else{
        this.create(this.state.item)
      }

    };

    this.handleFormDelete = () => {
      console.log("handleDelete", this.state.item)
      this.delete(this.state.item)
    };

    this.handleFormUpdate = (value) => {
      console.log("handleFormUpdate", value)
      this.setState({item: value});
    };

    this.load()
  }

  render() {

    const {classes} = this.props;

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
    return fetch('/api/member_groups')
      .then(res => res.json())
      .then(json => {
        console.log(json)
        this.setState({list: json});
      })
  }

  create() {
    const opts = {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      body: JSON.stringify(this.state.item),
    };
    fetch('/api/member_groups', opts)
      .then(() => {
        this.setState({item: null});
        this.load();
      })
  }

  update() {

    const opts = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      body: JSON.stringify(this.state.item),
    };
    fetch(`/api/member_groups/${this.state.item.code}`, opts)
      .then(() => {
        this.setState({item: null});
        this.load();
      })
  }

  delete() {
    const opts = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
    };
    fetch(`/api/member_groups/${this.state.item.code}`, opts)
      .then(() => {
        this.setState({item: null});
        this.load();
      })
  }

}

export default withStyles(styles)(MemberManager);