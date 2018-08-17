import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';

import AddIcon from '@material-ui/icons/Add';

import MemberTable from "./MemberTable";
import MemberSearch from "./MemberSearch";
import MemberForm from "./MemberForm";
import MemberDialog from "./MemberDialog";


const styles = theme => ({
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing.unit,
  }
});

class MemberFeature extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      search: '',
      members: props.members || []
    };

    this.rowClick = (member) => {
      this.setState({member: member})
    };

    this.newClick = () => {
      this.setState({member: {}})
    };

    this.handleSearch = (val) => {
      console.log("handleSearch", this.state.search)
      this.setState({search: val});
    };

    this.handleClose = () => {
      console.log("handleClose", this.state.member)
      this.setState({member: null});
    };

    this.handleSave = () => {
      console.log("handleSave", this.state.member)

      if(this.state.member.id){
        const opts = {
          method: "PUT",
          headers: {
            "Content-Type": "application/json; charset=utf-8",
          },
          body: JSON.stringify(this.state.member),
        };
        fetch(`/api/members/${this.state.member.id}`, opts)
          .then(() => {
            this.setState({member: null});
            this.load();
          })
      }else{
        const opts = {
          method: "POST",
          headers: {
            "Content-Type": "application/json; charset=utf-8",
          },
          body: JSON.stringify(this.state.member),
        };
        fetch('/api/members', opts)
          .then(() => {
            this.setState({member: null});
            this.load();
          })
      }


    };

    this.handleFormUpdate = (value) => {
      console.log("handleFormUpdate", value)
      this.setState({member: value});
    };

    this.load()
  }

  render() {

    const {classes} = this.props;

    return (

      <div>
        <MemberSearch onChange={this.handleSearch}/>
        <MemberTable
          data={this.state.members}
          handleRowClick={this.rowClick}
        />

        <MemberDialog
          open={this.state.member != null}
          onClose={this.handleClose}
          onSave={this.handleSave}

        >
          <MemberForm
            item={this.state.member}
            onChange={this.handleFormUpdate}
          />

        </MemberDialog>

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
    return fetch('/api/members')
      .then(res => res.json())
      .then(json => {
        console.log(json)
        this.setState({members: json});
      })
  }


};

export default withStyles(styles)(MemberFeature);