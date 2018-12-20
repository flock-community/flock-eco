import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Card';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';

import Grid from '@material-ui/core/Grid';

import AddIcon from '@material-ui/icons/Add';

import MemberTable from "./MemberTable";
import MemberSearch from "./MemberSearch";
import MemberDialog from "./MemberDialog";

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
    members: this.props.members || [],
    action: null,
    item: null,
    groups: [],
    count: 0,
    page: 0,
  };

  componentDidMount() {
    this.load();
  }

  handleRowClick = (item) => {
    this.setState({item, action: 'EDIT'})
  };

  handleNewClick = () => {
    this.setState({item: null, action: 'NEW'})
  };

  handleSearch = (val) => {
    this.setState({
      page: 0,
      search: val.search
    }, () => {
      this.load()
    });

  };

  handleChangePage = (event, page) => {
    this.setState({page}, () => {
      this.load()
    })
  };

  handleComplete = () => {
    this.setState({
      item: null,
      action: null
    }, () => {
      this.load()
    })
  }

  load = () => {
    fetch(`/api/members?s=${this.state.search}&page=${this.state.page}&size=${this.state.size}`)
      .then(res => {
        this.setState({
          count: parseInt(res.headers.get('x-total'))
        })
        return res.json()
      })
      .then(json => {
        this.setState({members: json});
      })
      .catch(e => {
        this.setState({message: "Cannot load members"})
      })
  }

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
                <MemberSearch onChange={this.handleSearch}/>
              </CardContent>
            </Card>
          </Grid>
          <Grid item>
            <Paper className={classes.tablePaper}>
              <MemberTable
                data={this.state.members}
                count={this.state.count}
                page={this.state.page}
                size={this.state.size}
                onRowClick={this.handleRowClick}
                onChangePage={this.handleChangePage}
              />
            </Paper>
          </Grid>
        </Grid>

        {this.renderDialog()}

        <Button
          variant="fab"
          color="primary"
          aria-label="Add"
          className={classes.button}
          onClick={this.handleNewClick}
        >
          <AddIcon/>
        </Button>

      </React.Fragment>

    )
  }

  renderDialog() {

    return (<MemberDialog
      id={this.state.item && this.state.item.id}
      action={this.state.action}
      onComplete={this.handleComplete}/>)
  }

};

export default withStyles(styles)(MemberFeature);