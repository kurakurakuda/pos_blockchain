import { SELECT_USER } from 'store/const/actionTypes'

const current = -2

const currentUser = (state = current, action) => {
    switch (action.type) {
        case SELECT_USER:
            return action.userId
        default:
            return state
    }
}

export default currentUser