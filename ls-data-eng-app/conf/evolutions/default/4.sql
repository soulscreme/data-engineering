# --- !Ups

CREATE SEQUENCE order_id_seq;
CREATE TABLE orders (
 id integer NOT NULL DEFAULT nextval('order_id_seq'),
 purchaser_id integer NOT NULL REFERENCES purchasers (id),
 item_id integer NOT NULL REFERENCES items (id),
 quantity integer,
);

# --- !Downs

DROP TABLE orders;
DROP SEQUENCE order_id_seq;