import React from 'react';
import 'css/components/home.css';
import TabNav from './tabnav/TabNav'
import UserPortal from './UserPortal'
import ErrorModal from 'components/ErrorModal'
import { useSelector } from 'react-redux'

const Home = props => {
    const sideNavExpandedStatus = useSelector(state => state.sideNavExpandedStatus)
    const users = useSelector(state => state.userList)
    const currentUser = useSelector(state => state.currentUser)
    return (
        <div className={`home ${!sideNavExpandedStatus ? 'home-expand' : 'home-shrink'}`}>
          <UserPortal user={users.filter(u => u.id === currentUser)[0]} />
          <TabNav />
          <ErrorModal />
        </div>
    );
}

export default Home
