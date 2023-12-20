import React from 'react';
import Navbar from "./pages/components/homepage/Navbar";
import HomePage from "./pages/home/HomePage";
import Login from "./pages/login/Login";
import Register from './pages/register/Register';
import ServicePage from './pages/service/ServicePage';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import RideListPage from './pages/Rides/RideListPage';
import RidePage from './pages/Ride/RidePage';
import OauthLogin from './pages/components/oauth2/OauthLogin';
import "./static/css/App.css";
import BikeSharingMap from './pages/map/BikeSharingMap';
import ProtectedRoute from './route/ProtectedRoute';
import ServiceManRoute from './route/ServiceManRoute';
function App() {
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
          <ServiceManRoute>
            <ServicePage/>
          </ServiceManRoute>
        }/>
        
        <Route path='/rides' element={
          <ProtectedRoute>
            <RideListPage/>
          </ProtectedRoute>}
        />
        <Route path='/map' element={
          <ProtectedRoute>
            <BikeSharingMap/>
          </ProtectedRoute>
        }
        />
        <Route path='/ride' element={
          <ProtectedRoute>
            <RidePage/>
          </ProtectedRoute>
        }
        />
      </Routes>
    </Router>
    </div>
  );
}

export default App;
