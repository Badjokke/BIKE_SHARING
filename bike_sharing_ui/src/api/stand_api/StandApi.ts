import { HTTP_METHOD, performFetch } from "../GenericApi";
import { MapObject } from "../../pages/map/MapPage";

const STAND_SERVICE_API = process.env.REACT_APP_RIDE_SERVICE_URL;

export const fetchStands = async (standId:number=0):Promise<MapObject[]|null> =>{
    const responseData = await performFetch(`${STAND_SERVICE_API}/stand/location`,{standId},HTTP_METHOD.GET);
    if(responseData == null){
        throw new Error("Stand service down");
    }
    if(responseData.ok){
        return await responseData.json() as MapObject[];
    }
    if(responseData.status == 404){
        console.log("stand with stand id: "+standId +" not found");
    }
    return null;
}