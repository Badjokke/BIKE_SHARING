import React from 'react';
import { useLocation,useNavigate   } from 'react-router-dom';
import { useEffect } from 'react';
import { saveUserInfo } from '../../../token_manager/LocalStorageManager';



const OauthLogin = () => {
    const history = useNavigate();
    const location = useLocation();
    const urlParams = new URLSearchParams(location.search);
    
    const token = urlParams.get("token");
    const email = urlParams.get("email");
    const role = urlParams.get("role");

    if(email&&token&&role){
        saveUserInfo(email,token,role);
    }
    useEffect(() => {
        // Redirect to "/" when component mounts
        history("/");
      }, [history]);
    
    //store to local storage
    return null;

}

export default OauthLogin;