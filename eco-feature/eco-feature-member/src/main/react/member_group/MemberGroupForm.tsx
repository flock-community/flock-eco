import React, {useState} from 'react'

import Grid from '@material-ui/core/Grid'
import TextField from '@material-ui/core/TextField'

type MemberGroupFormInput = {
  name: string
  code: string
}

type MemberGroupProps = {
  value: MemberGroupFormInput
  onChange: (value: MemberGroupFormInput) => void
}

export function MemberGroupForm({value, onChange}: MemberGroupProps) {
  const [state, setState] = useState<MemberGroupFormInput>({
    ...value,
  })

  const handleChange = (name) => (event) => {
    const value = {
      ...state,
      [name]: event.target.value,
    }
    setState(value)
    onChange(value)
  }

  return (
    <Grid container direction="column" alignItems="stretch">
      <TextField
        label="Code"
        value={state.code}
        onChange={handleChange('code')}
      />

      <TextField
        label="Name"
        value={state.name}
        onChange={handleChange('name')}
      />
    </Grid>
  )
}
