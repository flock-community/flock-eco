import React, {useEffect, useState} from 'react'
import * as Yup from 'yup'
import Grid from '@material-ui/core/Grid'
import InputLabel from '@material-ui/core/InputLabel'
import TextField from '@material-ui/core/TextField'

import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'

import Input from '@material-ui/core/Input'
import Select from '@material-ui/core/Select'
import MenuItem from '@material-ui/core/MenuItem'

import Checkbox from '@material-ui/core/Checkbox'
import ListItemText from '@material-ui/core/ListItemText'

import {TextValidator, ValidatorForm} from 'react-material-ui-form-validator'

const schema = Yup.object().shape({
  firstName: Yup.string()
    .required()
    .default(''),
  infix: Yup.string()
    .default(''),
  surName: Yup.string()
    .required()
    .default(''),
  email: Yup.string()
    .default(''),
  phoneNumber: Yup.string()
    .default(''),
  street: Yup.string()
    .default(''),
  houseNumber: Yup.string()
    .default(''),
  houseNumberExtension: Yup.string()
    .default(''),
  postalCode: Yup.string()
    .default(''),
  city: Yup.string()
    .default(''),
  country: Yup.string()
    .default(''),
  language: Yup.string()
    .default(''),
  gender: Yup.string()
    .default('UNKNOWN'),
  birthDate: Yup.string()
    .default(''),
  groups: Yup.array()
    .default([]),
  fields: Yup.array()
    .default([]),
  status: Yup.string()
    .default('NEW'),
})

export function MemberForm({value, groups, fields, onChange, onSubmit}) {

  const [state, setState] = useState(value || schema.cast())

  useEffect(() => {
    setState(value || schema.cast())
  }, [value])

  const resolverGroup = code => groups.find(it => it.code === code) || {}
  const resolveField = key => (state.fields.find(it => it.key === key)) || {}

  const handleChange = (name) => (event) => {
    const it = {
      ...state,
      [name]: event.target.value,
    }
    setState(it)
    onChange(it)
  }

  const handleChangeGroup = (name) => (event) => {
    const value = event.target.value
    const it = {
      ...state,
      [name]: value,
    }
    setState(it)
    onChange(it)
  }

  const handleChangeField = (name) => (event) => {
    const value = event.target.value
    const it = {
      ...state,
      fields: fields.map(field => ({
        key: field.name,
        value: field.name === name
          ? Array.isArray(value) ? value.join(',') : value
          : resolveField(field.name).value || '',
      })),
    }
    setState(it)
    onChange(it)
  }

  const renderGroupsRow = groups && (
    <Grid item xs={12}>
      <FormControl fullWidth>
        <InputLabel htmlFor="groups">Groups</InputLabel>
        <Select
          multiple
          value={state.groups || []}
          input={<Input/>}
          onChange={handleChangeGroup('groups')}
          renderValue={selected =>
            selected
              .map(resolverGroup)
              .map(it => it.name)
              .join(',')
          }>
          {groups.map(it => (
            <MenuItem key={it.code} value={it.code}>
              <Checkbox
                checked={state.groups.indexOf(it.code) > -1}
              />
              <ListItemText primary={it.name}/>
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Grid>
  )

  const textField = field => (
    <TextField
      label={field.label}
      fullWidth
      disabled={field.disabled}
      value={resolveField(field.name).value || ''}
      onChange={handleChangeField(field.name)}
    />
  )

  const checkboxField = field => (
    <FormControlLabel
      disabled={field.disabled}
      control={
        <Checkbox
          onChange={handleChangeField(field.name)}
          checked={resolveField(field.name).value === 'true'}
          value={(resolveField(field.name).value !== 'true').toString()}
        />
      }
      label={field.label}
    />
  )

  const singleSelectField = field => (
    <FormControl
      fullWidth
      disabled={field.disabled}>
      <InputLabel htmlFor={field.name}>{field.label}</InputLabel>
      <Select
        value={resolveField(field.name).value || ''}
        input={<Input/>}
        onChange={handleChangeField(field.name)}
      >
        {field.options.map(it => (
          <MenuItem key={it} value={it}>
            <ListItemText primary={it}/>
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  )

  const multiSelectField = field => (
    <FormControl
      fullWidth
      disabled={field.disabled}>
      <InputLabel htmlFor={field.name}>{field.label}</InputLabel>
      <Select
        multiple
        value={resolveField(field.name).value ? resolveField(field.name).value.split(',') : []}
        input={<Input/>}
        onChange={handleChangeField(field.name)}
        renderValue={selected => selected.join(',')}
      >
        {field.options.map(it => (
          <MenuItem key={it} value={it}>
            <Checkbox
              checked={
                (resolveField(field.name).value || '').split(',').indexOf(it) > -1
              }
            />
            <ListItemText primary={it}/>
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  )

  const renderFieldsRow = fields && fields.map(it => (
    <React.Fragment key={it.name}>
      <Grid item xs={12}>
        {it.type === 'CHECKBOX' && checkboxField(it)}
        {it.type === 'TEXT' && textField(it)}
        {it.type === 'SINGLE_SELECT' && singleSelectField(it)}
        {it.type === 'MULTI_SELECT' && multiSelectField(it)}
      </Grid>
    </React.Fragment>
  ))

  return (
    <ValidatorForm
      id="member-form"
      onSubmit={onSubmit}>
      <Grid container spacing={1}>
        <Grid item xs={5}>
          <TextValidator
            required
            name="firstName"
            label="First name"
            fullWidth
            value={state.firstName || ''}
            onChange={handleChange('firstName')}
            validators={['required']}
            errorMessages={['this field is required']}
          />
        </Grid>

        <Grid item xs={2}>
          <TextField
            fullWidth
            label="Infix"
            value={state.infix || ''}
            onChange={handleChange('infix')}
          />
        </Grid>

        <Grid item xs={5}>
          <TextValidator
            required
            name="surName"
            label="Surname"
            fullWidth
            value={state.surName || ''}
            onChange={handleChange('surName')}
            validators={['required']}
            errorMessages={['this field is required']}
          />
        </Grid>

        <Grid item xs={7}>
          <TextField
            type="date"
            fullWidth
            label="Birth date"
            InputLabelProps={{shrink: true}}
            value={state.birthDate || ''}
            onChange={handleChange('birthDate')}
          />
        </Grid>

        <Grid item xs={5}>
          <FormControl
            fullWidth>
            <InputLabel htmlFor="gender">Gender</InputLabel>
            <Select
              required
              value={state.gender || 'UNKNOWN'}
              onChange={handleChange('gender')}
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
            fullWidth
            label="Phone number"
            value={state.phoneNumber || ''}
            onChange={handleChange('phoneNumber')}
          />
        </Grid>

        <Grid item xs={12}>
          <TextField
            fullWidth
            label="Email"
            value={state.email || ''}
            onChange={handleChange('email')}
          />
        </Grid>

        <Grid item xs={8}>
          <TextField
            fullWidth
            label="Street"
            value={state.street || ''}
            onChange={handleChange('street')}
          />
        </Grid>

        <Grid item xs={2}>
          <TextField
            fullWidth
            label="Nr"
            value={state.houseNumber || ''}
            onChange={handleChange('houseNumber')}
          />
        </Grid>

        <Grid item xs={2}>
          <TextField
            fullWidth
            label="Ext"
            value={state.houseNumberExtension || ''}
            onChange={handleChange('houseNumberExtension')}
          />
        </Grid>

        <Grid item xs={3}>
          <TextField
            fullWidth
            label="Postal code"
            value={state.postalCode || ''}
            onChange={handleChange('postalCode')}
          />
        </Grid>

        <Grid item xs={9}>
          <TextField
            fullWidth
            label="City"
            value={state.city || ''}
            onChange={handleChange('city')}
          />
        </Grid>
        {renderGroupsRow}
        {renderFieldsRow}
      </Grid>
    </ValidatorForm>)
}
