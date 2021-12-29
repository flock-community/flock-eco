import React, {ChangeEvent, useState} from 'react'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import Box from '@material-ui/core/Box'

import {makeStyles} from '@material-ui/core/styles'
import PasswordStrengthBar from 'react-password-strength-bar'
import {Typography} from '@material-ui/core'
import {useTranslation} from 'react-i18next'

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
  onSubmit?: (password: string, score?: number) => void
}

export const UserLoginResetForm = ({onSubmit}: UserLoginResetFormProps) => {
  const classes = useStyles()

  const {t} = useTranslation(['translation'])

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
      onSubmit?.(password, score)
    } else {
      setMessage(t('eco.feature.user.passwordNoEqual'))
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
        <PasswordStrengthBar
          password={password ?? ''}
          onChangeScore={setScore}
        />
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
          {t('eco.feature.user.choosePassword')}
        </Button>
      </form>
    </Box>
  )
}
