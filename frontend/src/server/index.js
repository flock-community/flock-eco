const express = require('express');
const path = require('path');

const app = express();

const port = 8080;

app.use('/', express.static(path.join(__dirname, '../client')));

app.listen(port, ()=> console.log(`App is listening on port ${port}`));
