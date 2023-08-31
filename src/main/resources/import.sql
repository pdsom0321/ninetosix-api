INSERT INTO company(NAME) VALUES('GS ITM');

INSERT INTO location (NAME, latitude, longitude) VALUES('역삼 GS 타워', 37.50195492996154, 127.03732902930322);
INSERT INTO location (NAME, latitude, longitude) VALUES('보헌 빌딩', 37.57838965148805, 126.98667257949424);
INSERT INTO location (NAME, latitude, longitude) VALUES('원서 빌딩', 37.57917595553029, 126.98844416432452);

INSERT INTO company_location (company_id, location_id) VALUES(1,1);
INSERT INTO company_location (company_id, location_id) VALUES(1,2);
INSERT INTO company_location (company_id, location_id) VALUES(1,3);

INSERT INTO team (name, company_id) VALUES('모바일팀', 1);
INSERT INTO team (name, company_id) VALUES('기타', 1);
