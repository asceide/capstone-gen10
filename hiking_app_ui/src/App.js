import {useEffect, useState} from 'react';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import { AuthContext, UserContext } from './context';
import { refresh } from './services/authentication';
import { Home, Login, NavBar } from './components';

function App() {

  const [user, setUser] = useState();

  useEffect(() => {
    refresh().then(setUser).catch(logout);
  }, []);

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
        </Routes>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
