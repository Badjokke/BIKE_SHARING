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
import "./static/css/App.css";
function App() {
  const data = [
    {
      id: 1,
      location: { longitude: 420.0, latitude: 420.0 },
    },
    {
      id: 2,
      location: { longitude: -74.006, latitude: 40.7128 },
    },
    {
      id: 3,
      location: { longitude: -118.2437, latitude: 34.0522 },
    },
    {
      id: 4,
      location: { longitude: -87.6298, latitude: 41.8781 },
    },
    {
      id: 5,
      location: { longitude: -0.1278, latitude: 51.5074 },
    },
  ];
  return (
    <Router>
      <Navbar/>
    <Routes>
        <Route path='/' element={<HomePage/>}/>
        <Route path='/login' element={<Login/>}/>
        <Route path='/register' element={<Register/>}/>
        <Route path='/service' element={<ServicePage/>}/>
        <Route path='/rides' element={<RideListPage/>}/>
        <Route path='/map' element={<MapPage data={data}/>}/>
        <Route path='/ride' element={<RidePage/>}/>
      </Routes>
    </Router>     
  );
}

export default App;
