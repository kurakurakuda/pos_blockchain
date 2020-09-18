import React from 'react';
import 'css/components/tabnav/history/history.css'
import TxDetailsModal from 'components/tabnav/history/TxDetailsModal'
import { useSelector } from 'react-redux'

const History = props => {

 const users = useSelector(state => state.userList)
 const currentUser = useSelector(state => state.currentUser)
 const header = () => {
  const headerCell = (name) => <div className='column'><b>{name}</b></div>
    return (
      <div className='header'>
          {headerCell('日付')}
          {headerCell('種類')}
          {headerCell('金額')}
          {headerCell('詳細')}
      </div>
    )
  }
  
  const listTile = () => {
    const cell = (name) => <div className='column'>{name}</div>
    return users.length === 0 ? [] :
            users.filter(u => u.id === currentUser)[0].txs.map((tx) => 
              <div className='item' key={tx.timestamp}>
                  {cell(tx.timestamp)}
                  {cell(label(tx))}
                  {cell(tx.amount + ' コイン')}
                  {cell(detailBtn(tx))}
              </div>
    )
  }

  const label = (tx) => {
    if (tx.senderId === -1) {
        return <div className='label type-gatya'>ガチャ</div>
    } else if (tx.recipientId === -1) {
      return <div className='label type-purchase'>購入</div>
    } else if (tx.recipientId === currentUser) {
      return <div className='label type-send'>受取</div>
    }
    return <div className='label type-send'>送金</div>
  }

  const detailBtn = (tx) => {
    const userName = id => currentUser === id || id === -1 ? '' : users.filter(u => u.id === id)[0].name
    return (
      <div id={tx.timestamp+'-detailBtn'}>
        <TxDetailsModal 
        　　hash={tx.hash}
            senderId={tx.senderId}
            senderName={userName(tx.senderId)}
            recipientId={tx.recipientId}
            recipientName={userName(tx.recipientId)}
            currentUser={currentUser}
            products={tx.products}
            amount={tx.amount}
            timestamp={tx.timestamp}
        />
      </div>
    )
  }

  return (
    <div className='history'>
        {header()}
        {listTile()}
    </div>
  );
}

export default History;