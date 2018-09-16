import React from "react";
import {withStyles} from '@material-ui/core/styles';

import Grid from '@material-ui/core/Grid';

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import TextField from '@material-ui/core/TextField';
import Checkbox from '@material-ui/core/Checkbox';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';

const styles = theme => ({
  input: {
    width: '100%'
  },
});

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
              className={classes.input}
              value={this.state.name}
              onChange={ev => this.handleChange('name')(ev.target.value)}/>
          </Grid>

          <Grid item>
            <TextField
              label="Email"
              className={classes.input}
              value={this.state.email}
              onChange={ev => this.handleChange('email')(ev.target.value)}/>
          </Grid>

          <Grid item>
            <TextField
              label="Reference"
              className={classes.input}
              value={this.state.reference}
              onChange={ev => this.handleChange('reference')(ev.target.value)}/>
          </Grid>

          <Grid item style={{marginTop:10}}>
            {this.renderList()}
          </Grid>

        </Grid>
      </React.Fragment>)
  }


  renderList() {

    const {classes, authorities} = this.props;

    const handleClick = (value) => {
      if (this.state.authorities.includes(value)) {
        this.handleChange('authorities')(this.state.authorities.filter(it => value !== it))
      } else {
        this.handleChange('authorities')(this.state.authorities.concat(value))
      }

    }

    return (
      <FormControl className={classes.formControl}>
        <InputLabel shrink>Authorities</InputLabel>
        <List
          className={classes.input}>
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
      </FormControl>
    )
  }

}


export default withStyles(styles)(UserForm);