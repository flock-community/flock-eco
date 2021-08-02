import React from 'react'
import ReactDOM from 'react-dom'
import {App} from './App'

fetch('http://localhost:3000/login', {
  headers: {
    'content-type': 'application/x-www-form-urlencoded',
  },
  body: 'username=test&password=test',
  method: 'POST',
}).then(() => {
  ReactDOM.render(<App />, document.getElementById('index'))
})
