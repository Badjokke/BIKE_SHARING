import React from 'react';
import MapPage from './MapPage';
import { useEffect, useState } from 'react';
import { MapObject } from './MapPage';
import {connectBikeSocket,disconnectSocket,fetchBikeData,unsubscribeFromChannel} from "../../socket/Bike/BikeLocationSocket";
import Stomp from "stompjs";
import { ObjectClickFunctions } from './MapPage';
import { Toast,Button } from 'react-bootstrap';
import { fetchStands } from '../../api/stand_api/StandApi';
import { useTranslation } from 'react-i18next';


const BikeSharingMap = () =>{
  const {t} = useTranslation();
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
      const confirmationMessage = t('Are you sure you want to leave?');
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
        setSubscribeMessage(t("Bike")+` ${subscribedBike} `+ t("subscribed"));
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
          <strong className="mr-auto">{t("Subscribe Message")}</strong>
        </Toast.Header>
        <Toast.Body>
          <p>{subscribeMessage}</p>
          <Button variant="danger" onClick={cancelSubscription}>
            {t("Cancel Subscription")}
          </Button>
        </Toast.Body>
      </Toast>
    <MapPage bikeData={bikeData} standData={standData} onObjectClick={onObjectClick}/>
    </div>
     );
}

export default BikeSharingMap;