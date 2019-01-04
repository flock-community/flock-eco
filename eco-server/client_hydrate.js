import 'babel-polyfill';

import React from 'react'
import ReactDOM from 'react-dom'
import MemberFeature from 'eco-feature-member/member/MemberFeature'
import JssProvider from 'react-jss/lib/JssProvider';
import {
  MuiThemeProvider,
  createMuiTheme,
  createGenerateClassName,
} from '@material-ui/core/styles';


class Main extends React.Component {
  state = {
    a: 1
  }
  // Remove the server-side injected CSS.
  componentDidMount() {
    const jssStyles = document.getElementById('jss-server-side');
    if (jssStyles && jssStyles.parentNode) {
      jssStyles.parentNode.removeChild(jssStyles);
    }
  }

  render() {
    return <MemberFeature/>
  }
}

// Create a new class name generator.
const generateClassName = createGenerateClassName();


ReactDOM.hydrate( <JssProvider generateClassName={generateClassName}>
    <Main />
</JssProvider>, document.querySelector('#root'))
