import {useEffect, useState} from 'react';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import { AuthContext, UserContext } from './context';
import { refresh } from './services/authentication';
import { findByEmail } from './services/users';
import { Home, Login, NavBar } from './components';
import Spot from './components/Spot';
import SpotPhotos from './components/SpotPhotos';
import Trails from "./components/Trails";
import TrailDetails from "./components/TrailDetails";



function App() {

  const [user, setUser] = useState();
  const [userInfo, setUserInfo] = useState();

  useEffect(() => {
    refresh().then(setUser).catch(logout);
  }, []);

  useEffect(() => {
    if (user) {
      findByEmail(user.sub).then(setUserInfo);
      
    }
  }, [user]);

  const logout = () => {
    setUser();
    localStorage.removeItem('jwt');
  };

  const context = {
    user,
    login: setUser,
    logout
  };

  const userContext = {
    appUserId: userInfo?.appUserId,
    firstName: userInfo?.firstName,
    lastName: userInfo?.lastName,
    city: userInfo?.city,
    state: userInfo?.state,
  };

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
          <Route path="/spot/:spotId" element={<Spot />} />
          <Route path="/spot/photos/:spotId" element={<SpotPhotos />} />
        </Routes>
      </Router>
      </UserContext.Provider>
    </AuthContext.Provider>
  );
}

export default App;
