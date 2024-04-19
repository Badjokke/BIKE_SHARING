import { createConnection, SocketConnectionParams } from "../SocketClient";
import { Client } from '@stomp/stompjs';
import Stomp from 'stompjs';
const wsUrl = `${process.env.REACT_APP_RIDE_SERVICE_WS_URL}/bike`

interface bikeLocationHandler  {
   [key:string]:  NodeJS.Timeout
}


const subscriptionMap: bikeLocationHandler = {};


let socket:Client | null = null;
const DATA_FETCH_INTERVAL = 1000;



const errorCallback = (error:string|Stomp.Frame) =>{
    console.log("connection failure",error);
}
const connectCallback = (frame: Stomp.Frame | undefined) =>{

    console.log("connection established",frame?.body);

}

const disconnectCallback = (frame: Stomp.Frame) =>{

    console.log("disconnect",frame);
}

const socketParams: SocketConnectionParams = {
    connectCallback:connectCallback,
    errorCallback: errorCallback,
    disconnectCallback:disconnectCallback,
    url:wsUrl
}



export const disconnectSocket = async () =>{
    if(socket == null){
        console.log("socket not active");
        return;
    }
    await socket.deactivate()
}
export const connectBikeSocket = (connectedCallback:(frame: Stomp.Frame | undefined)=>any) =>{
    socketParams.connectCallback = connectedCallback;
    socket = createConnection(socketParams);
}
export const fetchBikeData = (bikeId:number = 0, subscribeCallback:(message:Stomp.Message)=>any): string|null => {
    if(socket == null || !socket.active){
        console.log("connection is not establied");
        return null;
    }
    const sub = socket.subscribe(`/bike_location/location/${bikeId}`,subscribeCallback,{ id: String(bikeId) })

    const handler = setInterval(()=>{
        if(socket == null || !socket.active){
            clearTimeout(handler);
            return;
        }
        socket.publish({destination:"/bike_sharing/bike",body:JSON.stringify({bikeId:bikeId})})
        },DATA_FETCH_INTERVAL);

    subscriptionMap[sub.id] = handler;
    
    return sub.id;
}
export const unsubscribeFromChannel = (channelId: number) => {
    const channel = `${channelId}`;
    if(socket == null || !socket.active || !subscriptionMap[channel]){
        console.log("no channel with id "+channelId+"found");
        return;
    }
    
    socket.unsubscribe(channel);
    clearTimeout(subscriptionMap[channel]);
    delete subscriptionMap[channel];
}








