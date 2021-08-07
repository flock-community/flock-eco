import React from 'react'
import {Formik} from "formik";


function LoginForm() {
  const init = {
    username: '',
    password: '',
  }

  const handleSubmit: (values: typeof init) => void = async (values) => {
    await new Promise((r) => setTimeout(r, 500));
    alert(JSON.stringify(values, null, 2));
  }

  return (<Formik
    initialValues={init}
    onSubmit={handleSubmit}
  >

  </Formik>)
}
