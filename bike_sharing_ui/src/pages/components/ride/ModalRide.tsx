import React, { useState } from 'react';
import { Modal, Button, Form, Dropdown  } from 'react-bootstrap';

interface RideModalProps {
    show: boolean;
    handleClose: () => void;
    data: {
      id: number;
      location: {
        longitude: number;
        latitude: number;
      };
    }[];
    endStandData: {
      id: number;
      location: {
        longitude: number;
        latitude: number;
      };
    }[];
  }

const ModalRide: React.FC<RideModalProps> = ({ show, handleClose, data, endStandData }) => {
  // Add your modal form logic here

  const [selectedEndStand, setSelectedEndStand] = useState<number | null>(null);
  const [selectedBikeId, setSelectedBikeId] = useState<number | null>(null);

const handleIdChange = (id: number) => {
    setSelectedBikeId(id);
};

  const handleEndStandChange = (id: number) => {
    setSelectedEndStand(id);
  };

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
      {selectedBikeId ? `Bike ${selectedBikeId}` : 'Select bike'}
    </Dropdown.Toggle>
    <Dropdown.Menu>
      {data.map((bike) => (
       <Dropdown.Item key={bike.id} onClick={() => {
        console.log("hello");
        handleIdChange(bike.id);}}>
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
                {selectedEndStand ? `Stand ${selectedEndStand}` : 'Select End Stand'}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                {endStandData.map((stand) => (
                  <Dropdown.Item key={stand.id} onClick={() => handleEndStandChange(stand.id)}>
                    Stand {stand.id}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={handleClose}>
          Start ride
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalRide;