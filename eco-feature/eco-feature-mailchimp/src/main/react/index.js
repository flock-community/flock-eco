import React from "react";
import ReactDOM from "react-dom";

import MailchimpTemplateTable from "./template/MailchimpTemplateTable";
import MailchimpCampaignTable from "./template/MailchimpCampaignTable";

class App extends React.Component {

  render() {
    return (
      <React.Fragment>
        <MailchimpTemplateTable/>
        <MailchimpCampaignTable/>
      </React.Fragment>
    )
  }
};

ReactDOM.render(
  <App/>,
  document.getElementById("index"));
