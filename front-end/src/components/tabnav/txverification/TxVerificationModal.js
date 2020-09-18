import React from 'react';
import Modal from 'react-modal';
import 'css/components/tabnav/txverification/txverificationmodal.css'
import { verifyTxs } from 'store/actions/actions'
import { UPDATE_REQUESTING_STATUS } from 'store/const/actionTypes'
import { NOT_REQUESTING, IS_REQUESTING, GOT_RESPONSE } from 'store/const/backendConst'
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

const TxVerificationModal = props => {
  const users = useSelector(state => state.userList)
  const verificationResult = useSelector(state => state.verificationResult)
  const dispatch = useDispatch()
  
  const requestVerification = () => {
    dispatch(verifyTxs())
  }

  const confirmed = () => {
    dispatch({type: UPDATE_REQUESTING_STATUS, isRequesting: NOT_REQUESTING})
  }

  const verificationResultArea = () => {
    if (verificationResult.txs.length === 0) {
      return <div>改ざんは確認されませんでした</div>
    }
    const headerCell = (name) => <div className='txverificationmomodal-details-column'><b>{name}</b></div>
    const cell = (name) => <div className='txverificationmomodal-details-column'>{name}</div>
    const tx = verificationResult.txs[verificationResult.txs.length-1]
    return (
      <div className="txverificationmomodal-details">
        この取引を含む、これ以降の取引全てに改ざんの可能性があります。
        <div>
          <div className='txverificationmomodal-details-header'>
            {headerCell('取引日時')}
            {headerCell('送金者')}
            {headerCell('受取人')}
            {headerCell('金額')}
          </div>
          <div className='txverificationmomodal-details-items' >
            {cell(tx.timestamp)}
            {cell(tx.senderId === -1 ? '-' : users.filter(u => u.id === tx.senderId)[0].name)}
            {cell(tx.recipientId === -1 ? '-' : users.filter(u => u.id === tx.recipientId)[0].name)}
            {cell(tx.amount + ' コイン')}
          </div>
        </div>
      </div>
    )
  }

  return <div className='txverificationmomodal'>
    <button
      className='txverificationmomodal-start-btn'
      onClick={() => requestVerification()}>
        {verificationResult.isRequesting === IS_REQUESTING ? '問合せ中' : '検証'}
    </button>
    <Modal
      isOpen={verificationResult.isRequesting === GOT_RESPONSE}
      style={customStyles}
      contentLabel="TxVerification Modal"
      overlayClassName='TxVerificationModal__Overlay'>
      <div className='title'>
        <div><b>検証結果</b></div>
      </div>
      <hr />
      <div>
        {verificationResultArea()}
      </div>
      <button className='txverificationmomodal-recieve-btn' onClick={() => confirmed()}>確認</button>
    </Modal>
  </div>
}

export default TxVerificationModal;