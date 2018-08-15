import React from "react";

import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';

import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';


const styles = theme => ({
  paper:{
    width:'800px'
  }
});

class UserDialog extends React.Component {

  constructor(props){
    super(props);
    this.handleClose = () => {
      this.props.onClose();
    };

    this.handleSave = () => {
      this.props.onSave();
    };

  }

  render() {
    const { classes, open, onClose, selectedValue } = this.props;

    return (
      <Dialog
        open={open}
        onClose={this.handleClose}
        aria-labelledby="simple-dialog-title"
        classes={classes}
      >
        <DialogTitle id="simple-dialog-title">Set backup account</DialogTitle>
        <DialogContent>
          {this.props.children}
        </DialogContent>
        <DialogActions>
          <Button onClick={this.handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={this.handleDelete} color="secondary">
            Delete
          </Button>
          <Button onClick={this.handleSave} color="primary" autoFocus>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    );
  }
}

export default withStyles(styles)(UserDialog);