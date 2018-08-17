import React from "react";

import Grid from '@material-ui/core/Grid';
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

  handleChange(name) {
    return (event) => {
      this.setState(
        {[name]: event.target.value},
        () => {
          this.props.onChange(this.state)
        })

    }
  };

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
}

export default MemberForm;