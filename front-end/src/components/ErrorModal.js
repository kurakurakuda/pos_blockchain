import React, { useEffect, useRef } from 'react';
import Modal from 'react-modal';
import 'css/components/errormodal.css'
import { useSelector, useDispatch } from 'react-redux'
import { displayBackendError } from 'store/actions/actions'
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

const ErrorModal = props => {
  const modalRef = useRef(null);
  const errorInBackend = useSelector(state => state.errorInBackend)
  const dispatch = useDispatch()
  
  useEffect(() => {
    const closeModal = () => {
      dispatch(displayBackendError(false, ''))
    }
    
    const handleClickEvent = (event) => {
    if (
      modalRef && modalRef.current
        && !modalRef.current.contains(event.target) && errorInBackend.isDisplay
    ) {
      // ref内にクリックされたeventのDOMが含まれているかを確認する
      closeModal()
    }
  }
    // subscribe event
    document.addEventListener('click', handleClickEvent)
    return () => {
      // unsubscribe event
      document.removeEventListener('click', handleClickEvent)
    };
  }, [errorInBackend, dispatch]);

  const detail = (msg) => {
    return (
      <div>
        <div className="details">{msg}</div>
      </div>
    )
  }

  return (
    <div ref={modalRef} className='errormodal'>
      <Modal
        isOpen={errorInBackend.isDisplay}
        style={customStyles}
        contentLabel="Error Modal"
        overlayClassName='ErrorModal__Overlay'
      >
        <div className='title'>
          <div><b>エラー</b></div>
        </div>
        <hr />
        {detail(errorInBackend.msg)}
      </Modal>
    </div>
  );
}

export default ErrorModal;