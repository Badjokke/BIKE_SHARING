import { createConnection, SocketConnectionParams } from "../SocketClient";
import { Client } from '@stomp/stompjs';
import Stomp from 'stompjs';
import { MapObject } from "../../pages/map/MapPage";

const wsUrl = `${process.env.REACT_APP_RIDE_SERVICE_WS_URL}/ride`

let socket:Client | null = null;
let token: string;
let rideFinished:(message:Stomp.Message)=>any;



const errorCallback = (error:string|Stomp.Frame) =>{
    console.log("ride socket connection failure",error);
}
const connectCallback = (frame: Stomp.Frame | undefined) =>{

    console.log("connection ride socket established",frame?.body);

}
const disconnectCallback = (frame: Stomp.Frame) =>{

    console.log("disconnect",frame);
}

const socketParams: SocketConnectionParams = {
    connectCallback:connectCallback,
    errorCallback: errorCallback,
    disconnectCallback:disconnectCallback,
    url:""
}


export const disconnectSocket = async () =>{
    if(socket == null){
        console.log("socket not active");
        return;
    }
    await socket.deactivate()
}


export const connectRideSocket = (connectedCallback:(frame: Stomp.Frame | undefined)=>any, rideFinishedCallback:(message:Stomp.Message)=>any,rideToken:string) =>{
    socketParams.connectCallback = connectedCallback;
    socketParams.url = `${wsUrl}?token=${rideToken}`;
    token = rideToken
    socket = createConnection(socketParams);
    rideFinished = rideFinishedCallback;
}

export const subscribeToRide = (subscribeCallback:(message:Stomp.Message)=>any) => {
    if( socket == null || !socket.active || !token){
        console.log("Ride connection is not established");
        return;
    }

    socket.subscribe(`/bike_ride/${token}`,subscribeCallback,{ id: (token)});

    const channel = socket.subscribe(`/bike_ride/close/${token}`,(message:Stomp.Message)=>{
        rideFinished(message);
        socket?.unsubscribe(channel.id);
    })
}



export const sendBikeLocation = (bikeLocation:MapObject) => {
    if(socket == null || !socket.active||!token){
        console.log("connection is not establied");
        return null;
    }
    console.log("sending bike location",bikeLocation);
    socket.publish({destination:"/bike_sharing/ride",body:JSON.stringify({location:bikeLocation.location,rideToken:token})})

}









