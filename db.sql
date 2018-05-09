--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3
-- Dumped by pg_dump version 10.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: correctrealauthormark(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.correctrealauthormark() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  _author_id integer := OLD.author_id;
BEGIN

  UPDATE spring_database.public.authors SET mark = getCommonAuthorMark(_author_id)
  WHERE authors.author_id = _author_id;

  RETURN NULL;
END
$$;


ALTER FUNCTION public.correctrealauthormark() OWNER TO postgres;

--
-- Name: correctrealbookmark(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.correctrealbookmark() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  _book_id integer := OLD.book_id;
BEGIN

  UPDATE spring_database.public.books SET mark = getCommonBookMark(_book_id)
  WHERE books.book_id = _book_id;

  RETURN NULL;
END
$$;


ALTER FUNCTION public.correctrealbookmark() OWNER TO postgres;

--
-- Name: getcommonauthormark(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcommonauthormark(integer) RETURNS integer
    LANGUAGE sql
    AS $_$
SELECT cast(round(cast(SUM(mark) AS DOUBLE PRECISION)/COUNT(user_id)) AS INTEGER)
FROM authors_users_votes
WHERE author_id = $1;
$_$;


ALTER FUNCTION public.getcommonauthormark(integer) OWNER TO postgres;

--
-- Name: getcommonbookmark(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcommonbookmark(integer) RETURNS integer
    LANGUAGE sql
    AS $_$
SELECT cast(round(cast(SUM(mark) AS DOUBLE PRECISION)/COUNT(user_id)) AS INTEGER)
FROM books_users_votes
WHERE book_id = $1;
$_$;


ALTER FUNCTION public.getcommonbookmark(integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: authors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authors (
    author_id integer NOT NULL,
    name character varying(50) NOT NULL,
    surname character varying(50) NOT NULL,
    birthday date NOT NULL,
    motherland integer NOT NULL,
    photo text NOT NULL,
    mark integer DEFAULT 0,
    description character varying(2000) NOT NULL,
    CONSTRAINT authors_birthday_min CHECK (((date_part('year'::text, birthday) >= ('-15000'::integer)::double precision) AND (birthday <= ('now'::text)::date))),
    CONSTRAINT authors_mark_range CHECK (((mark >= 0) AND (mark <= 10)))
);


ALTER TABLE public.authors OWNER TO postgres;

--
-- Name: getselectauthorbyname(text, text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getselectauthorbyname(text, text, integer) RETURNS SETOF public.authors
    LANGUAGE sql
    AS $_$
SELECT *
FROM authors
WHERE (surname ~* ('(' || $2 || '.*|' || $1 || '.*)')) OR (name ~* ('(' || $1 || '.*|' || $2 || '.*)'))
ORDER BY name DESC,surname DESC LIMIT 5 OFFSET $3*5 ;
$_$;


ALTER FUNCTION public.getselectauthorbyname(text, text, integer) OWNER TO postgres;

--
-- Name: books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books (
    book_id integer NOT NULL,
    name character varying(200) NOT NULL,
    description character varying(2000) NOT NULL,
    mark integer DEFAULT 0,
    photo text NOT NULL,
    year integer NOT NULL,
    CONSTRAINT movies_mark_range CHECK (((mark >= 0) AND (mark <= 10)))
);


ALTER TABLE public.books OWNER TO postgres;

--
-- Name: getselectbookbyname(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getselectbookbyname(text, integer) RETURNS SETOF public.books
    LANGUAGE sql
    AS $_$
SELECT *
FROM spring_database.public.books
WHERE name ~* ('(\A' || $1 || '.*)')
ORDER BY name DESC LIMIT 5 OFFSET $2*5 ;
$_$;


ALTER FUNCTION public.getselectbookbyname(text, integer) OWNER TO postgres;

--
-- Name: countries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.countries (
    country_id integer NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.countries OWNER TO postgres;

--
-- Name: getselectcountrybyname(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getselectcountrybyname(text) RETURNS SETOF public.countries
    LANGUAGE sql
    AS $_$
SELECT *
FROM countries
WHERE LOWER(name) SIMILAR TO (LOWER($1) || '%')
ORDER BY name DESC LIMIT 10 ;
$_$;


ALTER FUNCTION public.getselectcountrybyname(text) OWNER TO postgres;

--
-- Name: genres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genres (
    genre_id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.genres OWNER TO postgres;

--
-- Name: getselectgenrebyname(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getselectgenrebyname(text) RETURNS SETOF public.genres
    LANGUAGE sql
    AS $_$
SELECT *
FROM genres
WHERE LOWER(name) SIMILAR TO $1 || '%'
ORDER BY name DESC LIMIT 10 ;
$_$;


ALTER FUNCTION public.getselectgenrebyname(text) OWNER TO postgres;

--
-- Name: setrealauthormark(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.setrealauthormark() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  _author_id integer := NEW.author_id;
BEGIN

  UPDATE spring_database.public.authors SET mark = getCommonAuthorMark(_author_id)
  WHERE authors.author_id = _author_id;

  RETURN NULL;
END
$$;


ALTER FUNCTION public.setrealauthormark() OWNER TO postgres;

--
-- Name: setrealbookmark(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.setrealbookmark() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  _book_id integer := NEW.book_id;
BEGIN

  UPDATE spring_database.public.books SET mark = getcommonbookmark(_book_id)
  WHERE books.book_id = _book_id;

  RETURN NULL ;
END
$$;


ALTER FUNCTION public.setrealbookmark() OWNER TO postgres;

--
-- Name: acl_class; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.acl_class (
    id integer NOT NULL,
    class character varying(255) NOT NULL
);


ALTER TABLE public.acl_class OWNER TO postgres;

--
-- Name: acl_class_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.acl_class_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.acl_class_id_seq OWNER TO postgres;

--
-- Name: acl_class_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.acl_class_id_seq OWNED BY public.acl_class.id;


--
-- Name: acl_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.acl_entry (
    id integer NOT NULL,
    acl_object_identity integer NOT NULL,
    ace_order integer NOT NULL,
    sid integer NOT NULL,
    mask integer NOT NULL,
    granting smallint NOT NULL,
    audit_success smallint NOT NULL,
    audit_failure smallint NOT NULL
);


ALTER TABLE public.acl_entry OWNER TO postgres;

--
-- Name: acl_entry_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.acl_entry_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.acl_entry_id_seq OWNER TO postgres;

--
-- Name: acl_entry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.acl_entry_id_seq OWNED BY public.acl_entry.id;


--
-- Name: acl_object_identity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.acl_object_identity (
    id integer NOT NULL,
    object_id_class integer NOT NULL,
    object_id_identity integer NOT NULL,
    parent_object integer,
    owner_sid integer,
    entries_inheriting smallint NOT NULL
);


ALTER TABLE public.acl_object_identity OWNER TO postgres;

--
-- Name: acl_object_identity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.acl_object_identity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.acl_object_identity_id_seq OWNER TO postgres;

--
-- Name: acl_object_identity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.acl_object_identity_id_seq OWNED BY public.acl_object_identity.id;


--
-- Name: acl_sid; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.acl_sid (
    id integer NOT NULL,
    principal smallint NOT NULL,
    sid character varying(100) NOT NULL
);


ALTER TABLE public.acl_sid OWNER TO postgres;

--
-- Name: acl_sid_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.acl_sid_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.acl_sid_id_seq OWNER TO postgres;

--
-- Name: acl_sid_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.acl_sid_id_seq OWNED BY public.acl_sid.id;


--
-- Name: author_reviews; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author_reviews (
    review_id integer NOT NULL,
    title character varying(100) NOT NULL,
    content character varying(2000) NOT NULL,
    writer_id integer NOT NULL,
    author_id integer NOT NULL
);


ALTER TABLE public.author_reviews OWNER TO postgres;

--
-- Name: author_reviews_review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_reviews_review_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.author_reviews_review_id_seq OWNER TO postgres;

--
-- Name: author_reviews_review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_reviews_review_id_seq OWNED BY public.author_reviews.review_id;


--
-- Name: authors_author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.authors_author_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.authors_author_id_seq OWNER TO postgres;

--
-- Name: authors_author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.authors_author_id_seq OWNED BY public.authors.author_id;


--
-- Name: authors_users_votes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authors_users_votes (
    author_id integer NOT NULL,
    user_id integer NOT NULL,
    mark integer NOT NULL,
    CONSTRAINT authors_users_votes_bind_mark_range CHECK (((mark >= 0) AND (mark <= 10)))
);


ALTER TABLE public.authors_users_votes OWNER TO postgres;

--
-- Name: book_reviews; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_reviews (
    review_id integer NOT NULL,
    title character varying(100) NOT NULL,
    content character varying(2000) NOT NULL,
    writer_id integer NOT NULL,
    book_id integer NOT NULL
);


ALTER TABLE public.book_reviews OWNER TO postgres;

--
-- Name: book_reviews_review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_reviews_review_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_reviews_review_id_seq OWNER TO postgres;

--
-- Name: book_reviews_review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_reviews_review_id_seq OWNED BY public.book_reviews.review_id;


--
-- Name: books_authors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books_authors (
    author_id integer NOT NULL,
    book_id integer NOT NULL
);


ALTER TABLE public.books_authors OWNER TO postgres;

--
-- Name: books_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.books_book_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.books_book_id_seq OWNER TO postgres;

--
-- Name: books_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.books_book_id_seq OWNED BY public.books.book_id;


--
-- Name: books_countries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books_countries (
    country_id integer NOT NULL,
    book_id integer NOT NULL
);


ALTER TABLE public.books_countries OWNER TO postgres;

--
-- Name: books_genres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books_genres (
    genre_id integer NOT NULL,
    book_id integer NOT NULL
);


ALTER TABLE public.books_genres OWNER TO postgres;

--
-- Name: books_users_votes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.books_users_votes (
    book_id integer NOT NULL,
    user_id integer NOT NULL,
    mark integer NOT NULL,
    CONSTRAINT books_users_votes_bind_mark_range CHECK (((mark >= 0) AND (mark <= 10)))
);


ALTER TABLE public.books_users_votes OWNER TO postgres;

--
-- Name: countries_country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.countries_country_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.countries_country_id_seq OWNER TO postgres;

--
-- Name: countries_country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.countries_country_id_seq OWNED BY public.countries.country_id;


--
-- Name: genres_genre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.genres_genre_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genres_genre_id_seq OWNER TO postgres;

--
-- Name: genres_genre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.genres_genre_id_seq OWNED BY public.genres.genre_id;


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- Name: movies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movies (
    movie_id integer NOT NULL,
    budget integer,
    description character varying(255),
    mark integer,
    money integer,
    name character varying(255) NOT NULL,
    photo character varying(255),
    year date
);


ALTER TABLE public.movies OWNER TO postgres;

--
-- Name: movies_countries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movies_countries (
    country_id integer NOT NULL,
    movie_id integer NOT NULL
);


ALTER TABLE public.movies_countries OWNER TO postgres;

--
-- Name: movies_genres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movies_genres (
    genre_id integer NOT NULL,
    movie_id integer NOT NULL
);


ALTER TABLE public.movies_genres OWNER TO postgres;

--
-- Name: persistent_logins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.persistent_logins (
    username character varying(50) NOT NULL,
    series character varying(64) NOT NULL,
    token character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL
);


ALTER TABLE public.persistent_logins OWNER TO postgres;

--
-- Name: place_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.place_types (
    type_id integer NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.place_types OWNER TO postgres;

--
-- Name: place_types_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.place_types_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.place_types_type_id_seq OWNER TO postgres;

--
-- Name: place_types_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.place_types_type_id_seq OWNED BY public.place_types.type_id;


--
-- Name: places; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.places (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    longitude character varying(1000000) NOT NULL,
    lat character varying(1000000) NOT NULL,
    type_id integer
);


ALTER TABLE public.places OWNER TO postgres;

--
-- Name: places_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.places_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.places_id_seq OWNER TO postgres;

--
-- Name: places_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.places_id_seq OWNED BY public.places.id;


--
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_roles (
    id integer NOT NULL,
    authority character varying(30) NOT NULL
);


ALTER TABLE public.users_roles OWNER TO postgres;

--
-- Name: user_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_role_id_seq OWNER TO postgres;

--
-- Name: user_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_role_id_seq OWNED BY public.users_roles.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    username character varying(100) NOT NULL,
    country_id integer NOT NULL,
    gender character varying(50) NOT NULL,
    CONSTRAINT users_gender_range CHECK ((((gender)::text = 'MALE'::text) OR ((gender)::text = 'FEMALE'::text)))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_user_id_seq OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- Name: users_users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_users_roles (
    user_id integer NOT NULL,
    user_role_id integer NOT NULL
);


ALTER TABLE public.users_users_roles OWNER TO postgres;

--
-- Name: acl_class id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_class ALTER COLUMN id SET DEFAULT nextval('public.acl_class_id_seq'::regclass);


--
-- Name: acl_entry id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_entry ALTER COLUMN id SET DEFAULT nextval('public.acl_entry_id_seq'::regclass);


--
-- Name: acl_object_identity id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_object_identity ALTER COLUMN id SET DEFAULT nextval('public.acl_object_identity_id_seq'::regclass);


--
-- Name: acl_sid id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_sid ALTER COLUMN id SET DEFAULT nextval('public.acl_sid_id_seq'::regclass);


--
-- Name: author_reviews review_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_reviews ALTER COLUMN review_id SET DEFAULT nextval('public.author_reviews_review_id_seq'::regclass);


--
-- Name: authors author_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors ALTER COLUMN author_id SET DEFAULT nextval('public.authors_author_id_seq'::regclass);


--
-- Name: book_reviews review_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reviews ALTER COLUMN review_id SET DEFAULT nextval('public.book_reviews_review_id_seq'::regclass);


--
-- Name: books book_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books ALTER COLUMN book_id SET DEFAULT nextval('public.books_book_id_seq'::regclass);


--
-- Name: countries country_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries ALTER COLUMN country_id SET DEFAULT nextval('public.countries_country_id_seq'::regclass);


--
-- Name: genres genre_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genres ALTER COLUMN genre_id SET DEFAULT nextval('public.genres_genre_id_seq'::regclass);


--
-- Name: place_types type_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.place_types ALTER COLUMN type_id SET DEFAULT nextval('public.place_types_type_id_seq'::regclass);


--
-- Name: places id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places ALTER COLUMN id SET DEFAULT nextval('public.places_id_seq'::regclass);


--
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- Name: users_roles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles ALTER COLUMN id SET DEFAULT nextval('public.user_role_id_seq'::regclass);


--
-- Data for Name: acl_class; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.acl_class (id, class) FROM stdin;
1	database.entity.Author
\.


--
-- Data for Name: acl_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) FROM stdin;
1	1	1	1	1	1	1	1
2	2	1	1	2	1	1	1
3	3	1	2	1	1	1	1
4	1	2	2	2	1	1	1
\.


--
-- Data for Name: acl_object_identity; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) FROM stdin;
1	1	41	\N	2	0
2	1	42	\N	2	0
3	1	43	\N	2	0
\.


--
-- Data for Name: acl_sid; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.acl_sid (id, principal, sid) FROM stdin;
1	0	ROLE_USER
2	0	ROLE_ADMIN
\.


--
-- Data for Name: author_reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.author_reviews (review_id, title, content, writer_id, author_id) FROM stdin;
92	asdfadsfdas	fdsfdasasdfdsffdadf	50	83
125	asfdsf	dsfsdfds	50	83
\.


--
-- Data for Name: authors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.authors (author_id, name, surname, birthday, motherland, photo, mark, description) FROM stdin;
83	Марк 	Твен	1835-04-30	3	/images/authors/photo-8649276490733105322.jpeg	9	Марк Твен — американский писатель, журналист и общественный деятель. Его творчество охватывает множество жанров — юмор, сатиру, философскую фантастику, публицистику и другие, и во всех этих жанрах он неизменно занимает позицию гуманиста и демократа.
134	Жюль	Верн	1900-03-21	6	/images/authors/photo-7848729194126569391.jpeg	0	Жюль Габриэ́ль Верн — французский писатель, классик приключенческой литературы, один из основоположников жанра научной фантастики. Член Французского Географического общества.
135	Иван	Тургенев	1900-05-25	1	/images/authors/photo-2122410202897230436.jpeg	0	Ива́н Серге́евич Турге́нев — русский писатель-реалист, поэт, публицист, драматург, переводчик. Один из классиков русской литературы, внёсших наиболее значительный вклад в её развитие во второй половине XIX века. 
138	Михаил	Лермонтов	1800-03-03	1	/images/authors/photo-5256621482225061540.jpeg	0	Михаи́л Ю́рьевич Ле́рмонтов — русский поэт, прозаик, драматург, художник.
137	Николай	Гоголь	1800-03-03	1	/images/authors/photo-4171776577842551747.jpeg	4	Никола́й Васи́льевич Го́голь — русский прозаик, драматург, поэт, критик, публицист, признанный одним из классиков русской литературы. Происходил из старинного дворянского рода Гоголь-Яновских.
82	Александр	Дюма	1802-07-24	6	/images/authors/photo-7065042469375985553.jpeg	7	Алекса́ндр Дюма́, отец — французский писатель, драматург и журналист. Один из самых читаемых французских авторов. Работал во многих жанрах: пьесы, романы, статьи и книги о путешествиях. 
136	Александр	Пушкин	1825-03-03	1	/images/authors/photo-6479400501127375004.jpeg	9	Алекса́ндр Серге́евич Пу́шкин — русский поэт, драматург и прозаик, заложивший основы русского реалистического направления, критик и теоретик литературы, историк, публицист; один из самых авторитетных литературных деятелей первой трети XIX века.
\.


--
-- Data for Name: authors_users_votes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.authors_users_votes (author_id, user_id, mark) FROM stdin;
83	50	9
82	50	4
137	50	5
137	48	4
82	48	10
136	50	9
\.


--
-- Data for Name: book_reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_reviews (review_id, title, content, writer_id, book_id) FROM stdin;
91	Ruslan	SHit	50	85
101	fasdfsdf	dsfdsfsadssasfds	50	85
102	afdsdfdsfdsf	sdfsdfsadfdsfdsfdsfadd	50	85
105	afadsfds	ffsdffsdfdfaf	50	85
120	fdsfadfadsdsfdss	fsdfdafdfds	50	85
122	My recense	fasdfdsfds	50	85
124	asdfdsfdsfsdf	fdadsfdsfdsfasdds	50	85
147	asfdsff	sfadfsdsddsaf	50	142
148	Интересная книга	Хорошо написано!	50	142
150	Good	Great book!	50	144
\.


--
-- Data for Name: books; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.books (book_id, name, description, mark, photo, year) FROM stdin;
85	Приключения Тома Сойера	Приключе́ния То́ма Со́йера» — вышедшая в 1876 году повесть Марка Твена о приключениях мальчика, растущего в вымышленном небольшом американском городке в штате Миссури. Действие романа происходит до событий Гражданской войны в США.	8	/images/books/photo-8158915566540719247.jpeg	1876
139	Евгений Онегин	«Евге́ний Оне́гин» — роман в стихах русского поэта Александра Сергеевича Пушкина, написанный в 1823—1830 годах, одно из самых значительных произведений русской словесности.	0	/images/books/photo-8890569935140616174.jpeg	1801
140	Мертвые души	«Мёртвые ду́ши» — произведение Николая Васильевича Гоголя, жанр которого сам автор обозначил как поэму. Изначально задумано как трёхтомное произведение. Первый том был издан в 1842 году.	0	/images/books/photo-6730178447171789747.jpeg	1842
143	Три мушкетера	«Три мушкетёра» — историко-приключенческий роман Александра Дюма-отца, написанный в 1844 году	0	/images/books/photo-195221557215162184.jpeg	1844
142	Отцы и дети	«Отцы́ и де́ти» — роман русского писателя Ивана Сергеевича Тургенева, написанный в 60-е годы XIX века. Роман стал знаковым для своего времени, а образ главного героя Евгения Базарова был воспринят молодёжью как пример для подражания.	9	/images/books/photo-7454483586470967079.jpeg	1831
141	Таинственный остров	«Таинственный остров» — роман-робинзонада французского писателя Жюля Верна. Первая публикация — журнал Magasin d'illustration et de récréation, с 1 января 1874 по 15 декабря 1875 года.	8	/images/books/photo-6246411828354720724.jpeg	1900
144	Герой нашего времени	«Геро́й на́шего вре́мени» — первый в русской прозе лирико-психологический роман, написанный Михаилом Юрьевичем Лермонтовым. Классика русской литературы.	6	/images/books/photo-3353402643136806281.jpeg	1803
\.


--
-- Data for Name: books_authors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.books_authors (author_id, book_id) FROM stdin;
83	85
136	139
137	140
134	141
135	142
82	143
138	144
\.


--
-- Data for Name: books_countries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.books_countries (country_id, book_id) FROM stdin;
3	85
1	139
1	140
2	140
6	141
1	142
6	143
1	144
\.


--
-- Data for Name: books_genres; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.books_genres (genre_id, book_id) FROM stdin;
1	85
1	139
4	139
1	140
1	141
1	142
1	143
2	144
\.


--
-- Data for Name: books_users_votes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.books_users_votes (book_id, user_id, mark) FROM stdin;
85	50	8
142	50	9
141	50	8
144	50	6
\.


--
-- Data for Name: countries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.countries (country_id, name) FROM stdin;
1	Россия
2	Украина
3	США
4	Англия
5	Германия
6	Франция
7	Чехия
8	Польша
\.


--
-- Data for Name: genres; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.genres (genre_id, name) FROM stdin;
1	роман
2	повесть
3	рассказ
4	стихи
\.


--
-- Data for Name: movies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.movies (movie_id, budget, description, mark, money, name, photo, year) FROM stdin;
\.


--
-- Data for Name: movies_countries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.movies_countries (country_id, movie_id) FROM stdin;
\.


--
-- Data for Name: movies_genres; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.movies_genres (genre_id, movie_id) FROM stdin;
\.


--
-- Data for Name: persistent_logins; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.persistent_logins (username, series, token, last_used) FROM stdin;
\.


--
-- Data for Name: place_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.place_types (type_id, name) FROM stdin;
1	кафе
2	кинотеатр
3	цирк
4	музей
\.


--
-- Data for Name: places; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.places (id, name, longitude, lat, type_id) FROM stdin;
12	аывафывывафв	0.9	0.7	2
13	выафывываыв	0.5	0.6	4
14	sadfasdf	-0.4	0.5	2
15	sdfdsf	-0.4	0.4	3
17	Бугульма	52.8260805	54.5220314	2
19	Казань	49.06608060000001	55.8304307	1
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, email, password, username, country_id, gender) FROM stdin;
48	rustam@ma.ru	$2a$10$Rs5.Snr7Bd.2oJWBenLLI.QQNE3qmXuE2n5RstCZRdaf5zHByU00y	rustam	1	FEMALE
77	rustem@ma.ru	$2a$10$Dg9CLoHC2uVwO1TIyCT.9.WzfdKkNG0vsE2a9UOSk5SzUUF7DaHhu	rustem	1	MALE
49	ruskan@ma.ru	$2a$10$Nt9LcrxGFyUu8nsXlyv3ruqZknmEGibiwWuRfHNw.4hyw.GNZsIOu	ruskan	1	FEMALE
50	rustan@ma.ru	$2a$10$qvJKvUoJ0ed0ja6uPrNiYO2zKIXPLphNhvJkFzYgNyM1nLW.5nVEK	rustamik	2	MALE
155	arslan@ma.ru	$2a$10$FqpQzLj9cZppfWCFgXwi1uMm0ZMn39oGBYI0bv3fbCaBUd9wOpMFS	arslan	1	FEMALE
\.


--
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_roles (id, authority) FROM stdin;
1	ROLE_USER
2	ROLE_ADMIN
\.


--
-- Data for Name: users_users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_users_roles (user_id, user_role_id) FROM stdin;
50	1
50	2
49	1
48	1
77	1
\.


--
-- Name: acl_class_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.acl_class_id_seq', 1, false);


--
-- Name: acl_entry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.acl_entry_id_seq', 1, false);


--
-- Name: acl_object_identity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.acl_object_identity_id_seq', 1, false);


--
-- Name: acl_sid_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.acl_sid_id_seq', 1, false);


--
-- Name: author_reviews_review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.author_reviews_review_id_seq', 1, false);


--
-- Name: authors_author_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.authors_author_id_seq', 1, false);


--
-- Name: book_reviews_review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_reviews_review_id_seq', 1, false);


--
-- Name: books_book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.books_book_id_seq', 1, false);


--
-- Name: countries_country_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.countries_country_id_seq', 8, true);


--
-- Name: genres_genre_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.genres_genre_id_seq', 4, true);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 155, true);


--
-- Name: place_types_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.place_types_type_id_seq', 4, true);


--
-- Name: places_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.places_id_seq', 1, false);


--
-- Name: user_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_role_id_seq', 2, true);


--
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 6, true);


--
-- Name: acl_class acl_class_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_class
    ADD CONSTRAINT acl_class_pkey PRIMARY KEY (id);


--
-- Name: acl_entry acl_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_entry
    ADD CONSTRAINT acl_entry_pkey PRIMARY KEY (id);


--
-- Name: acl_object_identity acl_object_identity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_object_identity
    ADD CONSTRAINT acl_object_identity_pkey PRIMARY KEY (id);


--
-- Name: acl_sid acl_sid_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_sid
    ADD CONSTRAINT acl_sid_pkey PRIMARY KEY (id);


--
-- Name: author_reviews author_reviews_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_reviews
    ADD CONSTRAINT author_reviews_pk PRIMARY KEY (review_id);


--
-- Name: authors authors_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_pk PRIMARY KEY (author_id);


--
-- Name: authors_users_votes authors_users_votes_author_id_user_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors_users_votes
    ADD CONSTRAINT authors_users_votes_author_id_user_id_pk PRIMARY KEY (author_id, user_id);


--
-- Name: book_reviews book_reviews_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reviews
    ADD CONSTRAINT book_reviews_pk PRIMARY KEY (review_id);


--
-- Name: books_authors books_authors_bind__pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_authors
    ADD CONSTRAINT books_authors_bind__pk PRIMARY KEY (book_id, author_id);


--
-- Name: books_countries books_countries_bind__pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_countries
    ADD CONSTRAINT books_countries_bind__pk PRIMARY KEY (book_id, country_id);


--
-- Name: books_genres books_genres_bind__pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_genres
    ADD CONSTRAINT books_genres_bind__pk PRIMARY KEY (book_id, genre_id);


--
-- Name: books books_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pk PRIMARY KEY (book_id);


--
-- Name: books_users_votes books_users_votes_book_id_user_id_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_users_votes
    ADD CONSTRAINT books_users_votes_book_id_user_id_unique UNIQUE (book_id, user_id);


--
-- Name: countries countries_name_uindex; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries
    ADD CONSTRAINT countries_name_uindex UNIQUE (name);


--
-- Name: countries countries_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries
    ADD CONSTRAINT countries_pk PRIMARY KEY (country_id);


--
-- Name: genres genres_name_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genres
    ADD CONSTRAINT genres_name_unique UNIQUE (name);


--
-- Name: genres genres_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genres
    ADD CONSTRAINT genres_pk PRIMARY KEY (genre_id);


--
-- Name: movies movies_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movies
    ADD CONSTRAINT movies_pkey PRIMARY KEY (movie_id);


--
-- Name: persistent_logins persistent_logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.persistent_logins
    ADD CONSTRAINT persistent_logins_pkey PRIMARY KEY (series);


--
-- Name: place_types place_types_type_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.place_types
    ADD CONSTRAINT place_types_type_id_pk PRIMARY KEY (type_id);


--
-- Name: places places_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT places_pk PRIMARY KEY (id);


--
-- Name: users_roles role_name_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT role_name_unique UNIQUE (authority);


--
-- Name: users_roles role_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT role_pk PRIMARY KEY (id);


--
-- Name: acl_sid unique_uk_1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_sid
    ADD CONSTRAINT unique_uk_1 UNIQUE (sid, principal);


--
-- Name: acl_class unique_uk_2; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_class
    ADD CONSTRAINT unique_uk_2 UNIQUE (class);


--
-- Name: acl_object_identity unique_uk_3; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_object_identity
    ADD CONSTRAINT unique_uk_3 UNIQUE (object_id_class, object_id_identity);


--
-- Name: acl_entry unique_uk_4; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_entry
    ADD CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity, ace_order);


--
-- Name: users_users_roles user_user_role_bind__pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_users_roles
    ADD CONSTRAINT user_user_role_bind__pk PRIMARY KEY (user_id, user_role_id);


--
-- Name: users users_email_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_unique UNIQUE (email);


--
-- Name: users users_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pk PRIMARY KEY (user_id);


--
-- Name: authors_name_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX authors_name_index ON public.authors USING btree (name);


--
-- Name: authors_surname_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX authors_surname_index ON public.authors USING btree (surname);


--
-- Name: books_name_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX books_name_index ON public.books USING btree (name);


--
-- Name: users_email_password; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX users_email_password ON public.users USING btree (email, password);


--
-- Name: authors_users_votes checkauthormark; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER checkauthormark AFTER DELETE ON public.authors_users_votes FOR EACH ROW EXECUTE PROCEDURE public.correctrealauthormark();


--
-- Name: books_users_votes checkbookmark; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER checkbookmark AFTER DELETE ON public.books_users_votes FOR EACH ROW EXECUTE PROCEDURE public.correctrealbookmark();


--
-- Name: authors_users_votes setauthormark; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER setauthormark AFTER INSERT ON public.authors_users_votes FOR EACH ROW EXECUTE PROCEDURE public.setrealauthormark();


--
-- Name: books_users_votes setbookmark; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER setbookmark AFTER INSERT ON public.books_users_votes FOR EACH ROW EXECUTE PROCEDURE public.setrealbookmark();


--
-- Name: acl_entry acl_entry_acl_object_identity_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_entry
    ADD CONSTRAINT acl_entry_acl_object_identity_fkey FOREIGN KEY (acl_object_identity) REFERENCES public.acl_object_identity(id);


--
-- Name: acl_entry acl_entry_sid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_entry
    ADD CONSTRAINT acl_entry_sid_fkey FOREIGN KEY (sid) REFERENCES public.acl_sid(id);


--
-- Name: acl_object_identity acl_object_identity_object_id_class_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_object_identity
    ADD CONSTRAINT acl_object_identity_object_id_class_fkey FOREIGN KEY (object_id_class) REFERENCES public.acl_class(id);


--
-- Name: acl_object_identity acl_object_identity_owner_sid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_object_identity
    ADD CONSTRAINT acl_object_identity_owner_sid_fkey FOREIGN KEY (owner_sid) REFERENCES public.acl_sid(id);


--
-- Name: acl_object_identity acl_object_identity_parent_object_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acl_object_identity
    ADD CONSTRAINT acl_object_identity_parent_object_fkey FOREIGN KEY (parent_object) REFERENCES public.acl_object_identity(id);


--
-- Name: author_reviews author_reviews_author_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_reviews
    ADD CONSTRAINT author_reviews_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.authors(author_id) ON DELETE CASCADE;


--
-- Name: author_reviews author_reviews_writer_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_reviews
    ADD CONSTRAINT author_reviews_writer_id_fk FOREIGN KEY (writer_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- Name: authors authors_motherland_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_motherland_fkey FOREIGN KEY (motherland) REFERENCES public.countries(country_id);


--
-- Name: authors_users_votes authors_users_votes_bind_authors_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors_users_votes
    ADD CONSTRAINT authors_users_votes_bind_authors_id_fkey FOREIGN KEY (author_id) REFERENCES public.authors(author_id) ON DELETE CASCADE;


--
-- Name: authors_users_votes authors_users_votes_bind_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors_users_votes
    ADD CONSTRAINT authors_users_votes_bind_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- Name: book_reviews book_reviews_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reviews
    ADD CONSTRAINT book_reviews_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(book_id) ON DELETE CASCADE;


--
-- Name: book_reviews book_reviews_writer_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reviews
    ADD CONSTRAINT book_reviews_writer_id_fk FOREIGN KEY (writer_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- Name: books_authors books_authors_bind_author_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_authors
    ADD CONSTRAINT books_authors_bind_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.authors(author_id) ON DELETE CASCADE;


--
-- Name: books_authors books_authors_bind_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_authors
    ADD CONSTRAINT books_authors_bind_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(book_id) ON DELETE CASCADE;


--
-- Name: books_genres books_genres_bind_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_genres
    ADD CONSTRAINT books_genres_bind_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(book_id) ON DELETE CASCADE;


--
-- Name: books_genres books_genres_bind_genre_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_genres
    ADD CONSTRAINT books_genres_bind_genre_id_fkey FOREIGN KEY (genre_id) REFERENCES public.genres(genre_id) ON DELETE CASCADE;


--
-- Name: books_users_votes books_users_votes_bind_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_users_votes
    ADD CONSTRAINT books_users_votes_bind_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(book_id) ON DELETE CASCADE;


--
-- Name: books_users_votes books_users_votes_bind_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_users_votes
    ADD CONSTRAINT books_users_votes_bind_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- Name: books_authors fk1b933slgixbjdslgwu888m34v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_authors
    ADD CONSTRAINT fk1b933slgixbjdslgwu888m34v FOREIGN KEY (book_id) REFERENCES public.books(book_id);


--
-- Name: books_authors fk3qua08pjd1ca1fe2x5cgohuu5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_authors
    ADD CONSTRAINT fk3qua08pjd1ca1fe2x5cgohuu5 FOREIGN KEY (author_id) REFERENCES public.authors(author_id);


--
-- Name: books_countries fk3tqrgyo3p1wt3t0p1owmrvdmj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_countries
    ADD CONSTRAINT fk3tqrgyo3p1wt3t0p1owmrvdmj FOREIGN KEY (country_id) REFERENCES public.countries(country_id);


--
-- Name: users_users_roles fk6hxc81b0daankktq7v1ivi0bw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_users_roles
    ADD CONSTRAINT fk6hxc81b0daankktq7v1ivi0bw FOREIGN KEY (user_role_id) REFERENCES public.users_roles(id);


--
-- Name: author_reviews fk6skf7wh13d7hcw89yh4drf3rg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_reviews
    ADD CONSTRAINT fk6skf7wh13d7hcw89yh4drf3rg FOREIGN KEY (author_id) REFERENCES public.authors(author_id);


--
-- Name: movies_countries fk80eh77h1dsgtiwygexrplh0sg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movies_countries
    ADD CONSTRAINT fk80eh77h1dsgtiwygexrplh0sg FOREIGN KEY (country_id) REFERENCES public.movies(movie_id);


--
-- Name: places fkalyv2yoowrcepwyv3h67brxyb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT fkalyv2yoowrcepwyv3h67brxyb FOREIGN KEY (type_id) REFERENCES public.place_types(type_id);


--
-- Name: book_reviews fkbfcn9ci2161ihce2g2ccn2ke7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reviews
    ADD CONSTRAINT fkbfcn9ci2161ihce2g2ccn2ke7 FOREIGN KEY (writer_id) REFERENCES public.users(user_id);


--
-- Name: book_reviews fkcm2shivx19spbg0flc47mcrs1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_reviews
    ADD CONSTRAINT fkcm2shivx19spbg0flc47mcrs1 FOREIGN KEY (book_id) REFERENCES public.books(book_id);


--
-- Name: books_genres fkgkat05y2cec3tcpl6ur250sd0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_genres
    ADD CONSTRAINT fkgkat05y2cec3tcpl6ur250sd0 FOREIGN KEY (genre_id) REFERENCES public.genres(genre_id);


--
-- Name: users fkjlpks00ofkq3sqd9hqiavv5lg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkjlpks00ofkq3sqd9hqiavv5lg FOREIGN KEY (country_id) REFERENCES public.countries(country_id);


--
-- Name: books_genres fklv42b6uemg63q27om39jjbt9o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_genres
    ADD CONSTRAINT fklv42b6uemg63q27om39jjbt9o FOREIGN KEY (book_id) REFERENCES public.books(book_id);


--
-- Name: users_users_roles fkm73faupeogv09wl4udlqplxpx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_users_roles
    ADD CONSTRAINT fkm73faupeogv09wl4udlqplxpx FOREIGN KEY (user_role_id) REFERENCES public.users_roles(id);


--
-- Name: movies_genres fknvet2opvrc9njefurbtdqu1vd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movies_genres
    ADD CONSTRAINT fknvet2opvrc9njefurbtdqu1vd FOREIGN KEY (movie_id) REFERENCES public.genres(genre_id);


--
-- Name: author_reviews fknxcl0oyvc8bg9nqnstebn9r0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_reviews
    ADD CONSTRAINT fknxcl0oyvc8bg9nqnstebn9r0 FOREIGN KEY (writer_id) REFERENCES public.users(user_id);


--
-- Name: users_users_roles fkpft1nj5gnk0yr6ei39kvbq8yx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_users_roles
    ADD CONSTRAINT fkpft1nj5gnk0yr6ei39kvbq8yx FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: books_countries fkqlwld43ia8crimhkbxypb4kkt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_countries
    ADD CONSTRAINT fkqlwld43ia8crimhkbxypb4kkt FOREIGN KEY (book_id) REFERENCES public.books(book_id);


--
-- Name: movies_genres fksswcxh4idq8ih44hlk4x60kj2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movies_genres
    ADD CONSTRAINT fksswcxh4idq8ih44hlk4x60kj2 FOREIGN KEY (genre_id) REFERENCES public.movies(movie_id);


--
-- Name: authors fksx9cdnityy3tv9v9l6cfdxhil; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT fksx9cdnityy3tv9v9l6cfdxhil FOREIGN KEY (motherland) REFERENCES public.countries(country_id);


--
-- Name: movies_countries fkt27hblvnovffbqtm6aq2nec8n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movies_countries
    ADD CONSTRAINT fkt27hblvnovffbqtm6aq2nec8n FOREIGN KEY (movie_id) REFERENCES public.countries(country_id);


--
-- Name: books_countries movies_countries_bind_book_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_countries
    ADD CONSTRAINT movies_countries_bind_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(book_id) ON DELETE CASCADE;


--
-- Name: books_countries movies_countries_bind_country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books_countries
    ADD CONSTRAINT movies_countries_bind_country_id_fkey FOREIGN KEY (country_id) REFERENCES public.countries(country_id) ON DELETE CASCADE;


--
-- Name: places places___fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT places___fk FOREIGN KEY (type_id) REFERENCES public.place_types(type_id);


--
-- Name: users_users_roles user_user_role_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_users_roles
    ADD CONSTRAINT user_user_role_role_id_fkey FOREIGN KEY (user_role_id) REFERENCES public.users_roles(id) ON DELETE CASCADE;


--
-- Name: users_users_roles user_user_role_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_users_roles
    ADD CONSTRAINT user_user_role_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- Name: users users_country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_country_id_fkey FOREIGN KEY (country_id) REFERENCES public.countries(country_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

