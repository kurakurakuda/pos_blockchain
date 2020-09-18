import React from 'react';
import 'css/components/tabnav/purchase/cart.css'
import { IoIosRemoveCircle } from "react-icons/io";
import { useSelector, useDispatch } from 'react-redux'
import {purchaseProducts } from 'store/actions/actions'
import * as ACTION_TYPE from 'store/const/actionTypes'

const Cart = props => {
    const productsInCart = useSelector(state => state.productsInCart)
    const productsInShop = useSelector(state => state.productsInShop)
    const users = useSelector(state => state.userList)
    const currentUser = useSelector(state => state.currentUser)
    const dispatch = useDispatch()

    const header = () => {
        const cell = (item) => <div className="item">{item}</div>
        return (
            <div className="header">
              {cell('Product')}
              {cell('金額')}
              {cell('数量')}
              <div className='remove-btn-area' />
            </div>
        )
    }

    const productsTable = () => {
        if (Object.keys(productsInCart).length === 0) {
            return <div>商品はありません</div>
        }
        return Object.keys(productsInCart).map(i =>productRow(i))
    }
    
    const productRow = (id) => {
        const cell = (item) => <div className="item">{item}</div>
        return (
        <div key={id-1} className="items">
            {cell(productsInShop[id-1].name)}
            {cell(productsInShop[id-1].price)}
            {cell(productsInCart[id])}
            <IoIosRemoveCircle className='remove-btn' onClick={() => dispatch({ type: ACTION_TYPE.REMOVE_FROM_CART, id: id})} />            
        </div>
        )
    }

    const amountArea = () => {
        let amount = 0
        Object.keys(productsInCart).forEach(i => amount += productsInShop[i-1].price * productsInCart[i])
        const canPurchase = !(users.length === 0 || users[currentUser].possession < amount)
        return (
            <div>
                <div>
                    <div>合計</div>
                    <div className="amount"><b>{amount}</b> コイン</div>
                </div>
                <button
                    className="purchase-btn"
                    onClick={() => purchase(amount)}
                    disabled={!canPurchase}>
                        {amount === 0 ? '商品を選択してください' : canPurchase ? '購入' : '所持金が足りません'}
                </button>
            </div>
        )
    }

    const purchase = (amount) => {
        if (amount === 0) {
            return
        }
        const products = []
        productsInShop.forEach(p => {
            if (Object.keys(productsInCart).indexOf(String(p.id)) >= 0) {
                p.count = productsInCart[p.id]
                products.push(p)
            }
        })
        const purchaseDetails = {
            senderId: currentUser,
            products: products
        }
        dispatch(purchaseProducts(purchaseDetails))
    }
    
    return (
        <div className="cart">
            <div><b>カート</b></div> 
            <div>
                {header()}
                <hr />
                {productsTable()}
            </div>
            <hr />
            {amountArea()}
        </div>
    );
}

export default Cart;