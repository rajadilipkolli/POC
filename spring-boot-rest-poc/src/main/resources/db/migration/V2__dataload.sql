INSERT INTO
   ADDRESS (COUNTY, POSTCODE, STREET, TOWN , ID) 
VALUES
   (
      'India', 'BT893PY', 'High Street', 'Belfast', 1
   )
, 
   (
      'Armagh', 'BT283FG', 'Main Street', 'Lurgan', 2
   )
, 
   (
      'Down', 'BT359JK', 'Main Street', 'Newry', 3
   )
;

INSERT INTO
   CUSTOMER (ADDRESS_ID, DATE_OF_BIRTH, FIRST_NAME, LAST_NAME, ID) 
VALUES
   (
      1, '1982-01-10', 'Raja', 'Kolli', 4
   )
, 
   (
      2, '1973-01-03', 'Paul', 'Jones', 5
   )
, 
   (
      3, '1979-03-08', 'Steve', 'Toale', 6
   )
;