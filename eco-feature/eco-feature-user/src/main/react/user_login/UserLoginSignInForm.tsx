import React from 'react'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import Checkbox from '@material-ui/core/Checkbox'
import Link from '@material-ui/core/Link'
import Grid from '@material-ui/core/Grid'
import Box from '@material-ui/core/Box'

import {makeStyles} from '@material-ui/core/styles'

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}))

type UserLoginFormProps = {
  remember: boolean
  reset?: () => void
  forgot?: () => void
}

export const UserLoginSignInForm = ({
  remember,
  reset,
  forgot,
}: UserLoginFormProps) => {
  const classes = useStyles()

  return (
    <Box className={classes.root}>
      <form className={classes.form} noValidate method="post" action="/login">
        <TextField
          margin="normal"
          required
          fullWidth
          id="username"
          label="Username"
          name="username"
          autoComplete="username"
          autoFocus
        />
        <TextField
          margin="normal"
          required
          fullWidth
          name="password"
          label="Password"
          type="password"
          id="password"
          autoComplete="current-password"
        />
        {remember && (
          <FormControlLabel
            control={<Checkbox value="remember" color="primary" />}
            label="Remember me"
          />
        )}
        <Button
          type="submit"
          fullWidth
          variant="contained"
          color="primary"
          className={classes.submit}
        >
          Sign In
        </Button>
        <Grid container>
          {forgot && (
            <Grid item xs>
              <Link href="#" variant="body2" onClick={forgot}>
                Forgot password?
              </Link>
            </Grid>
          )}
          {reset && (
            <Grid item>
              <Link href="#" variant="body2" onClick={reset}>
                Sign Up
              </Link>
            </Grid>
          )}
        </Grid>
      </form>
    </Box>
  )
}
