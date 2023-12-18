import { fireDocumentEvent } from "../events/EventManager";
const saveToLocalStorage= (key:string, value:string) => {
    localStorage.setItem(key,value)
}
const readLocalStorage = (key:string) => {
    return localStorage.getItem(key);
}
export const saveUserInfo = (email : string, token : string, role:string) => {
    saveToLocalStorage("email",email);
    saveToLocalStorage("token",token);
    saveToLocalStorage("role",role);
    fireDocumentEvent("userinfochange");
} 
export const saveChosenLanguage = (lan:string) => {
    saveToLocalStorage("language",lan);
}
export const loadChosenLanguage = ()=>{
    return readLocalStorage("language");
}
export const deleteUserInfo=()=>{
    localStorage.removeItem("email");
    localStorage.removeItem("token");
    localStorage.removeItem("role");
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
