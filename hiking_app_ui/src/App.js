import {useEffect, useState} from 'react';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import { AuthContext, UserContext } from './context';
import { refresh } from './services/authentication';
import { findByEmail } from './services/users';
import { Home, Login, NavBar, CreateAccount } from './components';
import { encrypt as encryption } from './helpers/encryption';
import Spot from './components/Spot';
import SpotPhotos from './components/SpotPhotos';
import SpotForm from './components/SpotForm';
import Trails from "./components/Trails";
import TrailDetails from "./components/TrailDetails";



function App() {

;

  const [user, setUser] = useState();
  const [userInfo, setUserInfo] = useState();
  // Holds the public key for encryption
  const [pkey, setPkey] = useState();

  useEffect(() => {
    // Try to refresh the token. If it fails, then clear out the user by logging out.
    refresh().then(setUser).catch(logout);
    // Get the public key for encryption
    fetch('/public_key/publicKey').then(res => res.text()).then(res => setPkey(res.replace("-----BEGIN PUBLIC KEY-----","").replace("-----END PUBLIC KEY-----","").replace("\n", ""))).catch("Was unable to load public key");
  }, []);

  useEffect(() => {
    // When a user is logged in, get their information.
    if (user) {
      findByEmail(user.sub).then(setUserInfo).catch("no info");
    }
  }, [user]);

  useEffect(() => {
    
  }, []);

  const logout = () => {
    // On log out, clear the user and remove the jwt token.
    setUser();
    localStorage.removeItem('jwt');
  };

  const context = {
    user,
    login: setUser,
    logout,
    pkey,
    encryption
  };

  const userContext = {
    userInfo
  }

  return (
    <AuthContext.Provider value={context}>
      <UserContext.Provider value={userContext}>
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/trails" element={<Trails/>}/>
          <Route path="/trails/:trailId" element={<TrailDetails/>}/>
          <Route path="/login" element={<Login />} />
          <Route path="/create-account" element={<CreateAccount />} />
          <Route path="/spot/:spotId" element={<Spot />} />
          <Route path="/spot/photo/:spotId" element={<SpotPhotos />} />
          <Route path="/spot/add/:trailId" element = {user ? <SpotForm /> : <Login />} />
          </Routes>
      </Router>
      </UserContext.Provider>
    </AuthContext.Provider>
  );
}

export default App;
