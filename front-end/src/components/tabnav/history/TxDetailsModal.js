import React, {useState} from 'react';
import Modal from 'react-modal';
import 'css/components/tabnav/history/txdetailsmodal.css'
import { IoIosClose } from "react-icons/io";
const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : '50%',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)'
 }
};

Modal.setAppElement('#root') //任意のアプリを設定する　create-react-appなら#root

const TxDetailsModal = props => {
  const [isOpen, openModal] = useState(false);

  const title = () => {
    const title = (name) => <div><b>{name}</b></div>
    if (props.senderId === -1) {
        return title('ガチャ')
    } else if (props.recipientId === -1) {
        return title('購入')
    }
    return title('送金')
  }

  const detail = () => {
    const isCurrentUser = id => id === props.currentUser
    if (props.senderId === -1) {
    return <div>
      {isCurrentUser(props.recipientId) ? 'あなた' : props.recipientName} は、ガチャで{props.amount} コインを獲得しました。
    </div>
    } else if (props.recipientId === -1) {
      return (
        <div>
          <div className="tx-details-modal-details">
            {isCurrentUser(props.senderId) ? 'あなた' : props.senderName} は商品を購入しました。
          </div>
          {productList()}
        </div>
      )
    } else if (props.recipientId === props.currentUser) {
      return <div>
        {isCurrentUser(props.recipientId) ? 'あなた' : props.recipientName} は {props.senderName} から {props.amount} コインを受取りしました。
      </div>
    }
    return <div>
      {isCurrentUser(props.senderId) ? 'あなた' : props.senderName} は {props.recipientName} に {props.amount} コインを送金しました。
    </div>
  }

  const productList = () => {
    const headerCell = (name) => <div className='tx-details-modal-column'><b>{name}</b></div>
    const cell = (name) => <div className='tx-details-modal-column'>{name}</div>
    const items = props.products.map((p) => 
      <div className='tx-details-modal-items' key={p}>
          {cell(p.name)}
          {cell(p.count)}
          {cell(p.count * p.price + ' コイン')}
      </div>
    )
    return (
      <div>
        <div className='tx-details-modal-header'>
          {headerCell('商品')}
          {headerCell('個数')}
          {headerCell('金額')}
        </div>
        {items}
      </div>
    )
  }

  return (
    <div>
      <button onClick={() => openModal(true)}>詳細</button>
      <Modal
        isOpen={isOpen}
        style={customStyles}
        contentLabel="Tx Details Modal"
      >
        <div className='tx-details-modal-title'>
          {title()}
          <div className='tx-details-modal-close-btn' onClick={() => openModal(false)}><IoIosClose /></div>
        </div>
        <hr />
        {detail()}          
      </Modal>
    </div>
  );
}
export default TxDetailsModal;