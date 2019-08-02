import React, {useState} from 'react'
import {storiesOf} from '@storybook/react'
import {UserTable} from '../../../main/react/user/UserTable'
import {UserForm} from '../../../main/react/user/UserForm'
import {findAllAuthorities} from '../../../main/react/user/UserClient'
import {Button} from '@material-ui/core'

const handleRowClick = row => console.log

const authorities = [
  'Auth.A',
  'Auth.B'
]
const value_a = {
  name: 'a',
  email: 'a@a.aa',
  reference: 'a',
  authorities: [false, true],
}

const value_b = {
  name: 'b',
  email: 'b@b.bb',
  reference: 'b',
  authorities: [true, false],
}

storiesOf('User/UserForm', module)
  .add('default', () => {
    return <UserForm />
  })
  .add('value', () => {
    return <UserForm authorities={authorities} value={value_a} />
  })
  .add('value switch', () => React.createElement(() => {

    const [state, setState] = useState(value_b)

    const handleClick = () => {
      if(state === value_b){
        setState(value_a)
      }else{
        setState(value_b)
      }
    }
    return <>
      <UserForm authorities={authorities} value={state} />
      <Button onClick={handleClick}>Switch</Button>
      </>
  }))
