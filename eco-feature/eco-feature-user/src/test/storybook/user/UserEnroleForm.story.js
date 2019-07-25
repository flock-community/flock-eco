import React from 'react';
import {storiesOf} from '@storybook/react';
import UserEnrolForm from "../../../main/react/user/UserEnrolForm";
import {Formik} from "formik";

const value = {
  name: "Willem Veelenturf",
  email: 'willem.veelenturf@flock.community'
}

storiesOf('Data/LayerMenuItem', module)

  // .add('default', () => {
  //   return (<UserEnrolForm/>)
  // })
  //
  // .add('value', () => {
  //   return (<UserEnrolForm
  //     value={value}/>)
  // })
  //
  // .add('password', () => {
  //   return (<UserEnrolForm
  //     password={true}
  //     validation={validation}/>)
  // })

  .add('formik', () => {
    return (<Formik
      initialValues={{
        username: '',
        email: '',
      }}
      onSubmit={values => {
        // same shape as initial values
        console.log(values);
      }}
      render={UserEnrolForm}/>)
  })

