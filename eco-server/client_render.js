import 'babel-polyfill';
import React from 'react';
import ReactDOM from 'react-dom';
import MemberFeature from 'eco-feature-member/member/MemberFeature'

ReactDOM.render(
  <MemberFeature />,
  document.querySelector('#root'),
);