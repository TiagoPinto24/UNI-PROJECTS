insert into application_user (version, id, username,email,name,hashed_password,user_type) values (1, '3','sponsor','sponsor@gexample.com','Sponsor sponsor','$2a$10$q6t8RW.XKUYlfS2G8gWn8escpmhGBuwLH4cz9sXVLqOLyeNVK1B0C','SPONSOR')
insert into user_roles (user_id, roles) values ('3', 'USER')

insert into application_user (version, id, username,name,email,hashed_password,user_type) values (1, '4','producer','producer@example.com','Producer producer','$2a$10$A8Or76MCtd3Rk1GV338CZ.h1S.ALDNM3gmnOPgwqcsZk29.F0Sq22','PRODUCER')
insert into user_roles (user_id, roles) values ('4', 'USER')
//--------------------------------------------------------------------------------------------------------

insert into post (description, image_path) values ('The donkey spent the day sleeping underneath a tree', 'https://storage.googleapis.com/donkey-image-storage/burro1.jpg')
insert into post (description, image_path) values ('Today this donkey eat a lot', 'https://storage.googleapis.com/donkey-image-storage/burro2.jpg')
insert into post (description, image_path) values ('This donkey spent the day running and playing with his friends', 'https://storage.googleapis.com/donkey-image-storage/burro3.jpg')