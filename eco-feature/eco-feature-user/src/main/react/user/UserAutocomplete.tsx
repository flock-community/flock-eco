import React, {useEffect} from 'react'
import TextField from '@material-ui/core/TextField'
import UserClient from './UserClient'
import {Autocomplete, Value} from '@material-ui/lab'
import {User} from '../graphql/user'

type UserAutocompleteProps = {
  value?: string[]
  onChange?: (value: any[]) => void
}

export function UserAutocomplete({value, onChange}: UserAutocompleteProps) {
  const [state, setState] = React.useState<User[]>([])
  const [inputValue, setInputValue] = React.useState('')
  const [options, setOptions] = React.useState([])

  useEffect(() => {
    UserClient.findAllUserByids(state || []).then((res) => {
      setState(res)
    })
  }, [value])

  React.useEffect(() => {
    UserClient.findAllUsers(inputValue, 0, 20).then((res) => {
      const ids = state.map((it) => it.id)
      setOptions(res.list.filter((it) => !ids.includes(it.id)).slice(0, 10))
    })
  }, [state, inputValue])

  return (
    <Autocomplete
      id="combo-box-demo"
      options={options}
      autoComplete
      includeInputInList
      filterSelectedOptions
      value={state}
      multiple
      getOptionLabel={(option) =>
        option ? `${option.name} <${option.email}>` : ''
      }
      renderInput={(params) => (
        <TextField {...params} label="Combo box" variant="outlined" />
      )}
      onChange={(event, newValue) => {
        setState(newValue)
        onChange?.(newValue.map((it) => it.id))
      }}
      onInputChange={(event, newInputValue) => {
        setInputValue(newInputValue)
      }}
    />
  )
}
