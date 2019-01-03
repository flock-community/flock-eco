import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Fab from '@material-ui/core/Fab';
import Paper from '@material-ui/core/Card';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';

import Grid from '@material-ui/core/Grid';

import AddIcon from '@material-ui/icons/Add';


import MemberTable from "./MemberTable";
import MemberSpecification from "./MemberSpecification";
import MemberDialog from "./MemberDialog";
import MemberMerger from './MemberMerger'

const styles = theme => ({
  tablePaper: {
    marginBottom: 50,
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
  button: {
    position: 'fixed',
    right: 20,
    bottom: 20,
    margin: theme.spacing.unit,
  },
});

class MemberFeature extends React.Component {

  state = {
    size: 10,
    search: this.props.search || '',
    action: null,
    item: null,
    filter: {},
    refresh: false,
    refreshSelection: false,
    mergeMemberIds: null,
  };

  handleRowClick = (item) => {
    this.setState({item, action: 'EDIT'})
  };

  handleNewClick = () => {
    this.setState({item: null, action: 'NEW'})
  };

  handleSpecification = (specification) => {
    this.setState({
      page: 0,
      specification
    });
  };

  handleComplete = () => {
    this.setState({
      item: null,
      action: null,
      refresh: !this.state.refresh,
      mergeMemberIds: null,
    })
  };

  handleMergeComplete = () =>
    this.setState(state => ({
      mergeMemberIds: null,
      refresh: !this.state.refresh,
      refreshSelection: !state.refreshSelection,
    }))

  mergeMembers = (mergeMemberIds) => this.setState({mergeMemberIds});

  handleMergerCancel = () =>
    this.setState({
      mergeMemberIds: null
    })

  render() {

    const {classes} = this.props;

    return (

      <React.Fragment>

        <Grid
          container
          direction="column"
          spacing={16}
        >
          <Grid item>
            <Card>
              <CardContent>
                <MemberSpecification
                  onChange={this.handleSpecification}/>
              </CardContent>
            </Card>
          </Grid>
          <Grid item>
            <Paper className={classes.tablePaper}>
              <MemberTable
                specification={this.state.specification}
                page={this.state.page}
                size={this.state.size}
                refresh={this.state.refresh}
                onRowClick={this.handleRowClick}
                refreshSelection={this.state.refreshSelection}
                onMergeMembers={this.mergeMembers}
              />
            </Paper>
          </Grid>
        </Grid>

        <MemberDialog
          id={this.state.item && this.state.item.id}
          action={this.state.action}
          onComplete={this.handleComplete}/>

        <MemberMerger
          mergeMemberIds={this.state.mergeMemberIds}
          onComplete={this.handleMergeComplete}
          onCancel={this.handleMergerCancel}/>

        <Fab
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.handleNewClick}
        >
          <AddIcon/>
        </Fab>

      </React.Fragment>

    )
  }

};

export default withStyles(styles)(MemberFeature);