import { ADD_TO_CART, REMOVE_FROM_CART, CLEAR_CART } from 'store/const/actionTypes'

// key: product.id
// value: product quantity in cart
let products = {}
  
const productsInCart = (state = products, action) => {
  switch (action.type) {
    case ADD_TO_CART:
      const currentCartToAdd = {}
      if (!Object.keys(products).includes(String(action.id))) {
        products[action.id] = 1
      } else {
        products[action.id] += 1
      }
      Object.keys(products).forEach(p => {
        currentCartToAdd[p] = products[p]
      })
      return currentCartToAdd
    case REMOVE_FROM_CART:
      const currentCartToRemove = {}
      Object.keys(products).forEach(p => {
        if (p === action.id) {
          products[action.id] -= 1
        }
        if (products[action.id] === 0) {
          delete products[action.id]
        } else {
          currentCartToRemove[p] = products[p]
        }
      })
      return currentCartToRemove
    case CLEAR_CART:
      products = {}
      return products
    default: 
      return state
  }
}

export default productsInCart
