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

const schema = Yup.object()
  .shape({
    firstName: Yup.string().required().default('').defined(),
    infix: Yup.string().default('').defined(),
    surName: Yup.string().required().default('').defined(),
    email: Yup.string().default('').defined(),
    phoneNumber: Yup.string().default('').defined(),
    street: Yup.string().default('').defined(),
    houseNumber: Yup.string().default('').defined(),
    houseNumberExtension: Yup.string().default('').defined(),
    postalCode: Yup.string().default('').defined(),
    city: Yup.string().default('').defined(),
    country: Yup.string().default('').defined(),
    language: Yup.string().default('').defined(),
    gender: Yup.string().default('UNKNOWN').defined(),
    birthDate: Yup.string().default('').defined(),
    groups: Yup.array()
      .of(Yup.string().required().default('').defined())
      .default([])
      .defined(),
    fields: Yup.array()
      .of(
        Yup.object()
          .shape({
            key: Yup.string().default('').defined(),
            value: Yup.string().default('').defined(),
          })
          .defined(),
      )
      .default([])
      .defined(),
    status: Yup.string().default('NEW').defined(),
  })
  .defined()

type MemberSchema = Yup.InferType<typeof schema>

type MemberFormProps = {
  value: any
  disabled: boolean
  groups: {code: string; name: string}[]
  fields: {name: string; type: string}[]
  languages: {name: string; alpha2: string}[]
  countries: {name: string; alpha2: string}[]
  onChange: (input: MemberSchema) => void
  onSubmit: (input: MemberSchema) => void
}

export function MemberForm({
  value,
  disabled,
  groups,
  fields,
  languages,
  countries,
  onChange,
  onSubmit,
}: MemberFormProps) {
  const [state, setState] = useState<MemberSchema>(value || schema.default())

  state
  useEffect(() => {
    setState(value || schema.default())
  }, [value])

  const resolverGroup = (code: string) => groups.find((it) => it.code === code)
  const resolveField = (key: string) =>
    state.fields.find((it) => it.key === key)

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
      fields: fields.map((field) => ({
        key: field.name,
        value:
          field.name === name
            ? Array.isArray(value)
              ? value.join(',')
              : value
            : resolveField(field.name)?.value || '',
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
          disabled={disabled}
          value={state.groups || []}
          input={<Input />}
          onChange={handleChangeGroup('groups')}
          renderValue={(selected: string[]) =>
            selected
              .map(resolverGroup)
              .map((it) => it.name)
              .join(',')
          }
        >
          {groups.map((it) => (
            <MenuItem key={it.code} value={it.code}>
              <Checkbox checked={state.groups.indexOf(it.code) > -1} />
              <ListItemText primary={it.name} />
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Grid>
  )

  const textField = (field) => (
    <TextField
      label={field.label}
      fullWidth
      disabled={disabled || field.disabled}
      value={resolveField(field.name)?.value || ''}
      onChange={handleChangeField(field.name)}
    />
  )

  const checkboxField = (field) => (
    <FormControlLabel
      disabled={disabled || field.disabled}
      control={
        <Checkbox
          onChange={handleChangeField(field.name)}
          checked={resolveField(field.name)?.value === 'true'}
          value={(resolveField(field.name)?.value !== 'true').toString()}
        />
      }
      label={field.label}
    />
  )

  const singleSelectField = (field) => (
    <FormControl fullWidth disabled={disabled || field.disabled}>
      <InputLabel htmlFor={field.name}>{field.label}</InputLabel>
      <Select
        value={resolveField(field.name)?.value || ''}
        input={<Input />}
        onChange={handleChangeField(field.name)}
      >
        {field.options.map((it) => (
          <MenuItem key={it} value={it}>
            <ListItemText primary={it} />
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  )

  const multiSelectField = (field) => (
    <FormControl fullWidth disabled={disabled || field.disabled}>
      <InputLabel htmlFor={field.name}>{field.label}</InputLabel>
      <Select
        multiple
        value={
          resolveField(field.name)?.value
            ? resolveField(field.name)?.value.split(',')
            : []
        }
        input={<Input />}
        onChange={handleChangeField(field.name)}
        renderValue={(selected: string[]) => selected.join(',')}
      >
        {field.options.map((it) => (
          <MenuItem key={it} value={it}>
            <Checkbox
              checked={
                (resolveField(field.name)?.value || '').split(',').indexOf(it) >
                -1
              }
            />
            <ListItemText primary={it} />
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  )

  const renderFieldsRow =
    fields &&
    fields.map((it) => (
      <Grid key={it.name} item xs={12}>
        {it.type === 'CHECKBOX' && checkboxField(it)}
        {it.type === 'TEXT' && textField(it)}
        {it.type === 'SINGLE_SELECT' && singleSelectField(it)}
        {it.type === 'MULTI_SELECT' && multiSelectField(it)}
      </Grid>
    ))

  return (
    <ValidatorForm id="member-form" onSubmit={onSubmit}>
      <Grid container spacing={1}>
        <Grid item xs={5}>
          <TextValidator
            required
            disabled={disabled}
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
            disabled={disabled}
            label="Infix"
            value={state.infix || ''}
            onChange={handleChange('infix')}
          />
        </Grid>

        <Grid item xs={5}>
          <TextValidator
            required
            disabled={disabled}
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
            disabled={disabled}
            label="Birth date"
            InputLabelProps={{shrink: true}}
            value={state.birthDate || ''}
            onChange={handleChange('birthDate')}
          />
        </Grid>

        <Grid item xs={5}>
          <FormControl fullWidth>
            <InputLabel htmlFor="gender">Gender</InputLabel>
            <Select
              required
              disabled={disabled}
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
            disabled={disabled}
            label="Phone number"
            value={state.phoneNumber || ''}
            onChange={handleChange('phoneNumber')}
          />
        </Grid>

        <Grid item xs={12}>
          <TextField
            fullWidth
            disabled={disabled}
            label="Email"
            value={state.email || ''}
            onChange={handleChange('email')}
          />
        </Grid>

        <Grid item xs={8}>
          <TextField
            fullWidth
            disabled={disabled}
            label="Street"
            value={state.street || ''}
            onChange={handleChange('street')}
          />
        </Grid>

        <Grid item xs={2}>
          <TextField
            fullWidth
            disabled={disabled}
            label="Nr"
            value={state.houseNumber || ''}
            onChange={handleChange('houseNumber')}
          />
        </Grid>

        <Grid item xs={2}>
          <TextField
            fullWidth
            disabled={disabled}
            label="Ext"
            value={state.houseNumberExtension || ''}
            onChange={handleChange('houseNumberExtension')}
          />
        </Grid>

        <Grid item xs={3}>
          <TextField
            fullWidth
            disabled={disabled}
            label="Postal code"
            value={state.postalCode || ''}
            onChange={handleChange('postalCode')}
          />
        </Grid>

        <Grid item xs={9}>
          <TextField
            fullWidth
            disabled={disabled}
            label="City"
            value={state.city || ''}
            onChange={handleChange('city')}
          />
        </Grid>

        <Grid item xs={6}>
          <FormControl disabled={disabled} fullWidth>
            <InputLabel htmlFor="country">Country</InputLabel>
            <Select
              value={state.country || ''}
              onChange={handleChange('country')}
              inputProps={{
                name: 'country',
                id: 'country',
              }}
            >
              <MenuItem value="">Unknown</MenuItem>
              {countries.map((country) => (
                <MenuItem
                  key={`country-${country.alpha2}`}
                  value={country.alpha2}
                >
                  {country.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={6}>
          <FormControl disabled={disabled} fullWidth>
            <InputLabel htmlFor="country">Language</InputLabel>
            <Select
              value={state.language || ''}
              onChange={handleChange('language')}
              inputProps={{
                name: 'language',
                id: 'language',
              }}
            >
              <MenuItem value="">Unknown</MenuItem>
              {languages.map((language) => (
                <MenuItem
                  key={`language-${language.alpha2}`}
                  value={language.alpha2}
                >
                  {language.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>

        {renderGroupsRow}
        {renderFieldsRow}
      </Grid>
    </ValidatorForm>
  )
}
