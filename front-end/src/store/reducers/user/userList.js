import { 
    CREATE_USER,
    RETRIEVE_USERS,
    UPDATE_USER_POSSESSION,
    UPDATE_POSSESSION_OF_RECIPIENT_AND_SENDER,
    RETRIEVE_TXS } from 'store/const/actionTypes'

const users = []

const userList = (state = users, action) => {
    switch (action.type) {
        case CREATE_USER:
            const newUser = {
                id: action.id,
                name: action.name,
                administrator: action.administrator,
                possession: 0,
                txs: []
            }
            return Object.assign([], state, 
                [...state, newUser]
            );
        case UPDATE_USER_POSSESSION:
            const updatedWithPossession = state
            return updatedWithPossession.map(u => {
                if (u.id === action.id) {
                    u.possession = action.possession
                }
                return u
            })
        case UPDATE_POSSESSION_OF_RECIPIENT_AND_SENDER:
            const updatedWithRecipientAndSenderPossession = state
            return updatedWithRecipientAndSenderPossession.map(u => {
                if (u.id === action.senderId) {
                    u.possession = action.senderPossession
                } else if (u.id === action.recipientId) {
                    u.possession = action.recipientPossession
                }
                return u
            })
        case RETRIEVE_USERS:
            const latestUsers = state.concat(action.users)
            return latestUsers.map(u => {
                u.possession = 0
                u.txs = []
                return u
            })
        case RETRIEVE_TXS:
            const updatedByTxs = state
            return updatedByTxs.map(u => {
                if (u.id === action.id) {
                    u.possession = action.possession
                    u.txs = action.txs
                }
                return u
            })
        default:
            return state
    }
}

export default userList
  