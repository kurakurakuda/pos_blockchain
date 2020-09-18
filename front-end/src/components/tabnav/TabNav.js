import React, { useState } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import 'css/components/tabnav/tabnav.css'
import History from 'components/tabnav/history/History'
import ProdcutArea from 'components/tabnav/purchase/ProductArea'
import GatyaModal from 'components/tabnav/gatya/GatyaModal'
import SendFrom from 'components/tabnav/sendmoney/SendForm'
import TxVerificationModal from 'components/tabnav/txverification/TxVerificationModal'
import { useSelector, useDispatch } from 'react-redux'
import { retrieveTxsByUser, retrieveAllTxs } from 'store/actions/actions'
import { IS_TX_HISTORY_LATEST } from 'store/const/actionTypes'

const TabNav = props => {
  const [tabIndex, changeTavToShow] = useState(0);
  const currentUser = useSelector(state => state.currentUser)
  const isTxHistoryLatest = useSelector(state => state.txHistoryLatestStatus)
  const dispatch = useDispatch()

  if (tabIndex === 0 && !isTxHistoryLatest) {
    dispatch({type: IS_TX_HISTORY_LATEST, isLatest: true})
    if (currentUser === -2) {
      dispatch(retrieveAllTxs())
    } else {
      dispatch(retrieveTxsByUser(currentUser))
    }
  } else if (tabIndex !== 0 && isTxHistoryLatest) {
    dispatch({type: IS_TX_HISTORY_LATEST, isLatest: false})
  }

  if (currentUser === -2) {
    return (
      <Tabs className="tabnav" selectedIndex={tabIndex === 0 ? 0 : 1} onSelect={tabIndex => changeTavToShow(tabIndex)}>
        <TabList>
          <Tab>取引履歴</Tab>
          <Tab>検証</Tab>
        </TabList>
        <TabPanel className={tabIndex === 0 ? 'tab-panel': null}>
          <History />
        </TabPanel>
        <TabPanel className={tabIndex !== 0 ? 'tab-panel': null}>
          <TxVerificationModal />
        </TabPanel>
      </Tabs>
    )
  }
  
  return (
    <Tabs
      className="tabnav"
      selectedIndex={tabIndex}
      onSelect={tabIndex => changeTavToShow(tabIndex)}>
      <TabList>
        <Tab>取引履歴</Tab>
        <Tab>購入</Tab>
        <Tab>送金</Tab>
        <Tab>ガチャ</Tab>
      </TabList>
      <TabPanel className={tabIndex === 0 ? 'tab-panel': null}>
        <History />
      </TabPanel>
      <TabPanel className={tabIndex === 1 ? 'tab-panel': null}>
        <ProdcutArea />
      </TabPanel>
      <TabPanel className={tabIndex === 2 ? 'tab-panel': null}>
        <SendFrom />
      </TabPanel>
      <TabPanel className={tabIndex === 3 ? 'tab-panel': null}>
        <GatyaModal />
      </TabPanel>
    </Tabs>
  );
}

export default TabNav;