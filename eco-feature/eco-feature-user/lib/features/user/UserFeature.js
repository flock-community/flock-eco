import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import AddIcon from '@material-ui/icons/Add';
import UserTable from './UserTable';
import UserForm from './UserForm';
import UserDialog from './UserDialog';
import Paper from '@material-ui/core/es/Paper/Paper';
import Fab from '@material-ui/core/Fab';
const styles = theme => ({
    tablePaper: {
        marginBottom: 50,
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
    },
    button: {
        position: 'fixed',
        right: 20,
        bottom: 20,
        margin: theme.spacing.unit,
    },
});
class itemManager extends React.Component {
    constructor() {
        super(...arguments);
        this.state = {
            size: 10,
            members: this.props.members || [],
            item: null,
            groups: [],
            count: 0,
            page: 0,
        };
        this.handleRowClick = item => {
            console.log('123');
            this.setState({ item });
        };
        this.handleNewClick = () => {
            this.setState({ item: {} });
        };
        this.handleClose = value => {
            this.setState({ item: null });
        };
        this.handleSave = value => {
            if (this.state.item.id) {
                this.update(this.state.item);
            }
            else {
                this.create(this.state.item);
            }
        };
        this.handleDelete = value => {
            this.delete(this.state.item);
        };
        this.handleFormUpdate = value => {
            this.setState({ item: value });
        };
    }
    componentDidMount() {
        fetch('/api/authorities')
            .then(res => res.json())
            .then(json => {
            this.setState({ authorities: json });
        });
        //this.load()
    }
    render() {
        const { classes } = this.props;
        return (React.createElement("div", null,
            React.createElement(Paper, { className: classes.tablePaper },
                React.createElement(UserTable, { onRowClick: this.handleRowClick })),
            React.createElement(UserDialog, { open: this.state.item != null, onClose: this.handleClose, onSave: this.handleSave, onDelete: this.handleDelete },
                React.createElement(UserForm, { authorities: this.state.authorities, item: this.state.item, onChange: this.handleFormUpdate })),
            React.createElement(Fab, { color: "primary", "aria-label": "Add", className: classes.button, onClick: this.handleNewClick },
                React.createElement(AddIcon, null))));
    }
    create(item) {
        const opts = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
            },
            body: JSON.stringify(item),
        };
        fetch('/api/users', opts).then(() => {
            this.setState({ item: null });
            //this.load()
        });
    }
    update(item) {
        const opts = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
            },
            body: JSON.stringify(item),
        };
        fetch(`/api/users/${item.id}`, opts).then(() => {
            this.setState({ item: null });
            this.load();
        });
    }
    delete(item) {
        const opts = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
            },
        };
        fetch(`/api/users/${item.id}`, opts).then(() => {
            this.setState({ item: null });
            //this.load()
        });
    }
}
export default withStyles(styles)(itemManager);
