import React, {ChangeEvent, useState} from 'react'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import Box from '@material-ui/core/Box'

import {makeStyles} from '@material-ui/core/styles'
import PasswordStrengthBar from 'react-password-strength-bar'
import {InputAdornment} from '@material-ui/core'
import {useTranslation} from 'react-i18next'
import {passgarble} from '@flock/pass-garble'
import {FileCopy, Visibility, VisibilityOff} from '@material-ui/icons'
import {Alert} from '@material-ui/lab'

type PasswordGenerationOptions = passgarble.PasswordGenerationOptions
type PasswordGenerator = passgarble.PasswordGenerator

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
  hidden: {
    display: 'none',
  },
  clickable: {
    cursor: 'pointer',
  },
}))

type UserLoginResetFormProps = {
  onSubmit?: (password: string, score?: number) => void
}

export const UserLoginResetForm = ({onSubmit}: UserLoginResetFormProps) => {
  const classes = useStyles()

  const [generator] = useState<PasswordGenerator>(
    new passgarble.PasswordGenerator(),
  )
  const {t} = useTranslation(['translation'])
  const [message, setMessage] = useState<string>()
  const [password, setPassword] = useState<string>('')
  const [rePassword, setRePassword] = useState<string>('')
  const [score, setScore] = useState<number>()
  const [visibility, setVisibility] = useState<boolean>(false)

  const handleChangePassword = (value: ChangeEvent<HTMLInputElement>) => {
    setPassword(value.target.value)
  }

  const handleChangeRePassword = (value: ChangeEvent<HTMLInputElement>) => {
    setRePassword(value.target.value)
  }

  const createRandomPassword = () => {
    const passwordOptions: PasswordGenerationOptions = passgarble.defaultOptions()
    const newPassword = generator.generatePassword(passwordOptions)
    setPassword(newPassword)
    setRePassword(newPassword)
    setVisibility(false)
    setMessage(t('eco.feature.user.passwordGenerated'))
  }

  const handleReset = () => {
    if (password !== undefined && password === rePassword) {
      setMessage(undefined)
      onSubmit?.(password, score)
    } else {
      setMessage(t('eco.feature.user.passwordNoEqual'))
    }
  }

  const copyPasswordToClipboard = () => {
    navigator.clipboard.writeText(password).then()
    setMessage(t('eco.feature.user.passwordCopied'))
  }

  const toggleVisibility = () => {
    setVisibility(!visibility)
  }

  return (
    <Box className={classes.root}>
      <form className={classes.form} noValidate method="post" action="/login">
        <TextField
          className={classes.hidden}
          margin="normal"
          fullWidth
          id="username"
          label="Username"
          name="username"
          autoComplete="username"
          hidden={true}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          name="password"
          label="Password"
          type={visibility ? 'text' : 'password'}
          id="password"
          autoComplete="current-password"
          onChange={handleChangePassword}
          value={password}
          InputProps={{
            endAdornment: (
              <InputAdornment className={classes.clickable} position="end">
                <FileCopy onClick={copyPasswordToClipboard} />
                {visibility ? (
                  <Visibility onClick={toggleVisibility} />
                ) : (
                  <VisibilityOff onClick={toggleVisibility} />
                )}
              </InputAdornment>
            ),
          }}
        />
        <Button onClick={createRandomPassword}>
          {t('eco.feature.user.generatePassword')}
        </Button>
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
          value={rePassword}
        />
        {message ? <Alert severity="warning">{message}</Alert> : ''}
        <Button
          fullWidth
          variant="contained"
          color="primary"
          className={classes.submit}
          onClick={handleReset}
        >
          {t('eco.feature.user.savePassword')}
        </Button>
      </form>
    </Box>
  )
}
