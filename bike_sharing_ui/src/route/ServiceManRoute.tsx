
import React, { useState } from 'react';
import {Navigate } from 'react-router-dom';
import { isUserLoggedIn,isUserServiceman } from '../token_manager/LocalStorageManager';
const ServiceManRoute = ({children}:any) => {
    const [isAuth,setIsAuth] = useState<boolean>(isUserLoggedIn() && isUserServiceman())
    console.log(isAuth);
    if(!isAuth) {
        return <Navigate to="/login" replace/>
    }
 return children;

};


export default ServiceManRoute;