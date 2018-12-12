import React from "react";

import {withStyles} from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';


const styles = theme => ({
  textField: {
    width: "100%",
  },
});

class MemberForm extends React.Component {

  constructor(props) {
    super(props);
    this.init()
  }

  init() {
    this.state = {
      search: ''
    };
  }

  handleChange(name) {
    return (event) => {
      this.setState(
        {[name]: event.target.value},
        () => {
          this.props.onChange(this.state)
        })

    }
  };

  render() {
    const {classes, item} = this.props;

    return (
      <TextField
        label="Search"
        value={this.state.search}
        className={classes.textField}
        onChange={this.handleChange('search')}/>
    )
  }
}

export default withStyles(styles)(MemberForm);