import { DISPLAY_BACKEND_ERROR } from 'store/const/actionTypes'

let defalutState = {
    isDisplay: false,
    msg: ''
}

const errorInBackend = (state = defalutState, action) => {
    switch (action.type) {
        case DISPLAY_BACKEND_ERROR:
            return {
                isDisplay: action.isDisplay,
                msg: action.msg
            };
        default:
            return state
    }
}

export default errorInBackend