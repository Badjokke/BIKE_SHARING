import React, { useEffect, useState } from 'react';
import { Modal, Button, Form, Dropdown  } from 'react-bootstrap';
import { SelectedRide } from '../../Ride/RidePage';
import { startUserRide,BikeStartObject,RideStartResponse,RideStartFailureResponse} from '../../../api/ride_api/RideApi';
import { useTranslation } from 'react-i18next';
export interface BikeObject  {
    id: number;
    Stand: {
      id : number;
      location: {
        longitude: number;
        latitude: number;
      }
    },
    lastServiced?:string
}
export interface StandObject {
  id: number;
  location: {
    longitude: number;
    latitude: number;
  };
}

interface RideModalProps {
    show: boolean;
    handleClose: () => void;
    bikeData: BikeObject[];
    endStandData: StandObject[];
    setTokenState: (selectedRide:SelectedRide)=> void;
  }

const ModalRide: React.FC<RideModalProps> = ({ show, handleClose, bikeData, endStandData,setTokenState }) => {

  const {t} = useTranslation();
  const [selectedEndStand, setSelectedEndStand] = useState<StandObject | null>(null);
  const [selectedBike, setSelectedBike] = useState<BikeObject | null>(null);
  const [distance, setDistance] = useState<number | null>(null);


  const handleIdChange = (bike: BikeObject) => {
    setSelectedBike(bike);
  };

  useEffect(()=>{
    if(selectedEndStand == null ||  selectedBike == null)
      return;
    setDistance(calculateDistance(selectedEndStand.location, selectedBike.Stand.location));

  },[selectedEndStand,selectedBike,setSelectedBike,setSelectedEndStand])

  const handleEndStandChange = (stand: StandObject) => {
    setSelectedEndStand(stand);
  };

  const calculateDistance = (stand:{longitude:number,latitude:number},bike:{longitude:number,latitude:number}) => {
    const EARTH_RADIUS = 6371000;

    const toRadians = (degrees: number) => (degrees * Math.PI) / 180;
  
    const bikeLatitude = toRadians(bike.latitude);
    const bikeLongitude = toRadians(bike.longitude);
    const standLatitude = toRadians(stand.latitude);
    const standLongitude = toRadians(stand.longitude);
  
    const longitudeDelta = standLongitude - bikeLongitude;
    const latitudeDelta = standLatitude - bikeLatitude;
  
    let distance =
      Math.sin(latitudeDelta / 2) * Math.sin(latitudeDelta / 2) +
      Math.cos(bikeLatitude) * Math.cos(standLatitude) * Math.sin(longitudeDelta / 2) * Math.sin(longitudeDelta / 2);
  
    distance = EARTH_RADIUS * (2 * Math.atan2(Math.sqrt(distance), Math.sqrt(1 - distance)));
  
    return distance;
  }

  const startRide = async () =>{
    if(selectedBike === null || selectedEndStand === null){
      return;
    }
     // Calculate the distance between the starting stand and the selected end stand
     const startStandId = selectedBike.Stand.id;
     const endStandId = selectedEndStand.id;
 
     console.log(`starting ride with bike ${selectedBike.id} from stand ${startStandId} to stand ${endStandId}`);
     console.log(`Distance: ${distance} meters`);

      const rideObject: BikeStartObject = {
        bikeId:selectedBike.id,
        startStandId: selectedBike.Stand.id,
        endStandId: selectedEndStand.id
      }
     const response = await startUserRide(rideObject);

      if(!response){
        console.log("user information is not stored in local storage, unable to proceed");
        //redirect to login
        return;
      }
      if('token' in response){
        const res = response as RideStartResponse;

        const selectedRide:SelectedRide = {
          bike : selectedBike,
          stand: selectedEndStand,
          rideToken : res.token
       }
       setTokenState(selectedRide);
       return;
      }
      const res = response as RideStartFailureResponse;
      console.log(res.message);
      if(res.redirectTo){
        //todo redirect
      } 



  }



  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>{t("Ride Details")}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
      <Form.Group controlId="formId">
  <Form.Label>{t("Bike")}</Form.Label>
  <Dropdown>
    <Dropdown.Toggle variant="success" id="dropdown-id">
      {selectedBike ? `${t("Bike")} ${selectedBike.id}` : t('Select bike')}
    </Dropdown.Toggle>
    <Dropdown.Menu>
      {bikeData.map((bike) => (
       <Dropdown.Item key={bike.id} onClick={() => {
        handleIdChange(bike);}}>
       {t("Bike")} {bike.id}
     </Dropdown.Item>
      ))}
    </Dropdown.Menu>
  </Dropdown>
</Form.Group>
          {/* Other form fields */}
          <Form.Group controlId="formEndStand">
            <Form.Label>{t("End Stand")}</Form.Label>
            <Dropdown>
              <Dropdown.Toggle variant="success" id="dropdown-endstand">
                {selectedEndStand ? `${t("Stand")} ${selectedEndStand.id}` : t('Select End Stand')}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                {endStandData.map((stand) => (
                  <Dropdown.Item key={stand.id} onClick={() => handleEndStandChange(stand)}>
                    {t("Stand")} {stand.id}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown>
          </Form.Group>
          <Form.Group controlId="formStartStand">
            <Form.Label>{t("Starting Stand")}</Form.Label>
            <Form.Control type="text" readOnly value={selectedBike ? selectedBike.Stand.id : ''} />
          </Form.Group>
          <Form.Group controlId="formDistance">
            <Form.Label>{t("Distance")}</Form.Label>
            <Form.Control type="text" readOnly value={distance !== null ? `${distance} ${t("meters")}` : ''} />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          {t("Close")}
        </Button>
        <Button variant="primary" onClick={startRide}>
          {t("Start ride")}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalRide;