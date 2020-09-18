import React from 'react';
import Modal from 'react-modal';
import 'css/components/sidenav/usercreationmodal.css'
import { IoIosClose, IoIosAdd } from "react-icons/io";

const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'auto',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)'
 }
};

Modal.setAppElement('#root') //任意のアプリを設定する　create-react-appなら#root
class UserCreationModal extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      modalIsOpen: false,
      value: '',
      canCreate: true
    };
    this.openModal = this.openModal.bind(this);
    this.closeModal = this.closeModal.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.create = this.create.bind(this);
    
  }

  openModal() {
    this.setState({
      modalIsOpen: true,
      value: ''
    });
  }

  closeModal() {
    this.setState({
      modalIsOpen: false,
      value: '',
      canCreate: true
    });
  }

  title() {
    return <div><b>ユーザー作成</b></div>
  }

  create() {
    if (this.state.canCreate && this.state.value !== 0) {
      this.setState({
        modalIsOpen: false,
      })
      this.props.createUser(this.state.value)
    } else {
      this.setState({
        canCreate: false
      })
    }
  }

  handleChange(event) {
    this.setState({
      value: event.target.value,
      canCreate: event.target.value.length > 0 && event.target.value.length <= 20 ? true : false
    }) 
  }

  detail() {
    return <div className='user-creation-input'>
      <div className='position'>
        <label>
          <input
            className={`name-form ${this.state.canCreate ? null : 'name-invalid'}`}
            name="sendform"
            value={this.state.value}
            onChange={this.handleChange}
            placeholder='ユーザー名' />
        </label>
      </div>
      <button className='create-button' onClick={this.create}>作成</button>
    </div>
  }

  render() {
    return (
      <div>
        <IoIosAdd onClick={this.openModal}/>
        <Modal
          isOpen={this.state.modalIsOpen}
          onAfterOpen={this.afterOpenModal}
          onRequestClose={this.closeModal}
          style={customStyles}
          contentLabel="User Creation Modal"
        >
        <div className='user-creation-modal-title'>
            {this.title()}
            <div className='user-creation-modal-close-btn' onClick={this.closeModal}>
              <IoIosClose />
            </div>
        </div>
        <hr />
          {this.detail()}          
        </Modal>
      </div>
    );
  }
}

export default UserCreationModal;