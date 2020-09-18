const products = [
    {
        id: 1,
        name: '自動車',
        price: 100
    },
    {
        id: 2,
        name: 'テレビ',
        price: 60
    },
    {
        id: 3,
        name: 'かばん',
        price: 16
    },
    {
        id: 4,
        name: '船',
        price: 135
    }
  ]
  
  const productsInShop = (state = products) => {
    return state
  }

  export default productsInShop
  