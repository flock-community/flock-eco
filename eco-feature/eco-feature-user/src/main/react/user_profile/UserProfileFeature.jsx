import React, {useEffect, useState} from 'react'
import UserClient from '../user/UserClient'
import makeStyles from '@material-ui/core/styles/makeStyles'
import {Typography} from '@material-ui/core'
import List from '@material-ui/core/List'
import ListItemText from '@material-ui/core/ListItemText'
import Grid from '@material-ui/core/Grid'

const useStyles = makeStyles(theme => ({}))

/**
 * @return {null}
 */
export function UserProfileFeature({value}) {
  const classes = useStyles()

  const [user, setUser] = useState(null)

  useEffect(() => {
    if (value) {
      setUser({
        ...value,
        authorities: !value.authorities ? [] : value.authorities,
      })
    }
    UserClient.findUsersMe().then(json => {
      setUser(json)
    })
  }, [])

  if (!user) return null

  return (
    <Grid container spacing={1}>
      <Grid item xs={12} sm={6}>
        <Typography variant="h6">{user.name}</Typography>
        <Typography>{user.email}</Typography>
      </Grid>
      <Grid item xs={12} sm={6}>
        <Typography variant="h6">Authorities</Typography>
        {user.authorities.length > 0 && (
          <List disablePadding>
            {user.authorities.map(it => (
              <ListItemText key={`profile-authority-${it}`}>{it}</ListItemText>
            ))}
          </List>
        )}
      </Grid>
    </Grid>
  )
}
