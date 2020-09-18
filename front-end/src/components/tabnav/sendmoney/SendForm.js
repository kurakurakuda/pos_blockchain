import React, { useState } from 'react';
import 'css/components/tabnav/sendmoney/sendform.css'
import { useSelector, useDispatch } from 'react-redux'
import Select from 'react-select'
import { sendMoney } from 'store/actions/actions'

const customeStyles = {
  control: (base, state) => ({
      ...base,
      borderWidth: '2px',
  })
}

const SendForm = props => {
  const [selectedValue, setSelectedValue] = useState('');
  const [money, enterMoney] = useState(0)
  const users = useSelector(state => state.userList)
  const currentUser = useSelector(state => state.currentUser)
  const dispatch = useDispatch()
  const options = []
  users.forEach(u => {
    if (u.id !== -2 && u.id !== currentUser) {
      const op = {}
      op.value = u.id
      op.label = u.name
      options.push(op)
    }
  })

  const handleSelectOptions = e => {
    setSelectedValue(e.value);
  }

  const handleInputValue = (event) => {
    enterMoney(event.target.value)
  }

  const send = () => {
    const sending = {
      senderId: currentUser,
      recipientId: selectedValue,
      amount: money
    }
    dispatch(sendMoney(sending))
    enterMoney(0)
  }

  return (
    <div className="sendform">
        <div className='select-user'>
        <Select
          options={options}
          value={options.find(obj => obj.value === selectedValue)}
          styles={customeStyles}
          onChange={handleSelectOptions} />
        </div>
        <div className='input-coin'>
          <label>
            <input
              className={'input-coin-form'}
              type="number"
              name="sendform"
              value={money}
              onChange={handleInputValue}
              placeholder='送金額' />
          </label>
        </div>
        <button
          className='send-button'
          onClick={send}
          disabled={(money <= 0 || money > users[currentUser].possession || selectedValue === '')}>
            {money > users[currentUser].possession ? '所持金を超えています' : '送金'}
        </button>
    </div>
  );
}

export default SendForm;