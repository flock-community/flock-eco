import React from 'react'

class UserAuthorityUtil extends React.Component {
  static authorities = null

  static setAuthorities(authorities) {
    UserAuthorityUtil.authorities = authorities
  }

  static hasAuthority(authority) {
    if (!UserAuthorityUtil.authorities) {
      return null
    }

    return authority
      .split(',')
      .map(it => UserAuthorityUtil.authorities.includes(it))
      .reduce((acc, cur) => (acc ? acc : cur), false)
  }

  render() {
    const hasAuthority =
      this.props.has && UserAuthorityUtil.hasAuthority(this.props.has)
    return hasAuthority ? this.props.children : null
  }
}

export default UserAuthorityUtil
