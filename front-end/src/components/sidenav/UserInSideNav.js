import React, { PureComponent } from 'react';
import 'css/components/sidenav/userinsidenav.css';
import Avatar from 'react-avatar';

class UserInSideNav extends PureComponent {
    username(isDisplay) {
        if (isDisplay) {
            return (
                <div className="username">{this.props.user.name}</div>
            )
        }
        return null;
    }
    render() {
        return (
            <div className={`user-in-sidenav ${this.props.isSelected ? 'selected-user-in-sidenav' : null}`}>
              <div className="user">
                <Avatar name={this.props.user.name} className="avatar" size="36" round={true} />
                {this.username(this.props.isExpanded)}
              </div>
            </div>
        )
    }
}

export default UserInSideNav;