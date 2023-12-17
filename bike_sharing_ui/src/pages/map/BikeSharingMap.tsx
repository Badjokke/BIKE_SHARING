import React from 'react';
import MapPage from './MapPage';
import { useEffect, useState } from 'react';
import { MapObject } from './MapPage';
import {connectBikeSocket,disconnectSocket,fetchBikeData,unsubscribeFromChannel} from "../../socket/Bike/BikeLocationSocket";
import Stomp from "stompjs";
import { ObjectClickFunctions } from './MapPage';
import { Toast,Button } from 'react-bootstrap';
import { fetchStands } from '../../api/stand_api/StandApi';
const mockbikeData = [
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

  const mockstandData = [
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

  const [subscribeMessage,setSubscribeMessage] = useState<string>("ahojky");
  const [showToast, setShowToast] = useState(false);
  const [subscribedBike,setSubscribedBike] = useState<number|null>(null);
  const [bikeData,setBikeData] = useState<MapObject[]>([]);
  const [standData,setStandData] = useState<MapObject[]>([]);

  const onBikeClickFunction = (bikeObject:MapObject) =>{
      if(subscribedBike == null){
        return;
      }
      unsubscribeFromChannel(subscribedBike);      
      setSubscribedBike(bikeObject.id);
      setShowToast(true);
  }
  
  const onObjectClick: ObjectClickFunctions = {
    onBikeClick:onBikeClickFunction
  }


const bikeLocationRecieved = (message:Stomp.Message)=>{
  const locations =  JSON.parse(message.body) as MapObject[];
  setBikeData(locations);
  console.log(locations);
  }


  const connectedCallback = (frame: Stomp.Frame | undefined) =>{
    setSubscribedBike(0);
  
  }

  useEffect(() => {
    fetchStands().then((stands)=>{
      if(stands === null){
        console.log("no stands");
        return;
      }
      setStandData(stands);    
    });

    connectBikeSocket(connectedCallback);
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      // Your cleanup logic or any actions before the user leaves
      // You might want to return a string message to display a confirmation prompt
      const confirmationMessage = 'Are you sure you want to leave?';
      event.returnValue = confirmationMessage; // Standard for most browsers
      return confirmationMessage; // For some older browsers
    };

    window.addEventListener('beforeunload', handleBeforeUnload);

    return () => {
      // Cleanup: Remove the event listener when the component is unmounted
      window.removeEventListener('beforeunload', handleBeforeUnload);
      disconnectSocket();

    };

  

  }, []); // Empty dependency array ensures the effect runs once when the component mounts


    useEffect(()=>{
      if(subscribedBike == null){
        return;
      }
      if(subscribedBike != 0){
        setSubscribeMessage(`Bike: ${subscribedBike} subscribed`);
        setShowToast(true);
      }
      fetchBikeData(subscribedBike,bikeLocationRecieved);
    },[subscribedBike,setSubscribedBike])
    
    const cancelSubscription = () => {
      if(subscribedBike == null){
        return;
      }
      setShowToast(false); // Hide the toast after canceling subscription
      unsubscribeFromChannel(subscribedBike);      
      setSubscribedBike(0);
    };

    return (
      <div> 
         <Toast
        show={showToast}
        onClose={() => setShowToast(false)}
        style={{
          position: 'absolute',
          top: 20,
          right: 20,
          zIndex: 1000, // Set the z-index to make it appear above the map
        }}
      >
        <Toast.Header>
          <strong className="mr-auto">Subscribe Message</strong>
        </Toast.Header>
        <Toast.Body>
          <p>{subscribeMessage}</p>
          <Button variant="danger" onClick={cancelSubscription}>
            Cancel Subscription
          </Button>
        </Toast.Body>
      </Toast>
    <MapPage bikeData={bikeData} standData={standData} onObjectClick={onObjectClick}/>
    </div>
     );
}

export default BikeSharingMap;