-- Table: users

-- DROP TABLE local.users;

CREATE TABLE local.users
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
ALTER TABLE local.users OWNER TO postgres;


-- Table: addresses

-- DROP TABLE local.addresses;

CREATE TABLE local.addresses
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
      REFERENCES local.users (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE local.addresses OWNER TO postgres;


-- Table: loggedusers

-- DROP TABLE local.loggedusers;

CREATE TABLE local.loggedusers
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
ALTER TABLE local.loggedusers OWNER TO postgres;


-- Table: products

-- DROP TABLE local.products;

CREATE TABLE local.products
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
ALTER TABLE local.products OWNER TO postgres;


-- Table: peers

-- DROP TABLE local.peers;

CREATE TABLE local.peers
(
  peerid bigint NOT NULL,
  url character varying(100),
  distance integer NOT NULL,
  CONSTRAINT pk_product PRIMARY KEY (peerid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE local.products OWNER TO postgres;
