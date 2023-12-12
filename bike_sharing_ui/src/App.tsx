import React from 'react';
import Navbar from "./pages/components/homepage/Navbar";
import HomePage from "./pages/home/HomePage";
import Login from "./pages/login/Login";
import Register from './pages/register/Register';
import ServicePage from './pages/service/ServicePage';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import RideListPage from './pages/Rides/RideListPage';
import MapPage from './pages/map/MapPage';
import RidePage from './pages/Ride/RidePage';
import OauthLogin from './pages/components/oauth2/OauthLogin';
import "./static/css/App.css";
import BikeSharingMap from './pages/map/BikeSharingMap';
import {Navigate} from "react-router-dom";

function App() {

  const PrivateRoute = ({ Component }:any) => {
    const isAuthenticated = false;//useIsAuthenticated();
    //const auth = isAuthenticated();
    return isAuthenticated ? <Component /> : <Navigate to="/login" />;
  };

    

  return (
    <div>
    <Router>
      <Navbar/>
    <Routes>
        <Route path='/' element={<HomePage/>}/>
        <Route path='/login' element={<Login/>}/>
        <Route path='/register' element={<Register/>}/>
        <Route path='/oauth_login' element={<OauthLogin/>}/>
        <Route path='/service' element={
          <PrivateRoute loginPath={"/login"}>
            <ServicePage/>
          </PrivateRoute>
        
        
        }/>
        <Route path='/rides' element={<RideListPage/>}/>
        <Route path='/map' element={<BikeSharingMap/>}/>
        <Route path='/ride' element={<RidePage/>}/>
      </Routes>
    </Router>
    </div>
  );
}

export default App;
