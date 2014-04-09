# --- !Ups

CREATE SEQUENCE merchant_id_seq;
CREATE TABLE merchants (
    id integer NOT NULL DEFAULT nextval('merchant_id_seq'),
    name varchar(255),
    address varchar(255)
);

# --- !Downs

DROP TABLE merchants;
DROP SEQUENCE merchant_id_seq;