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