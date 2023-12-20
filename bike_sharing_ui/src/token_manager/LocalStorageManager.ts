import { fireDocumentEvent } from "../events/EventManager";
import { decodeJwt } from "./JwtParser";

const saveToLocalStorage= (key:string, value:string) => {
    localStorage.setItem(key,value)
}
const readLocalStorage = (key:string) => {
    return localStorage.getItem(key);
}
const saveTokenExpDate = async (token: string) =>{
    let expires_in = 0;
    const decoded = await decodeJwt(token);

    if(!decoded)
        return;
    
    "expires_in" in decoded? expires_in = Number(decoded.expires_in):expires_in = (decoded.exp - decoded.iat);
    //expiration of token in milliseconds
    const expTimestamp = new Date().getTime() + expires_in * 1000;
    saveToLocalStorage("exp",String(expTimestamp));
}

export const saveUserInfo = async (email : string, token : string, role:string) => {
    saveToLocalStorage("email",email);
    saveToLocalStorage("token",token);
    saveToLocalStorage("role",role);
    fireDocumentEvent("userinfochange");
    await saveTokenExpDate(token);
} 
export const saveChosenLanguage = (lan:string) => {
    saveToLocalStorage("language",lan);
}
export const loadChosenLanguage = ()=>{
    return readLocalStorage("language");
}
export const deleteUserInfo= () => {

    localStorage.removeItem("email");
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("exp");
    
    fireDocumentEvent("userinfochange");
}

export const getUserInfo = () : {email: string|null, token: string|null, role: string|null} => {
    const res = {
        email: readLocalStorage("email"),
        token: readLocalStorage("token"),
        role: readLocalStorage("role")
    }
    return res;
}


export const isUserLoggedIn = (): boolean =>{

    let expStamp: string|number | null = readLocalStorage("exp");

    if(!expStamp){
        return false;
    }

    expStamp = Number(expStamp);
    const tokenValid = new Date().getTime() < expStamp; 
    if(!tokenValid)
        deleteUserInfo();
    return tokenValid;
}

export const isUserServiceman = (): boolean =>{
    let role = readLocalStorage("role");
    return role === "SERVICEMAN";
}