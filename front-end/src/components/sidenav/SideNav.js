import React from 'react';
import 'css/components/sidenav/sidenav.css'
import UserInSideNav from 'components/sidenav/UserInSideNav'
import UserCreationModal from 'components/sidenav/UserCreationModal'
import { IoIosArrowForward, IoIosArrowBack } from "react-icons/io"
import { useSelector, useDispatch } from 'react-redux'
import {createUser, retrieveUserPossession} from 'store/actions/actions'
import * as ACTION_TYPE from 'store/const/actionTypes'

const SideNav = props => {
    const sideNavExpandedStatus = useSelector(state => state.sideNavExpandedStatus)
    const users = useSelector(state => state.userList)
    const currentUser = useSelector(state => state.currentUser)
    const dispatch = useDispatch()

    const userRows = users.map((u) => {
        return <div key={u.id} onClick={() => {
            if (!u.administrator) {
                dispatch(retrieveUserPossession(u.id))
            }
            dispatch({type: ACTION_TYPE.IS_TX_HISTORY_LATEST, isLatest: false})
            dispatch({ type: ACTION_TYPE.SELECT_USER, userId: u.id })}} >
            <UserInSideNav
                user={u}
                isExpanded={sideNavExpandedStatus}
                isSelected={u.id === currentUser}
                />
        </div>
    })
    return (
        <div className={`sidenav ${sideNavExpandedStatus ? 'sidenav-expand' : 'sidenav-shrink'}`}>
            {userRows}
            <div className={`btn add-btn ${sideNavExpandedStatus ? 'btn-expand' : 'btn-shrink'}`} >
                <UserCreationModal
                    createUser={(name) => dispatch(createUser(name))}
                />
            </div>
            <div className={`btn expand-btn ${sideNavExpandedStatus ? 'btn-expand' : 'btn-shrink'}`}
                 onClick={() => dispatch({ type: ACTION_TYPE.CHANGE_SIDE_NAV_STATUS, sideNavExpandedStatus: sideNavExpandedStatus })}>
                {sideNavExpandedStatus ? <IoIosArrowBack /> : <IoIosArrowForward />}
            </div>
        </div>
    )
}
export default SideNav
  