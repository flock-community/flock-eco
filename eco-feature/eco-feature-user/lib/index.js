import React from 'react';
import ReactDOM from 'react-dom';
import UserManager from './features/user/UserFeature';
class App extends React.Component {
    render() {
        const divStyle = {
            background: 'red',
        };
        return React.createElement(UserManager, null);
    }
}
ReactDOM.render(React.createElement(App, null), document.getElementById('index'));
