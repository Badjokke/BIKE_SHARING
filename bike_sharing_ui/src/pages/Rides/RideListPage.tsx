// src/components/RideListPage.tsx
import React, { useEffect } from 'react';
import { Table } from 'react-bootstrap';
import { useState } from 'react';
import { fetchUserRides,Ride,USER_RIDES_RESPONSE} from '../../api/user_service_api/UserApiCaller';
import { useNavigate } from 'react-router-dom';
/*
interface Ride {
  rideId: number;
  userId: number;
  bikeId: number;
  startStandId: number;
  endStandId: number;
  rideStart: string;
  rideEnd: string;
}*/

const RideListPage: React.FC = () => {
 const [rideData,setRideData] = useState<Ride[]|null|undefined>(undefined);
 const navigate = useNavigate();
  /* const rideData: Ride[] = [
    {
      rideId: 1,
      userId: 1,
      bikeId: 1,
      startStandId: 5,
      endStandId: 1,
      rideStart: '2023-11-23T23:00:00Z',
      rideEnd: '2023-11-24T23:00:00Z',
    },
    {
      rideId: 2,
      userId: 1,
      bikeId: 1,
      startStandId: 5,
      endStandId: 1,
      rideStart: '2023-11-23T23:00:00Z',
      rideEnd: '2023-11-24T23:30:00Z',
    },
  ];*/

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
      <h1>Ride List</h1>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Ride ID</th>
            <th>User ID</th>
            <th>Bike ID</th>
            <th>Start Stand ID</th>
            <th>End Stand ID</th>
            <th>Ride Start</th>
            <th>Ride End</th>
            <th>Ride Duration</th>
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

          (rideData===null?(<h1>User has no rides</h1>):(<h1>loading user rides</h1>))
  )
};

export default RideListPage;