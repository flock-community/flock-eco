import React from "react";

import {withStyles} from '@material-ui/core/styles';

import Grid from '@material-ui/core/Grid';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';

import FormControl from '@material-ui/core/FormControl';

import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

const styles = theme => ({
  input: {
    width: "100%",
  },
});

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
      groups: []
    };

    this.state = {...member, ...memberData};
  }

  render() {
    const {classes} = this.props;

    const groups = this.props.groups || []

    return (
      <Grid
        container
        spacing={16}
      >
        <Grid item xs={5}>
          <TextField
            required
            className={classes.input}
            label="First name"
            value={this.state.firstName}
            onChange={this.handleChange('firstName')}/>
        </Grid>

        <Grid item xs={2}>
          <TextField
            className={classes.input}
            label="Infix"
            value={this.state.infix}
            onChange={this.handleChange('infix')}/>
        </Grid>

        <Grid item xs={5}>
          <TextField
            required
            className={classes.input}
            label="Surname"
            value={this.state.surName}
            onChange={this.handleChange('surName')}/>
        </Grid>

        <Grid item xs={7}>
          <TextField
            className={classes.input}
            label="Birth date"
            value={this.state.birthDate}
            onChange={this.handleChange('birthDate')}/>
        </Grid>

        <Grid item xs={5}>
          <FormControl className={classes.input}>
            <InputLabel htmlFor="gender">Gender</InputLabel>
            <Select
              required

              value={this.state.gender}
              onChange={this.handleChange('gender')}
              inputProps={{
                name: 'gender',
                id: 'gender',
              }}
            >
              <MenuItem value="MALE">Male</MenuItem>
              <MenuItem value="FEMALE">Female</MenuItem>
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12}>
          <TextField
            className={classes.input}
            label="Email"
            value={this.state.email}
            onChange={this.handleChange('email')}/>
        </Grid>

        <Grid item xs={8}>
          <TextField
            className={classes.input}
            label="Street"
            value={this.state.street}
            onChange={this.handleChange('street')}/>
        </Grid>

        <Grid item xs={2}>
          <TextField
            className={classes.input}
            label="Nr"
            value={this.state.houseNumber}
            onChange={this.handleChange('houseNumber')}/>
        </Grid>

        <Grid item xs={2}>
          <TextField
            className={classes.input}
            label="Ext"
            value={this.state.houseNumberExtension}
            onChange={this.handleChange('houseNumberExtension')}/>
        </Grid>

        <Grid item xs={3}>
          <TextField
            className={classes.input}
            label="Postal code"
            value={this.state.postalCode}
            onChange={this.handleChange('postalCode')}/>
        </Grid>

        <Grid item xs={9}>
          <TextField
            className={classes.input}
            label="City"
            value={this.state.city}
            onChange={this.handleChange('city')}/>
        </Grid>

        {groups.length > 0 ? this.groupsRow(groups) : null}

      </Grid>
    )
  }

  groupsRow(groups) {

    const {classes} = this.props;

    return (<Grid item xs={12}>
      <FormControl>
        <InputLabel htmlFor="groups">Groups</InputLabel>
        <Select
          className={classes.input}
          multiple
          value={this.state.groups}
          inputProps={{
            name: 'groups',
            id: 'groups',
          }}
          onChange={this.handleChange('groups')}
        >
          {groups.map(it => (
            <MenuItem
              key={it.code}
              value={it}
            >
              {it.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Grid>)
  }

  handleChange(name) {
    return (event) => {
      console.log('value', event.target.value)
      this.setState(
        {[name]: event.target.value},
        () => {
          this.props.onChange(this.state)
        })

    }
  };

}

export default withStyles(styles)(MemberForm);