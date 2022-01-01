import React, {useState} from 'react'

import Grid from '@material-ui/core/Grid'
import FormControl from '@material-ui/core/FormControl'
import InputLabel from '@material-ui/core/InputLabel'
import Select from '@material-ui/core/Select'
import TextField from '@material-ui/core/TextField'
import MenuItem from '@material-ui/core/MenuItem'

type MemberFieldFormInput = {
  name: string
  label: string
  type: string
  options: string[]
}

type MemberFieldFormProps = {
  value: MemberFieldFormInput
  onChange: (value: MemberFieldFormInput) => void
}

export function MemberFieldForm({value, onChange}: MemberFieldFormProps) {
  const [state, setState] = useState<MemberFieldFormInput>({
    ...value,
  })

  const isSelect = ['SINGLE_SELECT', 'MULTI_SELECT'].includes(state.type)

  const handleChange = name => event => {
    const value = {
      ...state,
      options: isSelect ? state.options : [],
      [name]: event.target.value,
    }
    setState(value)
    onChange(value)
  }

  const handleOptionsChange = (options: string[]) => {
    const value = {...state, options}
    setState(value)
    onChange(value)
  }

  return (
    <Grid container direction="column" alignItems="stretch" spacing={1}>
      <Grid item xs={12}>
        <TextField
          fullWidth
          label="Name"
          value={state.name || ''}
          required
          onChange={handleChange('name')}
        />
      </Grid>

      <Grid item xs={12}>
        <TextField
          fullWidth
          label="Label"
          value={state.label || ''}
          required
          onChange={handleChange('label')}
        />
      </Grid>

      <Grid item xs={12}>
        <FormControl fullWidth>
          <InputLabel htmlFor="age-helper">Type</InputLabel>
          <Select
            value={state.type || 'CHECKBOX'}
            onChange={handleChange('type')}
          >
            <MenuItem value="CHECKBOX">Checkbox</MenuItem>
            <MenuItem value="TEXT">Text</MenuItem>
            <MenuItem value="SINGLE_SELECT">Single Select</MenuItem>
            <MenuItem value="MULTI_SELECT">Multi Select</MenuItem>
          </Select>
        </FormControl>
      </Grid>

      {isSelect && (
        <Grid item xs={12}>
          <TextField
            fullWidth
            label="Options"
            value={state.options || ''}
            onChange={ev => {
              handleOptionsChange(ev.target.value.split(','))
            }}
          />
        </Grid>
      )}
    </Grid>
  )
}
