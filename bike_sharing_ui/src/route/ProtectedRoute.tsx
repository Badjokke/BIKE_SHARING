import React,{useState} from 'react';
import {Navigate } from 'react-router-dom';
import { isUserLoggedIn } from '../token_manager/LocalStorageManager';
const ProtectedRoute = ({children}:any) => {
    const [isAuth,setIsAuth] = useState<boolean>(isUserLoggedIn())
    
    if(!isAuth) {
        return <Navigate to="/login" replace />
    }
 return children

};


export default ProtectedRoute;