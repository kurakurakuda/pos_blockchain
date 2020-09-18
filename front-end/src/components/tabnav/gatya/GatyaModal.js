import React, { useState } from 'react';
import Modal from 'react-modal';
import 'css/components/tabnav/gatya/gatyamodal.css'
import { postGatya } from 'store/actions/actions'
import { useSelector, useDispatch } from 'react-redux'
const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'autp',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)',
    textAlign             : 'center',
    animationType         : 'fade'
 }
};

Modal.setAppElement('#root') //任意のアプリを設定する　create-react-appなら#root

const GatyaModal = props => {
  const [isOpen, openModal] = useState(false);
  const [amount, updateAmount] = useState(0);
  const currentUser = useSelector(state => state.currentUser)
  const dispatch = useDispatch()
  
  const runGatya = () => {
    openModal(true)
    updateAmount(Math.floor(Math.random() * (200 - 1 + 1)) + 1)
  }

  const potsGatya = () => {
    const gatyaResult = {
      recipientId: currentUser,
      amount: amount
    }
    dispatch(postGatya(gatyaResult))
    openModal(false)
  }

  return <div className='gatya'>
    <button className='gatya-start-btn' onClick={() => runGatya()}>ガチャ</button>
    <Modal
      isOpen={isOpen}
      style={customStyles}
      contentLabel="Gatya Modal"
      overlayClassName='GatyaModal__Overlay'
    >
      <div className='title'>
        <div><b>ガチャ</b></div>
      </div>
      <hr />
      <div>
        <div className="gatya-details">{amount} コイン</div>
      </div>
      <button className='gatya-recieve-btn' onClick={() => potsGatya()}>受け取る</button>
    </Modal>
  </div>
}

export default GatyaModal;