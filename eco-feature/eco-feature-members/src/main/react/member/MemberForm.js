import React from "react";

import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';

class MemberForm extends React.Component {

  constructor(props) {
    super(props);
    this.init(props.item)
  }

  init(obj) {
    const member = {
      firstName: "",
      infix: "",
      surName: "",
      email: "",
    };

    this.state = Object.assign(member, obj);
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
          label="First name"
          value={this.state.firstName}
          onChange={this.handleChange('firstName')}/>

        <TextField
          label="Infix"
          value={this.state.infix}
          onChange={this.handleChange('infix')}/>

        <TextField
          label="Sur name"
          value={this.state.surName}
          onChange={this.handleChange('surName')}/>

        <TextField
          label="Email"
          value={this.state.email}
          onChange={this.handleChange('email')}/>

      </Grid>
    )
  }

  handleChange(name) {
    return (event) => {
      this.setState(
        {[name]: event.target.value},
        () => {
          console.log(this.state)
          this.props.onChange(this.state)
        })

    }
  };

}

export default MemberForm;