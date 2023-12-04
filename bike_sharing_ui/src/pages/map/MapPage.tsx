// src/components/MapPage.tsx
import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import 'leaflet/dist/images/marker-icon.png';
interface Location {
  longitude: number;
  latitude: number;
}

interface MapObject {
  id: number;
  location: Location;
}
const bikeIcon = new L.Icon({
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41],
  });

const MapPage: React.FC<{ data: MapObject[] }> = ({ data }) => {
  const center: Location = { longitude: 0, latitude: 0 }; // Default center

  if (data.length > 0) {
    // Use the first location as the center if available
    center.longitude = data[0].location.longitude;
    center.latitude = data[0].location.latitude;
  }

  return (
    <MapContainer center={[51.505, -0.09]} zoom={13} scrollWheelZoom={false}>
  <TileLayer
    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
  />
   {data.map((object) => (
        <Marker
          key={object.id}
          position={[object.location.latitude, object.location.longitude]}
          icon={bikeIcon}
        >
          <Popup>{`ID: ${object.id}`}</Popup>
        </Marker>
      ))}
</MapContainer>
  );
};

export default MapPage;