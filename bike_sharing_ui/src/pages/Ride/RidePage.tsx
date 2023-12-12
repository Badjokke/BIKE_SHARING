import React, { useState } from 'react';
import { Button, Container, Row, Col } from 'react-bootstrap';
import ModalRide, { BikeObject, StandObject } from '../components/ride/ModalRide';
import MapPage, { MapObject } from '../map/MapPage';
import { LatLngTuple } from 'leaflet';

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
  const [bikeToStandPath,setBikeToStandPath] = useState<LatLngTuple[]|null>(null)
  const handleButtonClick = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const rideData = [
    {
        "id": 2,
        "Stand": {
            "id": 1,
            "location": {
                "longitude": 16.0,
                "latitude": 11.0
            }
        }
    },
    {
        "id": 4,
        "Stand": {
            "id": 2,
            "location": {
                "longitude": 15.0,
                "latitude": 10.0
            }
        }
    },
    {
        "id": 1,
        "Stand": {
            "id": 3,
            "location": {
                "longitude": 15.0,
                "latitude": 10.0
            }
        }
    },
    {
        "id": 5,
        "Stand": {
            "id": 4,
            "location": {
                "longitude": 15.0,
                "latitude": 10.0
            }
        }
    },
    {
        "id": 3,
        "Stand": {
            "id": 5,
            "location": {
                "longitude": 15.0,
                "latitude": 10.0
            }
        }
    }
];
  const standData = [
    {
      id: 1,
      location: {
        longitude: 200.0,
        latitude: 500.0,
      },
    },
  ];

  const setUserSelectedRide = (selectedRide: SelectedRide) =>{
      setSelectedRide(selectedRide);
      setShowModal(false);

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
    (!selectedStand || !selectedBike || !bikeToStandPath)?
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

      <ModalRide show={showModal} handleClose={handleCloseModal} bikeData={rideData} endStandData={standData} setTokenState={setUserSelectedRide} />
    </Container>
  )
  :
  (<MapPage bikeData={[selectedBike]} standData={[selectedStand]} paths={bikeToStandPath}/>)
  )
  
  
};

export default RidePage;
