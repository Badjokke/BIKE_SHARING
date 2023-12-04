// src/components/BikeListPage.tsx
import React from 'react';
import { Table, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
interface Bike {
  id: number;
  location: {
    longitude: number;
    latitude: number;
  };
}

const ServicePage: React.FC = () => {
  const bikeData: Bike[] = [
    { id: 1, location: { longitude: 15.0, latitude: 10.0 } },
    { id: 2, location: { longitude: 15.0, latitude: 10.0 } },
    { id: 3, location: { longitude: 15.0, latitude: 10.0 } },
    { id: 4, location: { longitude: 15.0, latitude: 10.0 } },
    { id: 5, location: { longitude: 15.0, latitude: 10.0 } },
  ];

  const handleMarkAsServiced = (bikeId: number) => {
    // Custom function to handle marking a bike as serviced
    console.log(`Bike ${bikeId} marked as serviced`);
    // Add your logic here
  };
  return (
    <div className="container mt-4">
      <h1>Bikes for service</h1>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Longitude</th>
            <th>Latitude</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {bikeData.map((bike) => (
            <tr key={bike.id}>
              <td>{bike.id}</td>
              <td>{bike.location.longitude}</td>
              <td>{bike.location.latitude}</td>
              <td>
              <Button
                  variant="outline-success"
                  size="sm"
                  onClick={() => handleMarkAsServiced(bike.id)}
                >
                  <FontAwesomeIcon icon={faCheckCircle} className="mr-2" />
                  Mark as Serviced
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default ServicePage;
