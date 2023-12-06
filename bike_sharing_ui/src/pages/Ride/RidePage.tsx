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
      id: 1,
      location: {
        longitude: 15.0,
        latitude: 10.0,
      },
    },
    // Add other data objects here
  ];
  const standData = [
    {
      id: 1,
      location: {
        longitude: 15.0,
        latitude: 10.0,
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

      <ModalRide show={showModal} handleClose={handleCloseModal} data={rideData} endStandData={standData} />
    </Container>
  );
};

export default RidePage;
