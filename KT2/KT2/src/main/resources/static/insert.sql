-- ADMIN: 								admin@admin.admin | Password.123
-- CLIENT (INDIVIDUAL | BASE): 			mail1@mail.mail | Password.123
-- CLIENT (INDIVIDUAL | STANDARD): 		mail1@mail.mail | Password.123
-- CLIENT (INDIVIDUAL | GOLD): 			mail1@mail.mail | Password.123
-- CLIENT (LEGAL ENTITY | BASE): 		mail1@mail.mail | Password.123
-- CLIENT (LEGAL ENTITY | STANDARD): 	mail1@mail.mail | Password.123
-- CLIENT (LEGAL ENTITY | GOLD): 		mail1@mail.mail | Password.123

INSERT INTO public._user(client_package, id, is_activated, role, type, address, city, company_name, country, first_name, last_name, password, password_salt, phone_number, tax_identification_number, username, created_at, is_first_login, is2factor_authentication_enabled, is_blocked)
	VALUES (2, 100, true, 2, 1, 'Street 1', 'City', 'Company', 'Country', 'Name', 'Surname', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '123456789', 'admin@admin.admin', '2024-05-05 14:30:00',true,false, false),
 	(0, 101, true, 0, 0, 'Street 1', 'City', '', 'Country', 'Name', 'Surname', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '', 'mail1@mail.mail', '2024-05-05 14:30:00',false,false, false),
	(1, 102, true, 0, 0, 'Street 1', 'City', '', 'Country', 'Name', 'Surname', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '', 'mail2@mail.mail', '2024-05-05 14:30:00',false,false, false),
	(2, 103, true, 0, 0, 'Street 1', 'City', '', 'Country', 'Name', 'Surname', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '', 'mail3@mail.mail', '2024-05-05 14:30:00',false,false, false),
	(0, 104, true, 0, 1, 'Street 1', 'City', 'Company', 'Country', '', '', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '123456789', 'mail4@mail.mail', '2024-05-05 14:30:00',false,false, false),
	(1, 105, true, 0, 1, 'Street 1', 'City', 'Company', 'Country', '', '', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '123456789', 'mail5@mail.mail', '2024-05-05 14:30:00',false,false, false),
	(2, 106, true, 0, 1, 'Street 1', 'City', 'Company', 'Country', '', '', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '123456789', 'mail6@mail.mail', '2024-05-05 14:30:00',false,false, false),
	(2, 201, true, 2, 1, 'Street 1', 'City', 'Company', 'Country', 'Name', 'Surname', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '123456789', 'admin2@admin.admin', '2024-05-05 14:30:00',false,false, false),
	(2, 202, true, 1, 1, 'Street 1', 'City', 'Company', 'Country', 'Name', 'Surname', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '123456789', 'employee2@admin.admin', '2024-05-05 14:30:00',false,false, false),
	(2, 302, true, 0, 1, 'Street 1', 'City', 'Company', 'Country', 'Name', 'Surname', 'fd9e7afd41ff90aaa7337fab11aa90362cc3bb8574cfcb38cca2b3034ead4bd8', 'T2X1zXl/FtX/yMWaCwHy4g==', '+111 11 111 1111', '123456789', 'nikolicmarko1243@gmail.com', '2024-05-05 14:30:00',false,false, false);



INSERT INTO public._advertisements (id, request_id, username, active_from, active_until, description, slogan)
VALUES 
    (101, 101, 'nikolicmarko1243@gmail.com', '2025-06-01 00:00:00', '2025-07-01 00:00:00', 'Ad description 1', 'Ad slogan 1'),
    (102, 102, 'nikolicmarko1243@gmail.com', '2025-06-02 00:00:00', '2025-07-02 00:00:00', 'Ad description 2', 'Ad slogan 2'),
    (103, 103, 'nikolicmarko1243@gmail.com', '2025-06-03 00:00:00', '2025-07-03 00:00:00', 'Ad description 3', 'Ad slogan 3'),
    (104, 104, 'nikolicmarko1243@gmail.com', '2025-06-04 00:00:00', '2025-07-04 00:00:00', 'Ad description 4', 'Ad slogan 4'),
    (105, 105, 'nikolicmarko1243@gmail.com', '2025-06-05 00:00:00', '2025-07-05 00:00:00', 'Ad description 5', 'Ad slogan 5');
