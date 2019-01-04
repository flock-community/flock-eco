import express from 'express'
import React from 'react'
import ReactDOMServer from 'react-dom/server'
import {SheetsRegistry} from 'jss'
import JssProvider from 'react-jss/lib/JssProvider'
import {createGenerateClassName, createMuiTheme, MuiThemeProvider} from '@material-ui/core/styles'
import green from '@material-ui/core/colors/green'
import red from '@material-ui/core/colors/red'
import MemberFeature from 'eco-feature-member/member/MemberFeature'

const data = [
  {
    'id': 16,
    'firstName': 'first-name-0',
    'infix': null,
    'surName': 'sur-name-0',
    'email': '0@email',
    'phoneNumber': null,
    'street': null,
    'houseNumber': null,
    'houseNumberExtension': null,
    'postalCode': null,
    'city': null,
    'country': null,
    'gender': 'MALE',
    'birthDate': null,
    'groups': [
      {
        'id': 3,
        'code': 'GROUP_3',
        'name': 'Group_3',
      },
      {
        'id': 4,
        'code': 'GROUP_4',
        'name': 'Group_4',
      },
    ],
    'fields': {
      'field_text': '123',
      'field_checkbox': 'true',
      'field_single_select': 'Option 1',
      'field_multi_select': 'Option 3,Option 4',
    },
    'status': 'NEW',
    'created': '2019-01-04',
  }]

function fetch(url) {
  console.log(url)
  return Promise.resolve(data)
}

global.fetch = fetch

function renderFullPage(html, css) {
  return `
    <!doctype html>
    <html>
      <head>
        <title>Material-UI</title>
      </head>
      <body>
        <script async src="build/bundle.js"></script>
        <div id="root">${html}</div>
        <style id="jss-server-side">${css}</style>
      </body>
    </html>
  `
}

function handleRender(req, res) {
  // Create a sheetsRegistry instance.
  const sheetsRegistry = new SheetsRegistry()

  // Create a sheetsManager instance.
  const sheetsManager = new Map()

  // Create a theme instance.
  const theme = createMuiTheme({
    palette: {
      primary: green,
      accent: red,
      type: 'light',
    },
    typography: {
      useNextVariants: true,
    },
  })

  // Create a new class name generator.
  const generateClassName = createGenerateClassName()

  // Render the component to a string.
  const html = ReactDOMServer.renderToString(
    <JssProvider registry={sheetsRegistry} generateClassName={generateClassName}>
      <MuiThemeProvider theme={theme} sheetsManager={sheetsManager}>
        <MemberFeature data={data}/>
      </MuiThemeProvider>
    </JssProvider>,
  )

  // Grab the CSS from our sheetsRegistry.
  const css = sheetsRegistry.toString()

  // Send the rendered page back to the client.
  res.send(renderFullPage(html, css))
}

const app = express()

app.use('/build', express.static('build'))

// This is fired every time the server-side receives a request.
app.use(handleRender)

const port = 4000
app.listen(port)