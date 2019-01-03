import 'babel-polyfill';
import React from "react";
import ReactDOM from "react-dom";

import MemberFeature from "./member/MemberFeature";
import MemberGroupManager from "./member_group/MemberGroupFeature";
import MemberFieldFeature from "./member_field/MemberFieldFeature";

class App extends React.Component {

  render() {
    return (
      <MemberFeature/>
    )
  }
};

ReactDOM.render(
  <App/>,
  document.getElementById("index"));
