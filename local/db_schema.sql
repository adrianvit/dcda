-- Table: users

-- DROP TABLE users;

CREATE TABLE users
(
  userid bigint NOT NULL,
  firstname character varying(50),
  lastname character varying(50) NOT NULL,
  phone character varying(20) NOT NULL,
  email character varying(200) NOT NULL,
  pass character varying(100) NOT NULL,
  role character varying(100) NOT NULL,
  CONSTRAINT pk_userid PRIMARY KEY (userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users OWNER TO postgres;


-- Table: addresses

-- DROP TABLE addresses;

CREATE TABLE addresses
(
  addressid bigserial NOT NULL,
  country character varying(50) NOT NULL,
  county character varying(50) NOT NULL,
  city character varying(50) NOT NULL,
  street character varying(100),
  street_number character varying(20) NOT NULL,
  postal_code character varying(20) NOT NULL,
  userid bigint NOT NULL,
  CONSTRAINT pk_addressid PRIMARY KEY (addressid),
  CONSTRAINT fk_userid FOREIGN KEY (userid)
      REFERENCES users (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE addresses OWNER TO postgres;


-- Table: loggedusers

-- DROP TABLE loggedusers;

CREATE TABLE loggedusers
(
  userid bigint NOT NULL,
  token character varying(36),
  expiration timestamp(6) with time zone,
  role character varying(100) NOT NULL,
  CONSTRAINT pk_logged PRIMARY KEY (userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE loggedusers OWNER TO postgres;


-- Table: products

-- DROP TABLE products;

CREATE TABLE products
(
  productid bigint NOT NULL,
  description text,
  category integer NOT NULL,
  quantity integer NOT NULL,
  price integer NOT NULL,
  CONSTRAINT pk_product PRIMARY KEY (productid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE products OWNER TO postgres;