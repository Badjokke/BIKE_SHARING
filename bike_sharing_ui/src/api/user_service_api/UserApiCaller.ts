const USER_SERVICE_URL = `${process.env.REACT_APP_USER_SERVICE_URL}/user`;

import { HTTP_METHOD,performFetch } from "../GenericApi";

export interface USER_API_RESPONSE{
    message: string,
    data: {token:string|null},
    redirectTo?: string
}
enum USER_TYPE {
    SERVICEMAN = "SERVICEMAN", REGULAR="REGULAR"
}
interface USER_LOGGED_IN {
    token:string
}




export const registerUser = async (username:string, email: string, password: string, usertype:string): Promise<USER_API_RESPONSE> => {
    const method = HTTP_METHOD.POST;
    const type: USER_TYPE = USER_TYPE[usertype.toUpperCase() as keyof typeof USER_TYPE];
    const body = {
        username,email,password,usertype:type
    };
    const response = await performFetch(`${USER_SERVICE_URL}/register`,body,method);
    const apiResponse: USER_API_RESPONSE = {
        message: `User ${username} registered`,
        data: {token:null}
    }
    if(response=== null){
        throw new Error("register endpoint down");
    }
    if(response.status == 200){
        console.log("hello");
        const data = await response.json() as USER_LOGGED_IN;
        apiResponse.data = data;
        apiResponse.redirectTo = "/";
    }
    else if(response.status == 401){
        //redirect na login stranku - vyprsel token
    }
    else if (response.status == 409){
        apiResponse.message = `User with email: ${email} already exists`;
    }
    return apiResponse;
}



export const loginUser = async(username: string, password:string) =>{
    const method = HTTP_METHOD.POST;
    const body = {
        username,password
    }
    const apiResponse: USER_API_RESPONSE = {
        message: `User ${username} logged in`,
        data: {token:null}
    }
    const response = await performFetch(`${USER_SERVICE_URL}/login`,body,method);
    if(response == null){
        throw new Error("login endpoint down");
    }
    else if (response.ok){
        const data = await response.json() as USER_LOGGED_IN;
        apiResponse.data = data;
        apiResponse.redirectTo = "/";
    }
    else if (response.status === 404){
        apiResponse.message = `User ${username} not logged in`;
    }
    return apiResponse;

}