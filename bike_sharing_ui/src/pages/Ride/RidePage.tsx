import React, { useEffect, useState } from 'react';
import { Button, Container, Row, Col } from 'react-bootstrap';
import ModalRide, { BikeObject, StandObject } from '../components/ride/ModalRide';
import MapPage, { MapObject } from '../map/MapPage';
import { LatLngTuple } from 'leaflet';
import { connectRideSocket,disconnectSocket,sendBikeLocation,subscribeToRide } from '../../socket/Ride/RideSocket';
import Stomp from "stompjs";
import { MapClickObject } from '../map/MapPage';
import { fetchStands } from '../../api/stand_api/StandApi';
import { fetchRideableBikes } from '../../api/bike_api/BikeApi';
export interface SelectedRide {
  stand : StandObject,
  bike: BikeObject,
  rideToken: string

}

const RidePage: React.FC = () => {
  const [showModal, setShowModal] = useState(false);
  const [selectedRide, setSelectedRide] = useState<SelectedRide | null>(null);
  const [selectedStand,setSelectedStand] = useState<MapObject>();
  const [selectedBike, setSelectedBike] = useState<MapObject>();
  const [standData,setStandData] = useState<MapObject[]|null>();
  const [rideData, setRideData] = useState<BikeObject[]|null>();
  const [bikeToStandPath,setBikeToStandPath] = useState<LatLngTuple[]|null>(null)
  const [socketConnected,setSocketConnected] = useState<boolean>(false);

  const handleButtonClick = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };


  const bikeLocationUpdate = (message: Stomp.Message) => {
    if(!selectedStand){
      return;
    }
    const body = JSON.parse(message.body) as MapObject;
    console.log(`new bike location: ${body}`);
    const bikeToStandPath: LatLngTuple[] = 
      [
        [body.location.latitude, body.location.longitude],
        [selectedStand.location.latitude, selectedStand.location.longitude],
      ];
      setSelectedBike(body);
      setBikeToStandPath(bikeToStandPath);
    
  }

  const sendLocation = (location:MapClickObject) => {
    if(!selectedBike){
      console.log("selected bike is undefined");
      return;
    }
      const longitude = location.location.longitude;
      const latitude = location.location.latitude;
      selectedBike.location.longitude = longitude;
      selectedBike.location.latitude = latitude;
      sendBikeLocation(selectedBike);

  }


  const socketConnectedCallback = (frame: Stomp.Frame | undefined) =>{
    console.log("bike socket connected");
    setSocketConnected(true);
    subscribeToRide(bikeLocationUpdate);

  }
  const prepareRidePage = () => {

    fetchStands().then((stands)=>{
      if(stands === null){
        console.log("no stands");
        return;
      }
      setStandData(stands);    
    });

    fetchRideableBikes().then((bikes)=>{
      if(bikes == null){
        console.log("no rideable bikes");
        return;
      }
      setRideData(bikes);
    })

    setSocketConnected(false);
    setSelectedRide(null);
    setSelectedBike(undefined);
    setSelectedStand(undefined);
    setBikeToStandPath(null);
  
  }


  const rideFinishedCallback = (message:Stomp.Message) =>{
    if(!selectedRide){
      console.log("no ride is started");
      return;
    }
    alert("Ride finished. Good job!");
    //disconnectSocket();

    prepareRidePage();
  }

  const connectToRide = (bikeToken:string) =>{
    connectRideSocket(socketConnectedCallback,rideFinishedCallback,bikeToken);
  }

  useEffect(()=>{

    prepareRidePage();


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
  },[]);



  useEffect(()=>{
    if(!selectedRide || !selectedBike || socketConnected)
      return;
    connectToRide(selectedRide.rideToken);

  },[selectedRide,setSelectedRide])


  const setUserSelectedRide = (selectedRide: SelectedRide) =>{
      setSelectedRide(selectedRide);
      setShowModal(false);
      if(!rideData){
        return null;
      }
      const standMapObject: MapObject = {
        id: selectedRide.stand.id,
        location: selectedRide.stand.location
      }
      
      const bikeData = rideData.filter((bikeObject)=>bikeObject.id === selectedRide.bike.id);

      const bikeMapObject: MapObject = {
        id: selectedRide.bike.id,
        location: bikeData[0].Stand.location
      }
      const bikeToStandPath: LatLngTuple[] = 
      [
        [bikeMapObject.location.latitude, bikeMapObject.location.longitude],
        [standMapObject.location.latitude, standMapObject.location.longitude],
      ];

      setSelectedStand(standMapObject);
      setSelectedBike(bikeMapObject);
      setBikeToStandPath(bikeToStandPath);
  }

  return (
    ( !selectedStand || !selectedBike || !bikeToStandPath )?
  (  <Container className="d-flex align-items-center justify-content-center" style={{ height: '100vh' }}>
      <Row>
        <Col>
          <div className="text-center">
            <h1>Welcome to Bike Rides!</h1>
            <Button variant="primary" size="lg" onClick={handleButtonClick}>
              Start Your Ride!
            </Button>
          </div>
        </Col>
      </Row>

      {standData&&rideData&&<ModalRide show={showModal} handleClose={handleCloseModal} bikeData={rideData} endStandData={standData} setTokenState={setUserSelectedRide}/>}
    </Container>
  )
  :
  (<MapPage bikeData={[selectedBike]} standData={[selectedStand]} paths={bikeToStandPath} onObjectClick={{onMapClick:sendLocation}} />)
  )
  
  
};

export default RidePage;
