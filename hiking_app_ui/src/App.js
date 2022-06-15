import {useEffect, useState} from 'react';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import NavBar from './components/NavBar';
import AuthContext from './context/AuthContext';
import refresh from './services/authentication';
import Spot from './components/Spot';

function App() {

  const [user, setUser] = useState();

  const logout = () => {
    setUser();
    localStorage.removeItem('jwt');
  }

  const context = {
    user,
    login: setUser,
    logout
  };

  return (
    <AuthContext.Provider value={context}>
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/spot/:spotId" element={<Spot />} />
        </Routes>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
