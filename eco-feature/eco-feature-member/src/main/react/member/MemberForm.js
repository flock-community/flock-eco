import React from "react";

import {withStyles} from '@material-ui/core/styles';

import Grid from '@material-ui/core/Grid';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';

import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';

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

  handleChangeField(name) {

    return (event) => {
      const value = event.target.value
      console.log('handleChangeField', name, value)
      const fields = Object.assign(this.state.fields, {[name]: Array.isArray(value) ? value.join(',') : value})
      this.setState({fields}, () => {
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

        {this.props.groups && this.groupsRow()}

        {this.props.fields && this.fieldsRow()}

      </Grid>
    )
  }

  groupsRow() {

    const {classes} = this.props;

    const groups = this.props.groups || []
    this.state.groups = this.state.groups || []

    if (groups.length === 0)
      return null;

    return (<Grid item xs={12}>
      <FormControl
        className={classes.input}>
        <InputLabel htmlFor="groups">Groups</InputLabel>
        <Select
          className={classes.input || []}
          multiple
          value={this.state.groups.map(it => it.id) || []}
          input={<Input id="select-multiple"/>}
          onChange={this.handleChangeGroup('groups')}
          renderValue={selected => selected.map(this.resolverGroup).map(it => it.name).join(', ')}
        >
          {groups.map(it => (
            <MenuItem
              key={it.id}
              value={it.id}>
              <Checkbox checked={this.state.groups.map(it => it.id).indexOf(it.id) > -1}/>
              <ListItemText primary={it.name}/>
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Grid>)
  }

  fieldsRow() {

    const {classes} = this.props;

    const fields = this.props.fields || []
    const value = (field) => this.state.fields[field.name] || ""

    if (fields.length === 0)
      return null;

    const textField = (field) => (<TextField
      label={field.label}
      className={classes.input}
      value={value(field)}
      onChange={this.handleChangeField(field.name)}/>)

    const checkboxField = (field) => (<FormControlLabel
      className={classes.input}
      disabled={field.disabled}
      control={
        <Checkbox
          onChange={this.handleChangeField(field.name)}
          checked={(value(field) === 'true')}
          value={(value(field) !== 'true').toString()}
        />
      }
      label={field.label}
    />)

    const singleSelectField = (field) => (

      <FormControl
        className={classes.input}
        disabled={field.disabled}>
        <InputLabel htmlFor={field.name}>{field.label}</InputLabel>
        <Select
          className={classes.input || []}
          value={value(field)}
          input={<Input id="select-multiple"/>}
          onChange={this.handleChangeField(field.name)}
        >
          {field.options.map(it => (
            <MenuItem
              key={it}
              value={it}>
              <ListItemText primary={it}/>
            </MenuItem>
          ))}
        </Select>
      </FormControl>)

    const multiSelectField = (field) => (

      <FormControl className={classes.input}>
        <InputLabel htmlFor={field.name}>{field.label}</InputLabel>
        <Select
          multiple
          className={classes.input || []}
          value={value(field).split(',')}
          input={<Input id="select-multiple"/>}
          onChange={this.handleChangeField(field.name)}
          renderValue={selected => selected.join(', ')}
        >
          {field.options.map(it => (
            <MenuItem
              key={it}
              value={it}>
              <Checkbox checked={value(field).split(',').indexOf(it) > -1}/>
              <ListItemText primary={it}/>
            </MenuItem>
          ))}
        </Select>
      </FormControl>)

    return fields.map(it => (<React.Fragment
      key={it.name}>
      <Grid item xs={12}>
        {it.type === 'CHECKBOX' && checkboxField(it)}
        {it.type === 'TEXT' && textField(it)}
        {it.type === 'SINGLE_SELECT' && singleSelectField(it)}
        {it.type === 'MULTI_SELECT' && multiSelectField(it)}
      </Grid>
    </React.Fragment>))


  }


}

export default withStyles(styles)(MemberForm);