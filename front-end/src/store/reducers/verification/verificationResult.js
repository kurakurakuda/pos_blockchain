import { UPDATE_VERIFICATION_RESULT, UPDATE_REQUESTING_STATUS } from 'store/const/actionTypes'

// isRequesting
// 0: not requested
// 1: requesting
// 2: got response
const defaultState = {
    isRequesting: 0,
    txs: []
}

const verificationResult = (state = defaultState, action) => {
    switch (action.type) {
        case UPDATE_REQUESTING_STATUS:
            return {
                isRequesting: action.isRequesting,
                txs: []
            }
        case UPDATE_VERIFICATION_RESULT:
            return {
                isRequesting: 2,
                txs: action.txs
            }
        default:
            return state
    }
  }

export default verificationResult