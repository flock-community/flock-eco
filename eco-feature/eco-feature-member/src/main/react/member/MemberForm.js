import React from "react";

import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';

class MemberForm extends React.Component {

  constructor(props) {
    super(props);
    this.init(props.item)
  }

  init(memberData) {
    const member = {
        firstName: "",
        infix: "",
        surName: "",
        gender: "",
        birthDate: "",
        email: "",
        street: "",
        houseNumber: "",
        houseNumberExtension: "",
        postalCode: "",
        city: "",
    };

    this.state = {...member, ...memberData};
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
          label="Surname"
          value={this.state.surName}
          onChange={this.handleChange('surName')}/>

        <TextField
          label="Gender"
          value={this.state.gender}
          onChange={this.handleChange('gender')}/>

        <TextField
          label="Birth date"
          value={this.state.birthDate}
          onChange={this.handleChange('birthDate')}/>

        <TextField
          label="Email"
          value={this.state.email}
          onChange={this.handleChange('email')}/>

        <TextField
          label="House number"
          value={this.state.houseNumber}
          onChange={this.handleChange('houseNumber')}/>

        <TextField
          label="House number extension"
          value={this.state.houseNumberExtension}
          onChange={this.handleChange('houseNumberExtension')}/>

        <TextField
          label="Postal code"
          value={this.state.postalCode}
          onChange={this.handleChange('postalCode')}/>

        <TextField
          label="City"
          value={this.state.city}
          onChange={this.handleChange('city')}/>

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