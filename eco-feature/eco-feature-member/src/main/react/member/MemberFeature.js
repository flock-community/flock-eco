import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';

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
  },
});

class MemberFeature extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      size: 10,
      search: props.search || '',
      members: props.members || [],
      count: 0,
      page: 0,
    };

    this.handleRowClick = (member) => {
      this.setState({member: member})
    };

    this.handleNewClick = () => {
      this.setState({member: {}})
    };

    this.handleSearch = (val) => {
      console.log("handleSearch", val.search)
      this.setState({search: val.search}, () => {
        this.load()
      });

    };

    this.handleClose = () => {
      console.log("handleClose", this.state.member)
      this.setState({member: null});
    };

    this.handleSave = () => {
      console.log("handleSave", this.state.member)

      if (this.state.member.id) {
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
      } else {
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

    this.handleChangePage = (event) => {
      console.log(event)
    }

    this.load()
  }

  render() {

    const {classes} = this.props;

    return (

      <Paper>

        <Grid
          container
          direction="column"
          spacing={16}
        >
          <Grid item>
            <MemberSearch onChange={this.handleSearch}/>
          </Grid>
          <Grid item>
            <MemberTable
              data={this.state.members}
              count={this.state.count}
              page={this.state.page}
              size={this.state.size}
              onRowClick={this.handleRowClick}
              onChangePage={this.handleChangePage}
            />
          </Grid>
        </Grid>


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
          onClick={this.handleNewClick}
        >
          <AddIcon/>
        </Button>
      </Paper>
    )
  }

  load() {
    return fetch(`/api/members?s=${this.state.search}&size=${this.state.size}`)
      .then(res => {
        res.headers.forEach(function (value, name) {
          console.log(name + ": " + value);
        });
        console.log(res.headers, res.headers.get('x-total'))
        this.setState({
          count: parseInt(res.headers.get('x-total'))
        })
        return res.json()
      })
      .then(json => {
        console.log(json)
        this.setState({members: json});
      })
  }


};

export default withStyles(styles)(MemberFeature);