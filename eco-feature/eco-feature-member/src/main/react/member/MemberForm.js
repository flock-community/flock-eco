import React from "react";

import {withStyles} from '@material-ui/core/styles';

import Grid from '@material-ui/core/Grid';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';

import FormControl from '@material-ui/core/FormControl';

import Input from '@material-ui/core/Input';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

import Checkbox from '@material-ui/core/Checkbox';
import ListItemText from '@material-ui/core/ListItemText';

import {TextValidator} from 'react-material-ui-form-validator';

const styles = theme => ({
  input: {
    width: "100%",
  },
});

class MemberForm extends React.Component {

  constructor(props) {
    super(props);
    this.state = props.value || {}
  }

  handleChange(name) {
    return (event) => {
      console.log('value', event.target.value)
      this.setState({[name]: event.target.value}, () => {
        this.props.onChange(this.state)
      })
    }
  };

  handleChangeGroup(name) {
    return (event) => {
      const value = event.target.value.map(this.resolverGroup)
      console.log('value', value)
      this.setState({[name]: value}, () => {
        this.props.onChange(this.state)
      })
    }
  };

  resolverGroup = id => this.props.groups.find(it => it.id === id)

  render() {
    const {classes} = this.props;

    return (
      <Grid
        container
        spacing={16}>
        <Grid item xs={5}>
          <TextValidator
            required
            name="firstName"
            label="First name"
            className={classes.input}
            value={this.state.firstName || ''}
            onChange={this.handleChange('firstName')}
            validators={['required']}
            errorMessages={['this field is required']}/>
        </Grid>

        <Grid item xs={2}>
          <TextField
            className={classes.input}
            label="Infix"
            value={this.state.infix || ''}
            onChange={this.handleChange('infix')}/>
        </Grid>

        <Grid item xs={5}>
          <TextValidator
            required
            name="surName"
            label="Surname"
            className={classes.input}
            value={this.state.surName || ''}
            onChange={this.handleChange('surName')}
            validators={['required']}
            errorMessages={['this field is required']}/>
        </Grid>

        <Grid item xs={7}>
          <TextField
            className={classes.input}
            label="Birth date"
            value={this.state.birthDate || ''}
            onChange={this.handleChange('birthDate')}/>
        </Grid>

        <Grid item xs={5}>
          <FormControl className={classes.input}>
            <InputLabel htmlFor="gender">Gender</InputLabel>
            <Select
              required
              value={this.state.gender || 'UNKNOWN'}
              onChange={this.handleChange('gender')}
              inputProps={{
                name: 'gender',
                id: 'gender',
              }}
            >
              <MenuItem value="UNKNOWN">Unknown</MenuItem>
              <MenuItem value="MALE">Male</MenuItem>
              <MenuItem value="FEMALE">Female</MenuItem>
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12}>
          <TextField
            className={classes.input}
            label="Email"
            value={this.state.email || ''}
            onChange={this.handleChange('email')}/>
        </Grid>

        <Grid item xs={8}>
          <TextField
            className={classes.input}
            label="Street"
            value={this.state.street || ''}
            onChange={this.handleChange('street')}/>
        </Grid>

        <Grid item xs={2}>
          <TextField
            className={classes.input}
            label="Nr"
            value={this.state.houseNumber || ''}
            onChange={this.handleChange('houseNumber')}/>
        </Grid>

        <Grid item xs={2}>
          <TextField
            className={classes.input}
            label="Ext"
            value={this.state.houseNumberExtension || ''}
            onChange={this.handleChange('houseNumberExtension')}/>
        </Grid>

        <Grid item xs={3}>
          <TextField
            className={classes.input}
            label="Postal code"
            value={this.state.postalCode || ''}
            onChange={this.handleChange('postalCode')}/>
        </Grid>

        <Grid item xs={9}>
          <TextField
            className={classes.input}
            label="City"
            value={this.state.city || ''}
            onChange={this.handleChange('city')}/>
        </Grid>

        {this.groupsRow()}

      </Grid>
    )
  }

  groupsRow() {

    const {classes} = this.props;

    const groups = this.props.groups || []

    if (groups.length === 0)
      return null;

    console.log('-----', this.state.groups)

    return (<Grid item xs={12}>
      <FormControl
        className={classes.input}>
        <InputLabel htmlFor="groups">Groups</InputLabel>
        <Select
          className={classes.input || []}
          multiple
          value={this.state.groups.map(it => it.id) || []}
          input={<Input id="select-multiple" />}
          onChange={this.handleChangeGroup('groups')}
          renderValue={selected => selected.map(this.resolverGroup).map(it => it.name).join(', ')}
        >
          {groups.map(it => (
            <MenuItem
              key={it.id}
              value={it.id}>
              <Checkbox checked={this.state.groups.map(it => it.id).indexOf(it.id) > -1} />
              <ListItemText primary={it.name} />
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Grid>)
  }


}

export default withStyles(styles)(MemberForm);