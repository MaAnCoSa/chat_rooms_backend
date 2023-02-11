--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1
-- Dumped by pg_dump version 15.1

-- Started on 2023-02-11 02:37:18 MST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16767)
-- Name: conversation; Type: TABLE; Schema: public; Owner: manuelcota
--

CREATE TABLE public.conversation (
    id uuid NOT NULL,
    notif1 character varying(255),
    notif2 character varying(255),
    room_id uuid,
    user1 character varying(255),
    user2 character varying(255)
);


ALTER TABLE public.conversation OWNER TO manuelcota;

--
-- TOC entry 216 (class 1259 OID 16774)
-- Name: conversation_messages; Type: TABLE; Schema: public; Owner: manuelcota
--

CREATE TABLE public.conversation_messages (
    conversation_id uuid NOT NULL,
    messages_id uuid NOT NULL
);


ALTER TABLE public.conversation_messages OWNER TO manuelcota;

--
-- TOC entry 217 (class 1259 OID 16777)
-- Name: message; Type: TABLE; Schema: public; Owner: manuelcota
--

CREATE TABLE public.message (
    id uuid NOT NULL,
    date character varying(255),
    message character varying(255),
    receiver_name character varying(255),
    roomid uuid,
    sender_name character varying(255)
);


ALTER TABLE public.message OWNER TO manuelcota;

--
-- TOC entry 218 (class 1259 OID 16784)
-- Name: rooms; Type: TABLE; Schema: public; Owner: manuelcota
--

CREATE TABLE public.rooms (
    id uuid NOT NULL,
    conversations character varying(255)[],
    statuses character varying(255)[],
    users character varying(255)[]
);


ALTER TABLE public.rooms OWNER TO manuelcota;

--
-- TOC entry 219 (class 1259 OID 16791)
-- Name: rooms_user_list; Type: TABLE; Schema: public; Owner: manuelcota
--

CREATE TABLE public.rooms_user_list (
    room_id uuid NOT NULL,
    user_list_username character varying(255) NOT NULL
);


ALTER TABLE public.rooms_user_list OWNER TO manuelcota;

--
-- TOC entry 220 (class 1259 OID 16794)
-- Name: users; Type: TABLE; Schema: public; Owner: manuelcota
--

CREATE TABLE public.users (
    username character varying(255) NOT NULL,
    password character varying(255),
    roles character varying(255),
    token uuid
);


ALTER TABLE public.users OWNER TO manuelcota;

--
-- TOC entry 214 (class 1259 OID 16535)
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: manuelcota
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_seq OWNER TO manuelcota;

--
-- TOC entry 3482 (class 2606 OID 16773)
-- Name: conversation conversation_pkey; Type: CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.conversation
    ADD CONSTRAINT conversation_pkey PRIMARY KEY (id);


--
-- TOC entry 3484 (class 2606 OID 16783)
-- Name: message message_pkey; Type: CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- TOC entry 3486 (class 2606 OID 16790)
-- Name: rooms rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id);


--
-- TOC entry 3488 (class 2606 OID 16800)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);


--
-- TOC entry 3491 (class 2606 OID 16816)
-- Name: rooms_user_list fk884wu7tkxkxoj6870cbnf2fw2; Type: FK CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.rooms_user_list
    ADD CONSTRAINT fk884wu7tkxkxoj6870cbnf2fw2 FOREIGN KEY (room_id) REFERENCES public.rooms(id);


--
-- TOC entry 3492 (class 2606 OID 16811)
-- Name: rooms_user_list fkdg1fsu382beqmscodew8nrp01; Type: FK CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.rooms_user_list
    ADD CONSTRAINT fkdg1fsu382beqmscodew8nrp01 FOREIGN KEY (user_list_username) REFERENCES public.users(username);


--
-- TOC entry 3489 (class 2606 OID 16806)
-- Name: conversation_messages fkowwk6j0v3ydi001gu4m5lb39d; Type: FK CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.conversation_messages
    ADD CONSTRAINT fkowwk6j0v3ydi001gu4m5lb39d FOREIGN KEY (conversation_id) REFERENCES public.conversation(id);


--
-- TOC entry 3490 (class 2606 OID 16801)
-- Name: conversation_messages fksrkwqqihpmo60bbny5xibdx3r; Type: FK CONSTRAINT; Schema: public; Owner: manuelcota
--

ALTER TABLE ONLY public.conversation_messages
    ADD CONSTRAINT fksrkwqqihpmo60bbny5xibdx3r FOREIGN KEY (messages_id) REFERENCES public.message(id);


-- Completed on 2023-02-11 02:37:18 MST

--
-- PostgreSQL database dump complete
--

