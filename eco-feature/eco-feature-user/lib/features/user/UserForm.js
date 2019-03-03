import * as React from 'react';
import { Component } from 'react';
import { createStyles } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import TextField from '@material-ui/core/TextField';
import Checkbox from '@material-ui/core/Checkbox';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
const styles = (theme) => createStyles({
    input: {
        width: '100%',
    },
});
class UserForm extends Component {
    constructor() {
        super(...arguments);
        this.state = {
            name: '',
            email: '',
            reference: '',
            authorities: [],
        };
    }
    componentDidUpdate(prevProps) {
        if (prevProps.value !== this.props.value) {
            this.setState(this.props.value);
        }
    }
    handleChangeEvent(name) {
        return (event) => {
            this.setState({ [name]: event.target.value }, () => {
                this.props.onChange(this.state);
            });
        };
    }
    handleChangeValue(name) {
        return (authorities) => {
            this.setState({ [name]: authorities }, () => {
                this.props.onChange(this.state);
            });
        };
    }
    render() {
        const { classes } = this.props;
        return (React.createElement(React.Fragment, null,
            React.createElement(Grid, { container: true, direction: "column", justify: "space-evenly", alignItems: "stretch", spacing: 16 },
                " ",
                React.createElement(Grid, { item: true, xs: 12 },
                    React.createElement(TextField, { label: "Email", className: classes.input, value: this.state.email, onChange: this.handleChangeEvent('email') })),
                React.createElement(Grid, { item: true, xs: 12 },
                    React.createElement(TextField, { label: "Reference", className: classes.input, value: this.state.reference, onChange: this.handleChangeEvent('reference') })),
                React.createElement(Grid, { item: true, style: { marginTop: 10 } }, this.renderList()))));
    }
    renderList() {
        const { classes, authorities } = this.props;
        const handleClick = (value) => {
            if (this.state.authorities.indexOf(value) > 0) {
                this.handleChangeValue('authorities')(this.state.authorities
                    .filter(it => value !== it));
            }
            else {
                this.handleChangeValue('authorities')(this.state.authorities
                    .concat(value));
            }
        };
        return (React.createElement(FormControl, null,
            React.createElement(InputLabel, { shrink: true }, "Authorities"),
            React.createElement(List, { className: classes.input }, authorities.map(value => (React.createElement(ListItem, { className: classes.input, key: value, role: undefined, dense: true, button: true, onClick: ev => handleClick(value), style: {
                    margin: 0,
                    padding: 0,
                } },
                React.createElement(Checkbox, { checked: this.state.authorities.indexOf(value) > 0, tabIndex: -1, disableRipple: true, style: {
                        width: 32,
                    } }),
                React.createElement(ListItemText, { primary: value })))))));
    }
}
export default withStyles(styles)(UserForm);
