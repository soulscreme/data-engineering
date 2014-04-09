# --- !Ups

CREATE SEQUENCE item_id_seq;
CREATE TABLE items (
    id integer NOT NULL DEFAULT nextval('item_id_seq'),
    description varchar(255),
    price decimal,
    merchant_id integer NOT NULL,
    FOREIGN KEY (merchant_id) REFERENCES merchants (id)
);

# --- !Downs

DROP TABLE item;
DROP SEQUENCE item_id_seq;