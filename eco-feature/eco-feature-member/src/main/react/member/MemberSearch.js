import React from "react";

import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import TextField from '@material-ui/core/TextField';

class MemberForm extends React.Component {

  constructor(props) {
    super(props);
    this.init()
  }

  init() {
    this.state = {
      search: ''
    };
  }

  render() {
    const {classes, item} = this.props;

    return (
      <Grid
        container
        direction="column"
        justify="space-evenly"
        alignItems="stretch"

      >

            <TextField
              label="Search"
              value={this.state.search}
              onChange={this.handleChange('search')}/>

      </Grid>
    )
  }

  handleChange(name) {
    return (event) => {
      this.setState(
        {[name]: event.target.value},
        () => {
          this.props.onChange(this.state)
        })

    }
  };

}

export default MemberForm;