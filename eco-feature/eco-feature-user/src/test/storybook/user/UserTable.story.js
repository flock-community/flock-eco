import React from 'react'
import {storiesOf} from '@storybook/react'
import {UserTable} from '../../../main/react/user/UserTable'

const handleRowClick = (row) => console.log

storiesOf('User|UserTable', module)

  .add('default', () => {
    return (<UserTable/>)
  })

