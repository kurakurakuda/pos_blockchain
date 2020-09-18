import React, { PureComponent } from 'react';
import 'css/components/tabnav/purchase/productcard.css'
import AddToCartButton from 'components/tabnav/purchase/AddToCartButton'
import BagImg from 'assets/bag.png'
import CarImg from 'assets/car.png'
import ShipImg from 'assets/ship.png'
import TVImg from 'assets/tv.png'

class ProductCard extends PureComponent {

    img() {
        const imgHtml = (img, name) => <img src={img} alt={name} width="50%" height="50%" />
        switch (this.props.product.id) {
            case 1:
                return imgHtml(CarImg, 'car')
            case 2:
                return imgHtml(TVImg, 'tv')
            case 3:
                return imgHtml(BagImg, 'bag')
            case 4:
                return imgHtml(ShipImg, 'ship')
            default:
                return imgHtml(null, ' ')
        }
    }
    
    render() {
      return (
          <div className="product-card">
              <div className="title"><b>{this.props.product.name}</b></div>
              <div>{this.props.product.price} コイン</div>
              <div>{this.img()}</div>
              <AddToCartButton id={this.props.product.id}/>
          </div>
      );
    }
}

export default ProductCard;