// src/components/BikeListPage.tsx
import React, { useEffect, useState } from 'react';
import { Table, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { fetchBikesDueForService,ServiceableBikeObject,markBikeAsService } from '../../api/bike_api/BikeApi';
interface Bike {
  id: number;
  bikeStandId: number | null
}

const ServicePage: React.FC = () => {


  const [bikeData,setBikeData] = useState<ServiceableBikeObject[]|null>();


  const fetchServicableBikes = async () =>{
      const response = await fetchBikesDueForService();
      if(response == null){
        throw new Error("Failed to fetch bikes");
      }
      setBikeData(response);
  }

  useEffect(()=>{
      fetchServicableBikes();
  },[])




  /*
  const bikeData: Bike[] = [
    { id: 1, bikeStandId:1 },
    { id: 2, bikeStandId:2  },
    { id: 3, bikeStandId:3  },
    { id: 4, bikeStandId:4  },
    { id: 5, bikeStandId:null  },
  ];
*/
  const handleMarkAsServiced = async (bikeId: number) => {
    // Custom function to handle marking a bike as serviced
    console.log(`Bike ${bikeId} marked as serviced`);
    markBikeAsService(bikeId).then((message)=>{
      console.log(message);
      fetchServicableBikes();});
  };
  return (
    bikeData != null? (<div className="container mt-4">
      <h1>Bikes for service</h1>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Bike identifier</th>
            <th>Bike stand identifier</th>
            <th>Last serviced</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {bikeData.map((bike) => (
            <tr key={bike.bikeId}>
              <td>{bike.bikeId}</td>
              <td>{bike.standId == null? "Currently on a road" : bike.bikeId}</td>
              <td>{bike.lastServiced}</td>
              <td>
              <Button
                  variant="outline-success"
                  size="sm"
                  onClick={() => handleMarkAsServiced(bike.bikeId)}
                  disabled={bike.standId == null}
                >
                  <FontAwesomeIcon icon={faCheckCircle} className="mr-2" />
                  Mark as Serviced
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>) : (<h1>fetching bike data</h1>)
  );
};

export default ServicePage;
