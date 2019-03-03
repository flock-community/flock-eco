import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
const styles = theme => ({});
class UserDialog extends React.Component {
    constructor() {
        super(...arguments);
        this.handleClose = () => {
            this.props.onClose();
        };
        this.handleSave = () => {
            this.props.onSave();
        };
        this.handleDelete = () => {
            this.props.onDelete();
        };
    }
    render() {
        const { classes, open, onClose, selectedValue } = this.props;
        return (React.createElement(Dialog, { fullWidth: true, maxWidth: 'md', open: open, onClose: this.handleClose, "aria-labelledby": "simple-dialog-title", classes: classes },
            React.createElement(DialogTitle, { id: "simple-dialog-title" }, "User"),
            React.createElement(DialogContent, null, this.props.children),
            React.createElement(DialogActions, null,
                React.createElement(Button, { onClick: this.handleClose, color: "primary" }, "Cancel"),
                React.createElement(Button, { onClick: this.handleDelete, color: "secondary" }, "Delete"),
                React.createElement(Button, { onClick: this.handleSave, color: "primary", autoFocus: true }, "Save"))));
    }
}
export default withStyles(styles)(UserDialog);
