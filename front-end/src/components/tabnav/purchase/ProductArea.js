import React from 'react';
import 'css/components/tabnav/purchase/productarea.css'
import ProductCard from 'components/tabnav/purchase/ProductCard'
import Cart from 'components/tabnav/purchase/Cart'
import { useSelector } from 'react-redux'

const ProductArea = props => {
    const products = useSelector(state => state.productsInShop)
    const productCardTable = () => {
        const table = []
        for (var i = 0; i < products.length; i += 2) {
            let row = (
                <div key={`cardColumn` + i}>
                  {<ProductCard key={`card` + products[i].id} product={products[i]} />}
                  {<ProductCard key={`card` + products[i+1].id} product={products[i+1]} />}
                </div>
            )
            table.push(row)
        }
        return (
            <div className='card-table'>
                {table}
            </div>
        )
    }

    return (
        <div className="productarea">
            {productCardTable()}
            <div>
                <Cart />
            </div>
        </div>
    );
}

export default ProductArea;