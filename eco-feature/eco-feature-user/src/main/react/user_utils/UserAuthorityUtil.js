import React from 'react';

import {withStyles} from '@material-ui/core/styles';

const styles = theme => ({});

class UserAuthorityUtil extends React.Component {

  static authorities = null;

  static setAuthorities(authorities){
    UserAuthorityUtil.authorities = authorities
  }

  render() {

    if(!UserAuthorityUtil.authorities){
      return null
    }

    const hasAuthority = this.props.has && this.props.has.split(',')
      .map(it => UserAuthorityUtil.authorities.includes(it))
      .reduce((acc, cur) => acc ? acc : cur, false)
    return hasAuthority ? this.props.children: null
  }

}

export default withStyles(styles)(UserAuthorityUtil);