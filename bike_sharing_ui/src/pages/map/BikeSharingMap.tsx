import React from 'react';
import MapPage from './MapPage';
import { useEffect, useState } from 'react';
import { MapObject } from './MapPage';

import { SocketConnectionParams } from '../../socket/SocketClient';
import { createConnection } from '../../socket/SocketClient';
import Stomp from "stompjs";
const bikeData = [
    {
      id: 1,
      location: { longitude: 420.0, latitude: 420.0 },
    },
    {
      id: 2,
      location: { longitude: -74.006, latitude: 40.7128 },
    },
    {
      id: 3,
      location: { longitude: -118.2437, latitude: 34.0522 },
    },
    {
      id: 4,
      location: { longitude: -87.6298, latitude: 41.8781 },
    },
    {
      id: 5,
      location: { longitude: -0.1278, latitude: 51.5074 },
    },
  ];

  const standData = [
    {
      id: 1,
      location: { longitude: 15, latitude: 10 },
    },
    {
      id: 2,
      location: { longitude: 34, latitude: 50 },
    },
    {
      id: 3,
      location: { longitude: 60, latitude: 80 },
    }];




const BikeSharingMap = () =>{
    const [bikeData,setBikeData] = useState<MapObject[]>([]);
    const [standData,setStandData] = useState<MapObject[]>([]);

    const errorCallback = (error:string|Stomp.Frame) =>{

    }
    const connectCallback = (frame: Stomp.Frame | undefined) =>{

    }
    
    const messageRecieved = (message:Stomp.Message)=>{

    }

    const socketParams: SocketConnectionParams = {
        connectCallback:connectCallback,
        errorCallback: errorCallback,
        url:"ws://localhost:8082/bike"
    }




    return ( <MapPage bikeData={bikeData} standData={standData}/>);
}

export default BikeSharingMap;