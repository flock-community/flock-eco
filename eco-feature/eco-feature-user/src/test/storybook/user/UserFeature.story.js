import React from 'react'
import {storiesOf} from '@storybook/react'
import UserRegisterForm from '../../../main/react/user/UserRegisterForm'
import {UserFeature} from '../../../main/react/user/UserFeature'

storiesOf('User/UserFeature', module)

  .add('default', () => (<UserFeature style={{height:300}}/>))
