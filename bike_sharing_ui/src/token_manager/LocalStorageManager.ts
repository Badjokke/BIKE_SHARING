const saveToLocalStorage= (key:string, value:string) => {
    localStorage.setItem(key,value)
}
const readLocalStorage = (key:string) => {
    return localStorage.getItem(key);
}
export const saveUserInfo = (email : string, token : string) => {
    saveToLocalStorage("email",email);
    saveToLocalStorage("token",token);
} 


export const getUserInfo = () : {email: string|null, token: string|null} => {
    const res = {
        email: readLocalStorage("email"),
        token: readLocalStorage("token")
    }
    return res;
}