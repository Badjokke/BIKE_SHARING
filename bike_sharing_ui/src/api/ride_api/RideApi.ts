import { HTTP_METHOD, performFetch } from "../GenericApi";
import { getUserInfo } from "../../token_manager/LocalStorageManager";



const RIDE_SERVICE_API = process.env.REACT_APP_RIDE_SERVICE_URL;
interface RideStartRequestData {
    userEmail : string,
    startStandId : number,
    endStandId : number,
    bikeId : number
}

export interface BikeStartObject {
    bikeId: number,
    endStandId: number,
    startStandId: number
}

export interface RideStartResponse {
    rideId : number,
    token : string,
    message : string
}
export interface RideStartFailureResponse {
    message?:string
    redirectTo?:string
}

export const startUserRide = async (rideObject: BikeStartObject): Promise<RideStartResponse|undefined|RideStartFailureResponse> =>{
    const {email,token} = getUserInfo();
    if(!email || !token){
        return;
    }
    const requestData: RideStartRequestData = {
        userEmail:email, startStandId: rideObject.startStandId, endStandId: rideObject.endStandId, bikeId:rideObject.bikeId
    };


    const response = await performFetch(`${RIDE_SERVICE_API}/ride/start`,requestData,HTTP_METHOD.POST,token);
    if (response === null){
        throw new Error("Ride service down");
    }

    switch(response.status){
        case 201:
            return await response.json() as RideStartResponse;
        case 400:
        case 404:
            return await response.json() as RideStartFailureResponse;
        case 401:
            const data = await response.json() as RideStartFailureResponse;
            data.redirectTo = "/login";
            return data;
    }
}