import React from 'react'
import {withStyles} from '@material-ui/core/styles'
import Paper from '@material-ui/core/Paper'

const styles = theme => ({
  tablePaper: {
    marginBottom: 50,
    width: '100%',
    marginTop: theme.spacing.unit * 3,
    overflowX: 'auto',
  },
})

class ProfileFeature extends React.Component {

  state = {}

  componentDidMount() {
    fetch('/api/users/me')
      .then(res => res.json())
      .then(json => {
        this.setState({user: json.content})
      })
  }

  render() {
    const {classes} = this.props
    const {user} = this.state

    if(!user)
      return <h1>test</h1>

    return (
      <div>
        <Paper className={classes.tablePaper}>
          <p>Name: {user.name}</p>
          <p>Email: {user.email}</p>
          {user.authorities.length > 0 && <p>Roles: </p>}
          {user.authorities.length > 0 && <ul>{user.authorities
            .map(it => (<li key={it}>{it}</li>))}
          </ul>}
        </Paper>
      </div>
    )
  }
}

export default withStyles(styles)(ProfileFeature)
