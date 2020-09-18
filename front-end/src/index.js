import React from 'react';
import ReactDOM from 'react-dom';
import 'css/index.css';
import App from 'App';
import * as serviceWorker from 'serviceWorker';
import { Provider } from 'react-redux'
import rootReducer from 'store/reducers/reducers'
import thunkMiddleware from 'redux-thunk'
import { createStore, applyMiddleware } from 'redux';
import { retrieveUsers } from 'store/actions/actions'

const store = createStore(
    rootReducer,
    applyMiddleware(
        thunkMiddleware
    )
)
store.dispatch(retrieveUsers())

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>, 
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
