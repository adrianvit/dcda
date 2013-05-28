--
-- PostgreSQL database dump
--

-- Dumped from database version 9.0.4
-- Dumped by pg_dump version 9.0.4
-- Started on 2013-05-28 07:25:26

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = central, pg_catalog;

--
-- TOC entry 1871 (class 0 OID 0)
-- Dependencies: 1541
-- Name: emails_id_seq; Type: SEQUENCE SET; Schema: central; Owner: postgres
--

SELECT pg_catalog.setval('emails_id_seq', 4, true);


SET search_path = local, pg_catalog;

--
-- TOC entry 1872 (class 0 OID 0)
-- Dependencies: 1537
-- Name: addresses_addressid_seq; Type: SEQUENCE SET; Schema: local; Owner: postgres
--

SELECT pg_catalog.setval('addresses_addressid_seq', 1, false);


--
-- TOC entry 1873 (class 0 OID 0)
-- Dependencies: 1545
-- Name: orders_orderid_seq; Type: SEQUENCE SET; Schema: local; Owner: postgres
--

SELECT pg_catalog.setval('orders_orderid_seq', 2, true);


SET search_path = public, pg_catalog;

--
-- TOC entry 1874 (class 0 OID 0)
-- Dependencies: 1532
-- Name: addresses_addressid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('addresses_addressid_seq', 1, false);


SET search_path = central, pg_catalog;

--
-- TOC entry 1865 (class 0 OID 91489)
-- Dependencies: 1542
-- Data for Name: emails; Type: TABLE DATA; Schema: central; Owner: postgres
--

INSERT INTO emails VALUES (1, 'asdf@asdf.com');
INSERT INTO emails VALUES (3, 'asd123f@asdf.com');
INSERT INTO emails VALUES (4, 'qwerty@qwerty.com');


--
-- TOC entry 1866 (class 0 OID 91497)
-- Dependencies: 1543
-- Data for Name: servers; Type: TABLE DATA; Schema: central; Owner: postgres
--

INSERT INTO servers VALUES ('Timis', '79.114.103.120');
INSERT INTO servers VALUES ('Bucuesti', '79.114.103.120');


SET search_path = local, pg_catalog;

--
-- TOC entry 1861 (class 0 OID 91452)
-- Dependencies: 1536
-- Data for Name: users; Type: TABLE DATA; Schema: local; Owner: postgres
--

INSERT INTO users VALUES (1, 'firstname', 'lastname', 'phone', 'asd@asd.com', 'qwerty', 'admin', NULL);
INSERT INTO users VALUES (3, 'Adfg', 'Dsd', '455', 'asd123f@asdf.com', 'qwerty', 'Timiș', NULL);
INSERT INTO users VALUES (4, 'Adf', 'Asf', '557', 'qwerty@qwerty.com', 'qwerty', 'user', NULL);


--
-- TOC entry 1862 (class 0 OID 91462)
-- Dependencies: 1538 1861
-- Data for Name: addresses; Type: TABLE DATA; Schema: local; Owner: postgres
--



--
-- TOC entry 1863 (class 0 OID 91473)
-- Dependencies: 1539
-- Data for Name: loggedusers; Type: TABLE DATA; Schema: local; Owner: postgres
--

INSERT INTO loggedusers VALUES (4, 'daab4d52-b01c-4b0a-b65e-c375ab59c100', '2013-05-28 05:09:13.662+03', 'user');


--
-- TOC entry 1868 (class 0 OID 99711)
-- Dependencies: 1546 1861
-- Data for Name: orders; Type: TABLE DATA; Schema: local; Owner: postgres
--

INSERT INTO orders VALUES (1, 'Started', 1, '"localhost"', 1, 4);
INSERT INTO orders VALUES (2, 'Started', 1, '"localhost"', 1, 4);


--
-- TOC entry 1867 (class 0 OID 91505)
-- Dependencies: 1544
-- Data for Name: peers; Type: TABLE DATA; Schema: local; Owner: postgres
--

INSERT INTO peers VALUES (1, '192.168.1.103', 5);


--
-- TOC entry 1864 (class 0 OID 91478)
-- Dependencies: 1540
-- Data for Name: products; Type: TABLE DATA; Schema: local; Owner: postgres
--

INSERT INTO products VALUES (1, 'text', 1, 100, 10);
INSERT INTO products VALUES (2, 'text', 2, 100, 12);
INSERT INTO products VALUES (3, 'text', 3, 100, 13);
INSERT INTO products VALUES (4, 'text', 4, 100, 14);
INSERT INTO products VALUES (11, 'text', 1, 100, 210);
INSERT INTO products VALUES (21, 'text', 2, 100, 212);
INSERT INTO products VALUES (31, 'text', 3, 100, 213);
INSERT INTO products VALUES (41, 'text', 4, 100, 214);


SET search_path = public, pg_catalog;

--
-- TOC entry 1857 (class 0 OID 91391)
-- Dependencies: 1531
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1858 (class 0 OID 91401)
-- Dependencies: 1533 1857
-- Data for Name: addresses; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1859 (class 0 OID 91412)
-- Dependencies: 1534
-- Data for Name: loggedusers; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1860 (class 0 OID 91426)
-- Dependencies: 1535
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO products VALUES (1, 'text', 1, 100, 10);
INSERT INTO products VALUES (2, 'text', 2, 100, 12);
INSERT INTO products VALUES (3, 'text', 3, 100, 13);
INSERT INTO products VALUES (4, 'text', 4, 100, 14);


-- Completed on 2013-05-28 07:25:27

--
-- PostgreSQL database dump complete
--

