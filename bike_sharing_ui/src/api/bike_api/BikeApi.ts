import { HTTP_METHOD, performFetch } from "../GenericApi";
import { getUserInfo } from "../../token_manager/LocalStorageManager";
import { BikeObject } from "../../pages/components/ride/ModalRide";
import { Location } from "../../pages/map/MapPage";
import { MapObject } from "../../pages/map/MapPage";
const BIKE_SERVICE_API = process.env.REACT_APP_RIDE_SERVICE_URL;

export interface BikeStateUpdate{
    message: string
}

interface BikeState{
    userEmail: string,
    bikeId: number
}
export interface ServiceableBikeObject{
    bikeId: number,
    standId: number|null,
    lastServiced: string
}


export const fetchRideableBikes = async (): Promise<BikeObject[] | null> =>{
    const {token} = getUserInfo();
    if(!token){
        return null;
    }

    const params = {token};
    const response = await performFetch(`${BIKE_SERVICE_API}/bike/list`,params,HTTP_METHOD.GET,token);
    if(response == null){
        throw new Error("bike service down");
    }
    if(response.ok){
        const rideableBikes = await response.json() as BikeObject[];
        return rideableBikes;
    }
    return null;
}
export const fetchBikesDueForService = async (): Promise<ServiceableBikeObject[] | null> => {
    const {token,email} = getUserInfo();
    if(!token || !email){
        return null;
    }
    const params = {token};
    const response = await performFetch(`${BIKE_SERVICE_API}/bike/service`,params,HTTP_METHOD.GET,token);
    if(response == null){
        throw new Error("bike service down");
    }
    if (response.ok){
        const bikeObjects = await response.json() as BikeObject[];
        const servicableBikes:ServiceableBikeObject[] = [];
        for(let i = 0; i < bikeObjects.length; i++){
            const bike = bikeObjects[i];
            servicableBikes.push({bikeId:bike.id,lastServiced:bike.lastServiced||"",standId:bike.Stand?.id||null});
        }
        return servicableBikes;
    }
    console.log(response);

    return null;
}


export const markBikeAsService = async (bikeId: number):Promise<String|null> =>{
    const {token,email} = getUserInfo();
    if(!token || !email){
        return null;
    }
    const body:BikeState = {bikeId:bikeId,userEmail:email};
    const response = await performFetch(`${BIKE_SERVICE_API}/bike/service`,body,HTTP_METHOD.PUT,token);
    if(response == null){
        throw new Error("bike service down");
    }
    if(response.ok){
        const responseMessage = await response.json() as BikeStateUpdate;
        return responseMessage.message;
    }


    return null;

}