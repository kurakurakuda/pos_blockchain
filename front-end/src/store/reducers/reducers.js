import { combineReducers } from 'redux'
import sideNavExpandedStatus from 'store/reducers/util/sideNavExpandedStatus'
import productsInShop from 'store/reducers/products/productsInShop'
import productsInCart from 'store/reducers/products/productsInCart'
import userList from 'store/reducers/user/userList'
import currentUser from 'store/reducers/user/currentUser'
import errorInBackend from 'store/reducers/util/errorInBackend'
import txHistoryLatestStatus from 'store/reducers/util/txHistoryLatestStatus'
import verificationResult from 'store/reducers/verification/verificationResult'


const rootReducer = combineReducers({
  sideNavExpandedStatus,
  productsInShop,
  productsInCart,
  userList,
  currentUser,
  errorInBackend,
  txHistoryLatestStatus,
  verificationResult
})
  
export default rootReducer;