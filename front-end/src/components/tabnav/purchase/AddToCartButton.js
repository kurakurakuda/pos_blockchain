import React from 'react'
import 'css/components/tabnav/purchase/productcard.css'
import { useDispatch } from 'react-redux'
import * as ACTION_TYPE from 'store/const/actionTypes'

const AddToCartButton = ({ id }) => {
    const dispatch = useDispatch()
    return (
        <div>
            <button
                className="add-cart-button" 
                onClick={() => dispatch({ type: ACTION_TYPE.ADD_TO_CART, id: id })}>
                カートに追加
            </button>
        </div>
    )
}

export default AddToCartButton
