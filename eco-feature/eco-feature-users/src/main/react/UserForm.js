import React from "react";

import Grid from '@material-ui/core/Grid';

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import TextField from '@material-ui/core/TextField';
import Checkbox from '@material-ui/core/Checkbox';

class UserForm extends React.Component {

  constructor(props) {
    super(props);
    this.state = {user: props.user || {}};
    this.updateName = (ev) => {
      console.log(ev)
    }

    this.handleChange = name => event => {
      this.setState({
        user: {
          [name]: event.target.value,
        }
      });
    };
  }

  render() {

    const {classes, user, authorities} = this.props;

    return (
      <Grid
        container
        direction="column"
        justify="space-evenly"
        alignItems="stretch"
      >
        <TextField
          label="Name"
          value={this.state.user.name}
          onChange={this.handleChange('name')}/>
        <AuthorityList
          authorities={authorities}
          onChange={this.handleChange('authorities')}/>

      </Grid>
    )
  }

}

class AuthorityList extends React.Component {

  constructor(props) {
    super(props);
    this.state = {selected: new Set(['UserAuthority.READ'])};
    this.handleToggle = value => ev => {
      const {selected} = this.state;

      if (selected.has(value)) {
        selected.delete(value);
      } else {
        selected.add(value)
      }

      this.setState({
        selected: selected,
      });

      props.onChange(selected)
    };
  }

  render() {

    const {authorities} = this.props

    return (
      <List
        style={{
          position: 'relative',
          overflow: 'auto',
          maxHeight: 250,
        }}
      >
        {authorities.map(value => (
        <ListItem
          key={value}
          role={undefined}
          dense
          button
          onClick={this.handleToggle(value)}
          style={{
            margin: 0,
            padding: 0
          }}
        >
          <Checkbox
            checked={this.state.selected.has(value)}
            tabIndex={-1}
            disableRipple
            style={{
              width: 32
            }}
          />
          <ListItemText
            primary={value}
          />
        </ListItem>
        ))}
      </List>
    )
  }

}


export default UserForm;