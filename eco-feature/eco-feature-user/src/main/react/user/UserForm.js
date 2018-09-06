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
    this.init(props.item)
  }

  init(item) {
    const user = {
      name: "",
      disabled: false,
      authorities: [],
    };

    console.log('----', Object.assign(user, item))
    this.state = Object.assign(user, item);
  }

  handleChange(name) {
    return (value) => {
      console.log(value)
      this.setState(
        {[name]: value},
        () => {
          this.props.onChange(this.state)
        })

    }
  };

  render() {

    const {classes} = this.props;

    return (
      <React.Fragment>
        <Grid
          container
          direction="column"
          justify="space-evenly"
          alignItems="stretch"
        >
          <Grid item>
            <TextField
              label="Name"
              value={this.state.name}
              onChange={ev => this.handleChange('name')(ev.target.value)}/>
          </Grid>

          <Grid item>
            {this.renderList()}
          </Grid>

        </Grid>
      </React.Fragment>)
  }


  renderList() {

    console.log('++++', this.state.authorities)

    const {classes, authorities} = this.props;

    const handleClick = (value) => {
      if (this.state.authorities.includes(value)) {
        this.handleChange('authorities')(this.state.authorities.filter(it => value !== it))
      } else {
        this.handleChange('authorities')(this.state.authorities.concat(value))
      }

    }

    return (
      <List
        style={{
          position: 'relative',
          overflow: 'auto',
          maxHeight: 200,
        }}
      >
        {authorities.map(value => (
          <ListItem
            key={value}
            role={undefined}
            dense
            button
            onClick={ev => handleClick(value)}
            style={{
              margin: 0,
              padding: 0
            }}
          >
            <Checkbox
              checked={this.state.authorities.includes(value)}
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