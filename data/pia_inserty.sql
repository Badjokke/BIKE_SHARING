insert into bike_service.users(email_address, name, password, role) values (
                                                                             'kocka@kocka.cz', 'kocka', '7d4e3eec80026719639ed4dba68916eb94c7a49a053e05c8f9578fe4e5a3d7ea', '1');

insert into bike_location.stands(latitude, longitude,name)
values
  (51.515579783755925,	-0.13183593750000003,	'stand1'),
  (51.53096001302977,	0.050811767578125,	'stand2'),
  (51.456574106519724,	-0.12153625488281251,	'stand3'),
  (	51.4873692036782,	-0.31723022460937506,	'stand4'),
  (	51.421904771506135,	-0.13526916503906253,	'stand5');


insert into bike_location.bikes (last_service, latitude, longitude, stand_id)
values
  ('2023-11-19 12:30:00.000000', 51.421904771506135, -0.13526916503906253, 5),
  ('2023-11-18 15:45:00.000000', 51.421938222973054, -0.13554811477661136, 4),
  ( '2023-12-17 00:00:00.000000', 51.487569624487115, -0.3167366981506348, 2),
  ('2023-11-16 14:20:00.000000', 51.515579783755925, -0.13183593750000003, 1),
  ('2023-12-18 00:00:00.000000', 51.421904771506135, -0.13526916503906253, 5);

insert into bike_location.rides(end_timestamp, start_timestamp, user_id, bike_id, end_stand_id, start_stand_id)
values
  ('2023-12-18 22:57:28.732000', '2023-12-18 22:57:03.076000', (select id from bike_service.users where email = 'kocka@kocka.cz'), 1, 5, 4),
  ('2023-12-18 23:26:11.512000', '2023-12-18 23:25:56.785000', (select id from bike_service.users where email = 'kocka@kocka.cz'), 5, 5, 4);
  
