use bike_location;



INSERT INTO stands(latitude,longitude,name)
VALUES (11,16,"stand1"),(10,15,"stand2"), (10,15,"stand3"),(10,15,"stand4"),(10,15,"stand5");

INSERT INTO bikes (last_service, latitude, longitude, stand_id)
VALUES
  ('2023-11-19 12:30:00', 37.7749, -122.4194, 3),
  ('2023-11-18 15:45:00', 40.7128, -74.0060, 1),
  ('2023-11-17 09:00:00', 34.0522, -118.2437, 5),
  ('2023-11-16 14:20:00', 41.8781, -87.6298, 2),
  ('2023-11-15 10:10:00', 51.5074, -0.1278, 4);

