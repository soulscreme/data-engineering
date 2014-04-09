# --- !Ups

CREATE SEQUENCE purchaser_id_seq;
CREATE TABLE purchasers (
    id integer NOT NULL DEFAULT nextval('purchaser_id_seq'),
    name varchar(255)
);

# --- !Downs

DROP TABLE purchasers;
DROP SEQUENCE purchaser_id_seq;