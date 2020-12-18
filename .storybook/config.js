import {configure} from '@storybook/react'

function loadStories() {
  require('../eco-feature/eco-feature-user/src/test/storybook/index.js')
}

configure(loadStories, module)
