import React from 'react'

import Grid from '@material-ui/core/Grid'

import TextField from '@material-ui/core/TextField'
import {Field, Form} from "formik";
import * as Yup from 'yup';

function UserEnrolForm({errors, touched, isValidating}) {

  function validateEmail(value) {
    return Yup.string()
      .email('Invalid email')
      .required('Required')
      .validate(value)
      .catch(e => e.message);
  }

  function validateUsername(value) {
    let error;
    if (value === 'admin') {
      error = 'Nice try!';
    }
    return error;
  }

  return (


    <Form>
      <Field name="email" validate={validateEmail}/>
      {errors.email && touched.email && <div>{errors.email}</div>}

      <Field name="username" validate={validateUsername}/>
      {errors.username && touched.username && <div>{errors.username}</div>}

      <button type="submit">Submit</button>
    </Form>)


  function renderPassword4() {
    return (<>

        <Grid
          container
          direction="column"
          justify="space-evenly"
          alignItems="stretch"
          spacing={1}
        >
          <Grid item xs={12}>
            <TextField
              fullWidth
              label="Name"
              value={state.name}
              onChange={ev => this.handleChange('name')(ev.target.value)}
            />
          </Grid>

          <Grid item xs={12}>
            <TextField
              fullWidth
              label="Email"
              value={state.email}
              onChange={ev => this.handleChange('email')(ev.target.value)}
            />
          </Grid>

          {password && renderPassword()}

        </Grid>
      </>
    )

    function renderPassword() {
      return (<>
        <Grid item xs={12}>
          <TextField
            fullWidth
            label="Password"
            type={"password"}
            value={state.password}
            onChange={handleChangePassword(false)}
          />
        </Grid>

        <Grid item xs={12}>
          <TextField
            fullWidth
            label="Retype password"
            type={"password"}
            value={state.password}
            onChange={handleChangePassword(true)}
          />
        </Grid>
      </>)
    }
  }
}

export default UserEnrolForm
