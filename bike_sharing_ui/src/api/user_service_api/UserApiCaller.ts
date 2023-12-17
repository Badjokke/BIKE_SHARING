import { HTTP_METHOD,performFetch } from "../GenericApi";
import { getUserInfo } from "../../token_manager/LocalStorageManager";
const USER_SERVICE_URL = `${process.env.REACT_APP_USER_SERVICE_URL}/user`;

export interface USER_API_RESPONSE{
    message: string,
    data: {token:string|null,role:string|null},
    redirectTo?: string
}

export interface USER_RIDES_RESPONSE{
    rides : Ride[],
    message?: string,
    redirectTo?: string
}
export interface Ride {
    rideId: number;
    userId: number;
    bikeId: number;
    startStandId: number;
    endStandId: number;
    rideStart: string;
    rideEnd: string;
  }

enum USER_TYPE {
    SERVICEMAN = "SERVICEMAN", REGULAR="REGULAR"
}
interface USER_LOGGED_IN {
    token:string,
    role: string
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
        data: {token:null,role:null}
    }
    if(response=== null){
        throw new Error("register endpoint down");
    }
    if(response.status == 200){
        const data = await response.json() as USER_LOGGED_IN;
        apiResponse.data = data;
        apiResponse.redirectTo = "/";
    }
    else if(response.status == 401){
        apiResponse.redirectTo = "/login";
    }
    else if (response.status == 409){
        apiResponse.message = `User with email: ${email} already exists`;
    }
    return apiResponse;
}



export const loginUser = async(email: string, password:string) =>{
    const method = HTTP_METHOD.POST;
    const body = {
        email,password
    }
    const apiResponse: USER_API_RESPONSE = {
        message: `User ${email} logged in`,
        data: {token:null, role:null}
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
        apiResponse.message = `User ${email} not logged in`;
    }
    return apiResponse;

}


export const fetchUserRides = async():Promise<USER_RIDES_RESPONSE | null>=>{
    const {email,token} = getUserInfo();
    if(!email || !token){
        console.error(`user credentials not stored`);
        return null;
    }
    const params = {userEmail: email};

    const response = await performFetch(`${USER_SERVICE_URL}/rides`,params,HTTP_METHOD.GET,token);
    if (response == null){
        throw new Error("user rides endpoint down");
    }
    if(response.ok){
        const userRides = await response.json() as Ride[];

        return {rides:userRides};
    }
    return {
        rides:[],
        message:`user with email ${email} doesnt exist.`,
        redirectTo:"/login"
    }
    
}