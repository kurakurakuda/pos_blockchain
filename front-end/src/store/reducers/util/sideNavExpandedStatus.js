import { CHANGE_SIDE_NAV_STATUS } from 'store/const/actionTypes'

const sideNavExpandedStatus = (state = false, action) => {
    switch (action.type) {
        case CHANGE_SIDE_NAV_STATUS:
            return !action.sideNavExpandedStatus
        default:
            return state
    }
  }

export default sideNavExpandedStatus