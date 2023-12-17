import React, { useEffect, useState } from 'react';
import { Modal, Button, Form, Dropdown  } from 'react-bootstrap';
import { SelectedRide } from '../../Ride/RidePage';
import { startUserRide,BikeStartObject,RideStartResponse,RideStartFailureResponse} from '../../../api/ride_api/RideApi';
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

  const calculateDistance = (location1:{longitude:number,latitude:number},location2:{longitude:number,latitude:number}) => {
    const deltaLongitude = location1.longitude - location2.longitude;
    const deltaLatitude = location1.latitude - location2.latitude; 

    return Math.sqrt((deltaLongitude * deltaLongitude) + (deltaLatitude * deltaLatitude));
  }

  const startRide = async () =>{
    if(selectedBike === null || selectedEndStand === null){
      return;
    }
     // Calculate the distance between the starting stand and the selected end stand
     const startStandId = selectedBike.Stand.id;
     const endStandId = selectedEndStand.id;
 
     console.log(`starting ride with bike ${selectedBike.id} from stand ${startStandId} to stand ${endStandId}`);
     console.log(`Distance: ${distance} units`);

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
        <Modal.Title>Ride Details</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
      <Form.Group controlId="formId">
  <Form.Label>Bike</Form.Label>
  <Dropdown>
    <Dropdown.Toggle variant="success" id="dropdown-id">
      {selectedBike ? `Bike ${selectedBike.id}` : 'Select bike'}
    </Dropdown.Toggle>
    <Dropdown.Menu>
      {bikeData.map((bike) => (
       <Dropdown.Item key={bike.id} onClick={() => {
        handleIdChange(bike);}}>
       Bike {bike.id}
     </Dropdown.Item>
      ))}
    </Dropdown.Menu>
  </Dropdown>
</Form.Group>
          {/* Other form fields */}
          <Form.Group controlId="formEndStand">
            <Form.Label>End Stand</Form.Label>
            <Dropdown>
              <Dropdown.Toggle variant="success" id="dropdown-endstand">
                {selectedEndStand ? `Stand ${selectedEndStand.id}` : 'Select End Stand'}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                {endStandData.map((stand) => (
                  <Dropdown.Item key={stand.id} onClick={() => handleEndStandChange(stand)}>
                    Stand {stand.id}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown>
          </Form.Group>
          <Form.Group controlId="formStartStand">
            <Form.Label>Starting Stand</Form.Label>
            <Form.Control type="text" readOnly value={selectedBike ? selectedBike.Stand.id : ''} />
          </Form.Group>
          <Form.Group controlId="formDistance">
            <Form.Label>Distance</Form.Label>
            <Form.Control type="text" readOnly value={distance !== null ? `${distance} units` : ''} />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={startRide}>
          Start ride
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalRide;