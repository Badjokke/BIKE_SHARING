// src/components/BikeListPage.tsx
import React, { useEffect, useState } from 'react';
import { Table, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { fetchBikesDueForService,ServiceableBikeObject,markBikeAsService } from '../../api/bike_api/BikeApi';
import { useTranslation } from 'react-i18next';

const ServicePage: React.FC = () => {
  const {t} = useTranslation();

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

  const handleMarkAsServiced = async (bikeId: number) => {
    // Custom function to handle marking a bike as serviced
    console.log(`Bike ${bikeId} marked as serviced`);
    markBikeAsService(bikeId).then((message)=>{
      console.log(message);
      fetchServicableBikes();});
  };
  return (
    bikeData != null? (<div className="container mt-4">
      <h1>{t("Bikes for service")}</h1>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>{t("Bike")} identifier</th>
            <th>{t("Bike")} {t("Stand")} identifier</th>
            <th>{t("Last serviced")}</th>
            <th>{t("Action")}</th>
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
                  {t("Mark as Serviced")}
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>) : (<h1>{t("Fetching bike data")}</h1>)
  );
};

export default ServicePage;
