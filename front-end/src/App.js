import React from 'react';
import 'css/App.css';
import Home from './components/Home'
import SideNav from './components/sidenav/SideNav'

class App extends React.Component {
  render() {
    return (
      <div className="App">
        <SideNav />
        <Home />
      </div>
    );
  };
}

export default App;
