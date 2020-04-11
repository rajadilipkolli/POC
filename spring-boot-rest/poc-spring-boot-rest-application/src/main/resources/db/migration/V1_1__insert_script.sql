INSERT INTO
   CUSTOMER
    (DATE_OF_BIRTH, FIRST_NAME, LAST_NAME, ID)
VALUES
    (
        '1982-01-10', 'Raja', 'Kolli', 1
   )
,
    (
        '1973-01-03', 'Paul', 'Jones', 2
   )
,
    (
        '1979-03-08', 'Steve', 'Toale', 3
   )
;

INSERT INTO
   ADDRESS
    (COUNTY, POSTCODE, STREET, TOWN , CUSTOMER_ID)
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
    ORDERS
    (created_on, customer_id, order_number, order_status, ORDER_ID)
VALUES
    (
        '2018-06-19', 1, 'ORD1', 'NEW', 1
    )
;

INSERT INTO
    POST
    (TITLE, ID)
VALUES
    (
        'A Beautiful Post in Java', 1
    )
,
    (
        'Second Post', 2
    )
;

INSERT INTO
    POST_COMMENT
    (POST_ID, REVIEW, ID)
VALUES
    (
        1, 'Good' , 1
    )
,
    (
        1, 'Excellent' , 2
    )
;

INSERT INTO
    TAG
    (ID, NAME)
VALUES
    (
        1, 'Java'
    )
,
    (
        2, 'Spring Boot'
    )
;

INSERT INTO
    POST_TAG
    (POST_ID, TAG_ID)
VALUES
    (
        1, 1
    )
,
    (
        1, 2
    )
;

INSERT INTO
    POST_DETAILS
    (POST_ID, CREATED_BY)
VALUES
    (
        1, 'raja'
    )
,
    (
        2, 'raja'
    )
;