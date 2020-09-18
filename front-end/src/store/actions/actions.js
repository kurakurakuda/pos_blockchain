import { CREATE_USER,
         DISPLAY_BACKEND_ERROR,
         RETRIEVE_USERS,
         UPDATE_USER_POSSESSION,
         CLEAR_CART,
         UPDATE_POSSESSION_OF_RECIPIENT_AND_SENDER,
         RETRIEVE_TXS,
         UPDATE_VERIFICATION_RESULT,
         UPDATE_REQUESTING_STATUS } from 'store/const/actionTypes'
import * as errorMsg from 'store/const/errorMsg'
import { HTTP_METHOD_GET, 
        HTTP_METHOD_POST, 
        IS_REQUESTING, 
        GOT_RESPONSE } from 'store/const/backendConst'

const endpoint = process.env.REACT_APP_ENDPOINT_BACKEND

export const displayBackendError = (status, msg) => {
    return {
        type: DISPLAY_BACKEND_ERROR,
        isDisplay: status,
        msg: msg
    }
}

export const createUser = name => {
    return async dispatch => {
        try {
            const options = createRequestOptions(HTTP_METHOD_POST)
            const result = await requestBackend(`${endpoint}/user?name=${name}`, options)
            dispatch({
                type: CREATE_USER,
                id: result.id,
                name: result.name,
                administrator: result.administrator,
            })
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_CREATE_USER))
        }
    }
}

export const retrieveUsers = () => {
    return async dispatch => {
        try {
            const options = createRequestOptions(HTTP_METHOD_GET)
            const result = await requestBackend(`${endpoint}/user`, options)
             dispatch({ type: RETRIEVE_USERS, users: result.users })
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_RETRIEVE_USERS))
        }
    }
}

export const retrieveUserPossession = (id) => {
    return async (dispatch) => {
        try {
            const options = createRequestOptions(HTTP_METHOD_GET)
            const result = await requestBackend(`${endpoint}/user/balance?id=${id}`, options)
            dispatch({
                type: UPDATE_USER_POSSESSION,
                id: result.userId,
                possession: result.possession
            })
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_RETRIEVE_USERS_POSSESSION))
        }
    }
}

export const postGatya = (data) => {
    return async dispatch => {
        try {
            const options = createRequestOptionsWithBody(HTTP_METHOD_POST, data)
            const result = await requestBackend(`${endpoint}/tx/gatya`, options)
            dispatch({
                type: UPDATE_USER_POSSESSION,
                id: result.recipientId,
                possession: result.possession
            })
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_PROCESS_BACKEND))
        }
    }
}

export const purchaseProducts = (data) => {
    return async dispatch => {
        try {
            const options = createRequestOptionsWithBody(HTTP_METHOD_POST, data)
            const result = await requestBackend(`${endpoint}/tx/purchase`, options)
            dispatch({
                type: UPDATE_USER_POSSESSION,
                id: result.senderId,
                possession: result.possession
            })
            dispatch({type: CLEAR_CART})
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_PROCESS_BACKEND))
        }
    }
}

export const sendMoney = (data) => {
    return async dispatch => {
        try {
            const options = createRequestOptionsWithBody(HTTP_METHOD_POST, data)
            const result = await requestBackend(`${endpoint}/tx/sendmoney`, options)
                dispatch({
                type: UPDATE_POSSESSION_OF_RECIPIENT_AND_SENDER,
                senderId: result.senderId,
                senderPossession: result.senderPossession,
                recipientId: result.recipientId,
                recipientPossession: result.recipientPossession
            })
            dispatch({type: CLEAR_CART})
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_PROCESS_BACKEND))
        }
    }
}

export const retrieveTxsByUser = (id) => {
    return async dispatch => {
        try {
            const options = createRequestOptions(HTTP_METHOD_GET)
            const result = await requestBackend(`${endpoint}/user/txs?id=${id}`, options)
            dispatch({
                type: RETRIEVE_TXS,
                id: result.userId,
                possession: result.possession,
                txs: result.txs
            })
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_PROCESS_BACKEND))
        }
    }
}

export const retrieveAllTxs = () => {
    return async dispatch => {
        try {
            const options = createRequestOptions(HTTP_METHOD_GET)
            const result = await requestBackend(`${endpoint}/tx/all`, options)
            dispatch({
                type: RETRIEVE_TXS,
                id: -2,
                txs: result.txs
            })
        } catch (e) {
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_PROCESS_BACKEND))
        }
    }
}

export const verifyTxs = () => {
    return async dispatch => {
        dispatch({type: UPDATE_REQUESTING_STATUS, isRequesting: IS_REQUESTING})
        try {
            const options = createRequestOptions(HTTP_METHOD_GET)
            const result = await requestBackend(`${endpoint}/tx/confirmation`, options)
            dispatch({
                type: UPDATE_VERIFICATION_RESULT,
                txs: result.txs
            })
        } catch (e) {
            dispatch({type: UPDATE_REQUESTING_STATUS, isRequesting: GOT_RESPONSE})
            //TODO errorCodeで場合分け
            dispatch(displayBackendError(true, errorMsg.FAILED_TO_PROCESS_BACKEND))
        }
    }
}   

// --- private methods ---
const createRequestOptions = method => {
    return {
        method: method,
        mode: "cors"
    }
}

const createRequestOptionsWithBody = (method, body) => {
    return {
        method: method,
        mode: "cors",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    }
}

const requestBackend = async (url, options) => {
    const response = await fetch(url, options)
    if (!response.ok) {throw Error}
    return await response.json()
}