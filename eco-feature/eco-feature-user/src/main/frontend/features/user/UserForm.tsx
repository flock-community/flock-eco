import * as React from 'react'
import {Component} from 'react'

import {createStyles, WithStyles} from '@material-ui/core'
import {Theme} from '@material-ui/core/styles/createMuiTheme'
import {withStyles} from '@material-ui/core/styles'

import Grid from '@material-ui/core/Grid'

import List from '@material-ui/core/List'
import ListItem from '@material-ui/core/ListItem'
import ListItemText from '@material-ui/core/ListItemText'

import TextField from '@material-ui/core/TextField'
import Checkbox from '@material-ui/core/Checkbox'
import FormControl from '@material-ui/core/FormControl'
import InputLabel from '@material-ui/core/InputLabel'
import {User} from '../../../../../generated/src/main/frontend/models/user.model.json'
import {AuthoritiesClient} from '../../clients/AuthoritiesClient'

const styles = (theme: Theme) => createStyles({
  input: {
    width: '100%',
  },
})

interface Props<T> extends WithStyles<typeof styles> {
  value: T,
  authorities:string[]

  onChange(item: T): Function
}

class UserForm extends Component<Props<User>, User> {

  state: User = {
    name: '',
    email: '',
    reference: '',
    authorities: [],
  }

  componentDidUpdate(prevProps: Props<User>) {
    if (prevProps.value !== this.props.value) {
      this.setState(this.props.value)
    }
  }

  handleChangeEvent(name: string) {
    return (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>): void => {
      this.setState({[name]: event.target.value}, () => {
        this.props.onChange(this.state)
      })
    }
  }

  handleChangeValue(name: string) {
    return (authorities: string[]): void => {
      this.setState({[name]: authorities}, () => {
        this.props.onChange(this.state)
      })
    }
  }


  render() {
    const {classes} = this.props

    return (
      <React.Fragment>
        <Grid
          container
          direction="column"
          justify="space-evenly"
          alignItems="stretch"
          spacing={16}
        > <Grid item xs={12}>
          <TextField
            label="Email"
            className={classes.input}
            value={this.state.email}
            onChange={this.handleChangeEvent('email')}
          />
        </Grid>

          <Grid item xs={12}>
            <TextField
              label="Reference"
              className={classes.input}
              value={this.state.reference}
              onChange={this.handleChangeEvent('reference')}
            />
          </Grid>

          <Grid item style={{marginTop: 10}}>
            {this.renderList()}
          </Grid>

        </Grid>
      </React.Fragment>
    )
  }

  renderList() {
    const {classes, authorities} = this.props

    const handleClick = (value: string) => {
      if (this.state.authorities.indexOf(value) > 0) {
        this.handleChangeValue('authorities')(this.state.authorities
          .filter(it => value !== it))
      } else {
        this.handleChangeValue('authorities')(this.state.authorities
          .concat(value))
      }
    }

    return (
      <FormControl>
        <InputLabel shrink>Authorities</InputLabel>
        <List className={classes.input}>
          {authorities.map(value => (
            <ListItem
              className={classes.input}
              key={value}
              role={undefined}
              dense
              button
              onClick={ev => handleClick(value)}
              style={{
                margin: 0,
                padding: 0,
              }}
            >
              <Checkbox
                checked={this.state.authorities.indexOf(value) > 0}
                tabIndex={-1}
                disableRipple
                style={{
                  width: 32,
                }}
              />
              <ListItemText primary={value}/>
            </ListItem>
          ))}
        </List>
      </FormControl>
    )
  }
}

export default withStyles(styles)(UserForm)
