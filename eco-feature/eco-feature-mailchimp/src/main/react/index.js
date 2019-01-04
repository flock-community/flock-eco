import React from 'react'
import ReactDOM from 'react-dom'

import MailchimpMemberTable from './member/MailchimpMemberTable'
import MailchimpTemplateTable from './template/MailchimpTemplateTable'
import MailchimpCampaignTable from './campaign/MailchimpCampaignTable'

class App extends React.Component {
  render() {
    return (
      <React.Fragment>
        <MailchimpMemberTable />
        <MailchimpTemplateTable />
        <MailchimpCampaignTable />
      </React.Fragment>
    )
  }
}

ReactDOM.render(<App />, document.getElementById('index'))
