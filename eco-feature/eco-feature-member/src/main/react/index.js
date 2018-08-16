import React from "react";
import ReactDOM from "react-dom";

import MemberFeature from "./member/MemberFeature";
import MemberGroupManager from "./member_group/MemberGroupFeature";

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
