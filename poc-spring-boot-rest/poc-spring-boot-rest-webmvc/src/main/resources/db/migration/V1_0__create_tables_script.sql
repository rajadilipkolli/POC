CREATE SEQUENCE sequence_generator
  START WITH 1000
  INCREMENT BY 5;

CREATE TABLE POST
(
    ID BIGINT NOT NULL,
    TITLE VARCHAR(255),
    CONTENT VARCHAR(255),
    updated_on timestamp,
    PRIMARY KEY   (ID)
);

create table POST_COMMENT
(
    id bigint not null,
    review varchar(255),
    created_on timestamp,
    POST_ID bigint,
    primary key (id),
    CONSTRAINT FK_POST_COMMENT FOREIGN KEY (POST_ID) REFERENCES POST(ID)
);

create table post_details
(
    created_by varchar(255),
    created_on timestamp,
    POST_ID bigint not null,
    primary key (POST_ID),
    CONSTRAINT FK_POST_DETAILS FOREIGN KEY (POST_ID) REFERENCES POST(ID)
);

create table tag
(
    ID bigint not null,
    name varchar(255) not null,
    primary key (ID),
    CONSTRAINT UK_TAG_NAME UNIQUE (NAME)
);

create table post_tag
(
    POST_ID bigint not null,
    TAG_ID bigint not null,
    created_on timestamp,
    CONSTRAINT FK_POST_TAG_POST FOREIGN KEY (POST_ID) REFERENCES POST(ID),
    CONSTRAINT FK_POST_TAG_TAG FOREIGN KEY (TAG_ID) REFERENCES TAG(ID)
);