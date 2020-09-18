import { IS_TX_HISTORY_LATEST } from 'store/const/actionTypes'

const txHistoryLatestStatus = (state = false, action) => {
    switch (action.type) {
        case IS_TX_HISTORY_LATEST:
            return action.isLatest
        default:
            return state
    }
  }

export default txHistoryLatestStatus