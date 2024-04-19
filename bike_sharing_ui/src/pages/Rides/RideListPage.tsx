// src/components/RideListPage.tsx
import React, { useEffect } from 'react';
import { Table } from 'react-bootstrap';
import { useState } from 'react';
import { fetchUserRides,Ride} from '../../api/user_service_api/UserApiCaller';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

const RideListPage: React.FC = () => {
 const {t} = useTranslation();
 const [rideData,setRideData] = useState<Ride[]|null|undefined>(undefined);
 const navigate = useNavigate();

  const fetchRides = async () => {
    const response = await fetchUserRides();
    if(response == null){
      return;
    }
    //
    if (response.redirectTo){
        console.log("invalid request");
        navigate(response.redirectTo);
        return;
    }
    
    const userRideData = response.rides;
    userRideData.length == 0? setRideData(null):setRideData(userRideData);
  }


  useEffect(()=>{
    fetchRides();
  },[])




  const calculateRideDuration = (ride: Ride): string => {
    const rideStart = new Date(ride.rideStart);
    const rideEnd = new Date(ride.rideEnd);

    const durationInMilliseconds = rideEnd.getTime() - rideStart.getTime();
    const days = Math.floor(durationInMilliseconds / (24 * 60 * 60 * 1000));
    const hours = Math.floor((durationInMilliseconds % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000));
    const minutes = Math.floor((durationInMilliseconds % (60 * 60 * 1000)) / (60 * 1000));

    return `${days > 0 ? `${days}d ` : ''}${hours > 0 ? `${hours}h ` : ''}${minutes}m`;
  };

  return (
    (rideData)? ( <div className="container mt-4">
      <h1>{t("Ride List")}</h1>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>{t("Ride")} ID</th>
            <th>{t("User")} ID</th>
            <th>{t("Bike")} ID</th>
            <th>{t("Start")} {t("Stand")} ID</th>
            <th>{t("End")} {t("Stand")} ID</th>
            <th>{t("Ride")} {t("Start")}</th>
            <th>{t("Ride")} {t("End")}</th>
            <th>{t("Ride")} {t("Duration")}</th>
          </tr>
        </thead>
        <tbody>
          {rideData.map((ride) => (
            <tr key={ride.rideId}>
              <td>{ride.rideId}</td>
              <td>{ride.userId}</td>
              <td>{ride.bikeId}</td>
              <td>{ride.startStandId}</td>
              <td>{ride.endStandId}</td>
              <td>{ride.rideStart}</td>
              <td>{ride.rideEnd}</td>
              <td>{calculateRideDuration(ride)}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
          ):

          (rideData===null?(<h1>{t("User has no rides")}</h1>):(<h1>{t("Loading user rides")}</h1>))
  )
};

export default RideListPage;