import React from 'react';
import 'css/components/userportal.css';
import Avatar from 'react-avatar';

const UserPortal = ({ user }) => {
  return (
    <div className="user-portal">
      <div className="user">
        <Avatar name={ user && user.name ? user.name : ''} className="avatar" size="36" round={true} />
        <div className="username">
        {user && user.name ? user.name : ''}
        </div>
      </div>
      <hr />
      <div className="coin">{ user && user.possession ? user.possession : '-'} コイン</div>
    </div>
  )
}

export default UserPortal;