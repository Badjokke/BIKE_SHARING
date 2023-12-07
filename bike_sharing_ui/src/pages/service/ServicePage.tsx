// src/components/BikeListPage.tsx
import React from 'react';
import { Table, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
interface Bike {
  id: number;
  bikeStandId: number | null
}

const ServicePage: React.FC = () => {
  const bikeData: Bike[] = [
    { id: 1, bikeStandId:1 },
    { id: 2, bikeStandId:2  },
    { id: 3, bikeStandId:3  },
    { id: 4, bikeStandId:4  },
    { id: 5, bikeStandId:null  },
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
            <th>Bike identifier</th>
            <th>Bike stand identifier</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {bikeData.map((bike) => (
            <tr key={bike.id}>
              <td>{bike.id}</td>
              <td>{bike.bikeStandId == null? "Currently on a road" : bike.bikeStandId}</td>
              <td>
              <Button
                  variant="outline-success"
                  size="sm"
                  onClick={() => handleMarkAsServiced(bike.id)}
                  disabled={bike.bikeStandId == null}
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
