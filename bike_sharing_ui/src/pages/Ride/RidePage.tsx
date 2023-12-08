import React, { useState } from 'react';
import { Button, Container, Row, Col } from 'react-bootstrap';
import ModalRide from '../components/ride/ModalRide';

const RidePage: React.FC = () => {
  const [showModal, setShowModal] = useState(false);

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

  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ height: '100vh' }}>
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

      <ModalRide show={showModal} handleClose={handleCloseModal} bikeData={rideData} endStandData={standData} />
    </Container>
  );
};

export default RidePage;
