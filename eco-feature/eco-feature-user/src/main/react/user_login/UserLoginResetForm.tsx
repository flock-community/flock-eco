import React, {ChangeEvent, useState} from 'react'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import Box from '@material-ui/core/Box'

import {makeStyles} from '@material-ui/core/styles'
import PasswordStrengthBar from 'react-password-strength-bar'
import {InputProps, Typography} from '@material-ui/core'

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}))

type UserLoginResetFormProps = {
  onReset?: (password: string) => void
}

export const UserLoginResetForm = ({onReset}: UserLoginResetFormProps) => {
  const classes = useStyles()

  const [message, setMessage] = useState<string>()
  const [password, setPassword] = useState<string>()
  const [rePassword, setRePassword] = useState<string>()
  const [score, setScore] = useState<number>()

  const handleChangePassword = (value: ChangeEvent<HTMLInputElement>) => {
    setPassword(value.target.value)
  }

  const handleChangeRePassword = (value: ChangeEvent<HTMLInputElement>) => {
    setRePassword(value.target.value)
  }

  const handleReset = () => {
    if (password !== undefined && password === rePassword) {
      setMessage(undefined)
      onReset?.(password)
    } else {
      setMessage('Password not equal')
    }
  }

  return (
    <Box className={classes.root}>
      <form className={classes.form} noValidate method="post" action="/login">
        <TextField
          margin="normal"
          required
          fullWidth
          name="password"
          label="Password"
          type="password"
          id="password"
          autoComplete="current-password"
          onChange={handleChangePassword}
        />
        <PasswordStrengthBar password={password} onChangeScore={setScore} />
        <TextField
          margin="normal"
          required
          fullWidth
          label="Re-password"
          type="password"
          id="re-password"
          autoComplete="current-password"
          onChange={handleChangeRePassword}
        />
        <Typography variant={'caption'}>{message}</Typography>
        <Button
          fullWidth
          variant="contained"
          color="primary"
          className={classes.submit}
          onClick={handleReset}
        >
          Reset
        </Button>
      </form>
    </Box>
  )
}
