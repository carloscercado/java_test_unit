--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.20
-- Dumped by pg_dump version 9.6.20

-- Started on 2021-07-13 15:30:52
--
-- TOC entry 12 (class 2615 OID 33236)
-- Name: mss_login; Type: SCHEMA; Schema: -; Owner: tuten_user
--

CREATE SCHEMA mss_login;


ALTER SCHEMA mss_login OWNER TO tuten_user;

--
-- TOC entry 193 (class 1259 OID 33497)
-- Name: tuten_administrator; Type: TABLE; Schema: mss_login; Owner: tuten_user
--

CREATE TABLE mss_login.tuten_administrator (
                                               username text NOT NULL,
                                               user_id bigint NOT NULL,
                                               email text,
                                               first_name text NOT NULL,
                                               last_name text NOT NULL,
                                               password_hash text NOT NULL,
                                               session_token text,
                                               mobile_device_token text,
                                               phone text,
                                               password_change_date timestamp without time zone DEFAULT now(),
                                               password_needs_reset boolean DEFAULT false NOT NULL,
                                               role_id text NOT NULL,
                                               contractor_id bigint,
                                               professional_id bigint,
                                               monitor_rule_id bigint,
                                               active boolean DEFAULT true NOT NULL,
                                               status text,
                                               lastest_activity_date timestamp with time zone,
                                               web_token text,
                                               tenant_id uuid,
                                               is_admin_tenant boolean DEFAULT false NOT NULL
);


ALTER TABLE mss_login.tuten_administrator OWNER TO tuten_user;

--
-- TOC entry 194 (class 1259 OID 33506)
-- Name: tuten_administrator_user_id_seq; Type: SEQUENCE; Schema: mss_login; Owner: tuten_user
--

CREATE SEQUENCE mss_login.tuten_administrator_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE mss_login.tuten_administrator_user_id_seq OWNER TO tuten_user;

--
-- TOC entry 3048 (class 0 OID 0)
-- Dependencies: 194
-- Name: tuten_administrator_user_id_seq; Type: SEQUENCE OWNED BY; Schema: mss_login; Owner: tuten_user
--

ALTER SEQUENCE mss_login.tuten_administrator_user_id_seq OWNED BY mss_login.tuten_administrator.user_id;

-- TOC entry 195 (class 1259 OID 33588)
-- Name: tuten_user_permission; Type: TABLE; Schema: mss_login; Owner: tuten_user
--

CREATE TABLE mss_login.tuten_user_permission (
                                                 permission_id text NOT NULL,
                                                 description text NOT NULL,
                                                 section text NOT NULL,
                                                 permission_type text NOT NULL
);


ALTER TABLE mss_login.tuten_user_permission OWNER TO tuten_user;

--
-- TOC entry 196 (class 1259 OID 33594)
-- Name: tuten_user_role; Type: TABLE; Schema: mss_login; Owner: tuten_user
--

CREATE TABLE mss_login.tuten_user_role (
                                           role_id text NOT NULL,
                                           name text NOT NULL,
                                           timeout integer DEFAULT 0 NOT NULL,
                                           multiple_sessions_allowed boolean DEFAULT true NOT NULL,
                                           active boolean DEFAULT true NOT NULL,
                                           password_expiration_months integer DEFAULT 0 NOT NULL,
                                           tenant_id uuid,
                                           is_created_by_tenant boolean DEFAULT true NOT NULL,
                                           is_professional boolean DEFAULT false NOT NULL,
                                           is_boweb_pro boolean DEFAULT false NOT NULL
);


ALTER TABLE mss_login.tuten_user_role OWNER TO tuten_user;

--
-- TOC entry 2886 (class 2604 OID 35154)
-- Name: tuten_administrator user_id; Type: DEFAULT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_administrator ALTER COLUMN user_id SET DEFAULT nextval('mss_login.tuten_administrator_user_id_seq'::regclass);



--
-- TOC entry 3037 (class 0 OID 33497)
-- Dependencies: 193
-- Data for Name: tuten_administrator; Type: TABLE DATA; Schema: mss_login; Owner: tuten_user
--

--
-- TOC entry 3050 (class 0 OID 0)
-- Dependencies: 194
-- Name: tuten_administrator_user_id_seq; Type: SEQUENCE SET; Schema: mss_login; Owner: tuten_user
--

SELECT pg_catalog.setval('mss_login.tuten_administrator_user_id_seq', 188, true);


--
-- TOC entry 3039 (class 0 OID 33588)
-- Dependencies: 195
-- Data for Name: tuten_user_permission; Type: TABLE DATA; Schema: mss_login; Owner: tuten_user
--

--
-- TOC entry 3040 (class 0 OID 33594)
-- Dependencies: 196
-- Data for Name: tuten_user_role; Type: TABLE DATA; Schema: mss_login; Owner: tuten_user
--

--
-- TOC entry 2910 (class 2606 OID 35395)
-- Name: tuten_user_role admin_user_role_pk; Type: CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_user_role
    ADD CONSTRAINT admin_user_role_pk PRIMARY KEY (role_id);


--
-- TOC entry 2898 (class 2606 OID 35397)
-- Name: tuten_administrator administrator_pk; Type: CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_administrator
    ADD CONSTRAINT administrator_pk PRIMARY KEY (user_id);



--
-- TOC entry 2902 (class 2606 OID 35421)
-- Name: tuten_administrator u_email_administrator; Type: CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_administrator
    ADD CONSTRAINT u_email_administrator UNIQUE (username);


--
-- TOC entry 2906 (class 2606 OID 35425)
-- Name: tuten_user_permission unique_permission; Type: CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_user_permission
    ADD CONSTRAINT unique_permission UNIQUE (description, section, permission_type);


--
-- TOC entry 2912 (class 2606 OID 35427)
-- Name: tuten_user_role unique_role_id_name; Type: CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_user_role
    ADD CONSTRAINT unique_role_id_name UNIQUE (role_id, name);


--
-- TOC entry 2904 (class 2606 OID 35431)
-- Name: tuten_administrator unique_session_token; Type: CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_administrator
    ADD CONSTRAINT unique_session_token UNIQUE (session_token);


--
-- TOC entry 2908 (class 2606 OID 35433)
-- Name: tuten_user_permission user_permission_pk; Type: CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_user_permission
    ADD CONSTRAINT user_permission_pk PRIMARY KEY (permission_id);


--
-- TOC entry 2899 (class 1259 OID 35826)
-- Name: tuten_administrator_session_token_idx; Type: INDEX; Schema: mss_login; Owner: tuten_user
--

CREATE INDEX tuten_administrator_session_token_idx ON mss_login.tuten_administrator USING btree (session_token);


--
-- TOC entry 2900 (class 1259 OID 35827)
-- Name: tuten_administrator_user_id_idx; Type: INDEX; Schema: mss_login; Owner: tuten_user
--

CREATE INDEX tuten_administrator_user_id_idx ON mss_login.tuten_administrator USING btree (user_id);


--
-- TOC entry 2915 (class 2606 OID 35909)
-- Name: tuten_administrator admin_pro_id_fk; Type: FK CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_administrator
    ADD CONSTRAINT admin_pro_id_fk FOREIGN KEY (professional_id) REFERENCES public.tuten_contractors_professional(professional_id);


--
-- TOC entry 2916 (class 2606 OID 35914)
-- Name: tuten_administrator admin_role_id_fk; Type: FK CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_administrator
    ADD CONSTRAINT admin_role_id_fk FOREIGN KEY (role_id) REFERENCES mss_login.tuten_user_role(role_id);


--
-- TOC entry 2917 (class 2606 OID 35964)
-- Name: tuten_administrator user_contractor_id_fk; Type: FK CONSTRAINT; Schema: mss_login; Owner: tuten_user
--

ALTER TABLE ONLY mss_login.tuten_administrator
    ADD CONSTRAINT user_contractor_id_fk FOREIGN KEY (contractor_id) REFERENCES public.tuten_contractor(contractor_id);


CREATE TABLE mss_login.tuten_role_permission (
                                                 role_id text NOT NULL,
                                                 permission_id text NOT NULL,
                                                 CONSTRAINT permission_role_id PRIMARY KEY (role_id, permission_id),
                                                 CONSTRAINT permission_id_fk FOREIGN KEY (permission_id) REFERENCES mss_login.tuten_user_permission(permission_id) ON UPDATE CASCADE,
                                                 CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES mss_login.tuten_user_role(role_id) ON UPDATE CASCADE
);


-- Completed on 2021-07-13 15:30:52

--
-- PostgreSQL database dump complete
--

