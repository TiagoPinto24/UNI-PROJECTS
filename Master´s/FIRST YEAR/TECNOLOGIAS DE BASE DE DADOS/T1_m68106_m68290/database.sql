--
-- PostgreSQL database dump
--

\restrict gkK1g5IU7oj75eH1AcSY8gZRK7Srr5E7yJ2xayyyhBYVCU8PfeMlqAHJkiJAkt3

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

-- Started on 2025-12-22 16:47:07

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 886 (class 1247 OID 17043)
-- Name: estado; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.estado AS ENUM (
    'Pedido',
    'Em Execução',
    'Em Aprovação',
    'Aprovado'
);


ALTER TYPE public.estado OWNER TO postgres;

--
-- TOC entry 889 (class 1247 OID 17052)
-- Name: tipo_de_exame; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_de_exame AS ENUM (
    'Biológico',
    'Químico',
    'Balístico',
    'Documentais',
    'Digitais',
    'Médicos',
    'Vestígios'
);


ALTER TYPE public.tipo_de_exame OWNER TO postgres;

--
-- TOC entry 230 (class 1255 OID 17188)
-- Name: trg_laudo_immutavel_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trg_laudo_immutavel_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Se o laudo já estava aprovado, não permitir qualquer alteração
    IF OLD."Estado" = 'Aprovado' THEN
        RAISE EXCEPTION
            'O laudo está aprovado e não pode ser alterado.';
    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.trg_laudo_immutavel_func() OWNER TO postgres;

--
-- TOC entry 232 (class 1255 OID 17205)
-- Name: trg_prevent_delete_laudo_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trg_prevent_delete_laudo_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF OLD."Estado" = 'Aprovado' THEN
        RAISE EXCEPTION 'Não é permitida a eliminação de um Laudo aprovado (ID=%).', OLD."Identificador";
    END IF;
    RETURN OLD;
END;
$$;


ALTER FUNCTION public.trg_prevent_delete_laudo_func() OWNER TO postgres;

--
-- TOC entry 231 (class 1255 OID 17186)
-- Name: trg_validar_sobreposicao_cadeia_func(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trg_validar_sobreposicao_cadeia_func() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    last_para text;
    last_ts timestamp;
BEGIN
    IF EXISTS (
        SELECT 1 FROM public."CadeiaDeCustodia"
        WHERE "Evidência" = NEW."Evidência"
          AND "DataEvento" = NEW."DataEvento"
    ) THEN
        RAISE EXCEPTION 'Já existe um evento para esta evidência na mesma data/hora.';
    END IF;

    SELECT "Para", "DataEvento" INTO last_para, last_ts
    FROM public."CadeiaDeCustodia"
    WHERE "Evidência" = NEW."Evidência"
    ORDER BY "DataEvento" DESC
    LIMIT 1;

    IF last_para IS NOT NULL THEN
        IF NEW."De" IS DISTINCT FROM last_para THEN
            RAISE EXCEPTION 'Transferência inconsistente para Evidência %: campo "De" (%) difere do atual detentor (%) registado no último evento em %.',
                NEW."Evidência", NEW."De", last_para, last_ts;
        END IF;
    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.trg_validar_sobreposicao_cadeia_func() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16972)
-- Name: CadeiaDeCustodia; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."CadeiaDeCustodia" (
    "Evidência" character varying(50) NOT NULL,
    "DataEvento" timestamp(6) without time zone NOT NULL,
    "De" character varying(300) NOT NULL,
    "Para" character varying(300) NOT NULL,
    "LocalEvento" character varying(50) NOT NULL,
    "Observações" character varying(300) NOT NULL
);


ALTER TABLE public."CadeiaDeCustodia" OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16941)
-- Name: Caso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Caso" (
    "NúmeroDoCaso" integer NOT NULL,
    "DataAbertura" timestamp(6) without time zone NOT NULL,
    "Descrição" character varying(300) NOT NULL,
    "Estado" character varying(50) NOT NULL,
    "Tipologia" character varying(50) NOT NULL,
    "Local" integer NOT NULL
);


ALTER TABLE public."Caso" OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17084)
-- Name: EmAnexo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."EmAnexo" (
    "Ficheiro" integer NOT NULL,
    "Laudo" integer NOT NULL
);


ALTER TABLE public."EmAnexo" OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16985)
-- Name: Evidência; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Evidência" (
    "CódigoDeEvidência" integer NOT NULL,
    "Descrição" character varying(50) NOT NULL,
    "Tipo" character varying(50) NOT NULL,
    "Estado" character varying(50) NOT NULL,
    "Caso" integer NOT NULL,
    "Local" integer NOT NULL
);


ALTER TABLE public."Evidência" OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17101)
-- Name: Exame; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Exame" (
    "Identificador" integer NOT NULL,
    "Caso" integer,
    "Evidencia" integer NOT NULL,
    "TipoExame" public.tipo_de_exame NOT NULL,
    "PeritoResponsavel" character varying(50),
    "Estado" public.estado NOT NULL,
    "DataPedido" timestamp(6) without time zone NOT NULL,
    "DataConclusão" timestamp(6) without time zone
);


ALTER TABLE public."Exame" OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17075)
-- Name: FicheiroDigital; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."FicheiroDigital" (
    "Identificador" integer NOT NULL,
    "Exame" integer NOT NULL,
    "CaminhoURL" character varying(255) NOT NULL,
    "Hash" character varying(128) NOT NULL
);


ALTER TABLE public."FicheiroDigital" OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17067)
-- Name: Laudo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Laudo" (
    "Identificador" integer NOT NULL,
    "Sumário" character varying(300) NOT NULL,
    "Estado" public.estado NOT NULL,
    "Data" date NOT NULL,
    "Exame" integer NOT NULL
);


ALTER TABLE public."Laudo" OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17029)
-- Name: Local; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Local" (
    "Identificador" integer NOT NULL,
    "Designação" character varying(50) NOT NULL,
    "Localidade" character varying(50) NOT NULL,
    "Endereço" character varying(50),
    "País" character varying(50) NOT NULL
);


ALTER TABLE public."Local" OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17022)
-- Name: Localidade; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Localidade" (
    "Localidade" character varying(50) NOT NULL,
    "País" character varying(50) NOT NULL
);


ALTER TABLE public."Localidade" OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16960)
-- Name: PapelEmCaso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."PapelEmCaso" (
    "Pessoa" character varying(50) NOT NULL,
    "Papel" character varying(50) NOT NULL,
    "DataInicio" timestamp(6) without time zone,
    "DataFim" timestamp(6) without time zone,
    "Caso" integer NOT NULL,
    CONSTRAINT "PapelEmCaso_papel_chk" CHECK ((("Papel")::text = ANY ((ARRAY['denunciante'::character varying, 'vítima'::character varying, 'testemunha'::character varying, 'suspeito'::character varying, 'perito'::character varying, 'agente'::character varying])::text[])))
);


ALTER TABLE public."PapelEmCaso" OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16951)
-- Name: Pessoa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Pessoa" (
    "CartãoDeCidadão" character varying(50) CONSTRAINT "Pessoa_PessoaID_not_null" NOT NULL,
    "NomeCompleto" character varying(50) NOT NULL,
    "DataNascimento" date,
    "Observações" character varying(300) NOT NULL
);


ALTER TABLE public."Pessoa" OWNER TO postgres;

--
-- TOC entry 5092 (class 0 OID 16972)
-- Dependencies: 222
-- Data for Name: CadeiaDeCustodia; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."CadeiaDeCustodia" ("Evidência", "DataEvento", "De", "Para", "LocalEvento", "Observações") FROM stdin;
101	2025-12-22 15:09:29.396537	Agente João Pereira	Perita Ana Silva	Cena do Crime	Recolha inicial e entrega para análise
50	2025-12-01 10:00:00	Cena do Crime	Inspetor João	Rua	Recolha
\.


--
-- TOC entry 5089 (class 0 OID 16941)
-- Dependencies: 219
-- Data for Name: Caso; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Caso" ("NúmeroDoCaso", "DataAbertura", "Descrição", "Estado", "Tipologia", "Local") FROM stdin;
2025001	2025-12-22 15:09:29.396537	Investigação de furto qualificado	Aberto	Crime contra o património	1
2025002	2025-12-22 10:00:00	Ataque de Ransomware a Instituição Pública	Em Investigação	Cibercrime	2
1001	2025-10-01 09:00:00	Assalto ao Banco X	Em Investigação	Roubo	1
1002	2025-11-15 14:00:00	Fraude Digital Y	Aberto	Cibercrime	2
\.


--
-- TOC entry 5098 (class 0 OID 17084)
-- Dependencies: 228
-- Data for Name: EmAnexo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."EmAnexo" ("Ficheiro", "Laudo") FROM stdin;
1	90
\.


--
-- TOC entry 5093 (class 0 OID 16985)
-- Dependencies: 223
-- Data for Name: Evidência; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Evidência" ("CódigoDeEvidência", "Descrição", "Tipo", "Estado", "Caso", "Local") FROM stdin;
101	Invólucro de munição 9mm	Balístico	Apreendido	2025001	1
102	Disco Rígido Externo 2TB	Digital	Em Análise	2025002	2
50	Munição 9mm	Balístico	Apreendido	1001	1
51	Smartphone Encriptado	Digitais	Em Análise	1002	2
52	Evidência Órfã	Vestígios	Guardado	1001	1
\.


--
-- TOC entry 5099 (class 0 OID 17101)
-- Dependencies: 229
-- Data for Name: Exame; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Exame" ("Identificador", "Caso", "Evidencia", "TipoExame", "PeritoResponsavel", "Estado", "DataPedido", "DataConclusão") FROM stdin;
500	2025001	101	Balístico	12345678	Pedido	2025-12-22 15:09:29.396537	\N
501	2025002	102	Digitais	55667788	Em Execução	2025-12-22 11:00:00	\N
201	1001	50	Balístico	PERITO02	Aprovado	2025-10-05 00:00:00	2025-10-10 00:00:00
202	1002	51	Digitais	PERITO01	Em Execução	2025-10-20 00:00:00	\N
\.


--
-- TOC entry 5097 (class 0 OID 17075)
-- Dependencies: 227
-- Data for Name: FicheiroDigital; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."FicheiroDigital" ("Identificador", "Exame", "CaminhoURL", "Hash") FROM stdin;
1	501	/storage/evidence/disk_dump_001.raw	sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
\.


--
-- TOC entry 5096 (class 0 OID 17067)
-- Dependencies: 226
-- Data for Name: Laudo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Laudo" ("Identificador", "Sumário", "Estado", "Data", "Exame") FROM stdin;
900	Análise preliminar de impacto	Em Execução	2025-12-22	500
90	Relatório de Balística Finalizado	Aprovado	2025-10-10	201
\.


--
-- TOC entry 5095 (class 0 OID 17029)
-- Dependencies: 225
-- Data for Name: Local; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Local" ("Identificador", "Designação", "Localidade", "Endereço", "País") FROM stdin;
1	Laboratório Central da PJ	Lisboa	Rua Gomes Freire	Portugal
2	Departamento de Investigação Criminal	Porto	Rua de Camões	Portugal
\.


--
-- TOC entry 5094 (class 0 OID 17022)
-- Dependencies: 224
-- Data for Name: Localidade; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Localidade" ("Localidade", "País") FROM stdin;
Lisboa	Portugal
Porto	Portugal
Coimbra	Portugal
\.


--
-- TOC entry 5091 (class 0 OID 16960)
-- Dependencies: 221
-- Data for Name: PapelEmCaso; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."PapelEmCaso" ("Pessoa", "Papel", "DataInicio", "DataFim", "Caso") FROM stdin;
11223344	suspeito	2025-12-22 10:30:00	\N	2025002
SUSP001	suspeito	2025-10-02 00:00:00	\N	1001
SUSP001	suspeito	2025-11-16 00:00:00	\N	1002
\.


--
-- TOC entry 5090 (class 0 OID 16951)
-- Dependencies: 220
-- Data for Name: Pessoa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Pessoa" ("CartãoDeCidadão", "NomeCompleto", "DataNascimento", "Observações") FROM stdin;
12345678	Ana Silva	1985-05-20	Perita Especialista em Balística
87654321	João Pereira	1980-10-10	Agente de Investigação
11223344	Carlos Antunes	1992-03-15	Suspeito principal no caso 2025002
55667788	Marta Ramos	1988-07-22	Perita em Forense Digital
PERITO01	Dra. Helena Costa	1980-01-01	Especialista em Digital
PERITO02	Dr. Ricardo Jorge	1975-05-12	Especialista em Balística
SUSP001	Bruno Vala	1990-03-20	Indivíduo recorrente
AGENT01	Inspetor João Gouveia	1982-11-30	Agente de campo
\.


--
-- TOC entry 4912 (class 2606 OID 17157)
-- Name: CadeiaDeCustodia CadeiaDeCustodia_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."CadeiaDeCustodia"
    ADD CONSTRAINT "CadeiaDeCustodia_pkey" PRIMARY KEY ("Evidência", "DataEvento");


--
-- TOC entry 4906 (class 2606 OID 16950)
-- Name: Caso Caso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Caso"
    ADD CONSTRAINT "Caso_pkey" PRIMARY KEY ("NúmeroDoCaso");


--
-- TOC entry 4924 (class 2606 OID 17090)
-- Name: EmAnexo EmAnexo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."EmAnexo"
    ADD CONSTRAINT "EmAnexo_pkey" PRIMARY KEY ("Ficheiro", "Laudo");


--
-- TOC entry 4914 (class 2606 OID 16995)
-- Name: Evidência Evidência_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Evidência"
    ADD CONSTRAINT "Evidência_pkey" PRIMARY KEY ("CódigoDeEvidência");


--
-- TOC entry 4926 (class 2606 OID 17113)
-- Name: Exame Exame_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Exame"
    ADD CONSTRAINT "Exame_pkey" PRIMARY KEY ("Identificador");


--
-- TOC entry 4922 (class 2606 OID 17083)
-- Name: FicheiroDigital FicheiroDigital_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."FicheiroDigital"
    ADD CONSTRAINT "FicheiroDigital_pkey" PRIMARY KEY ("Identificador");


--
-- TOC entry 4920 (class 2606 OID 17074)
-- Name: Laudo Laudo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Laudo"
    ADD CONSTRAINT "Laudo_pkey" PRIMARY KEY ("Identificador");


--
-- TOC entry 4918 (class 2606 OID 17036)
-- Name: Local Local_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Local"
    ADD CONSTRAINT "Local_pkey" PRIMARY KEY ("Identificador");


--
-- TOC entry 4916 (class 2606 OID 17028)
-- Name: Localidade Localidade_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Localidade"
    ADD CONSTRAINT "Localidade_pkey" PRIMARY KEY ("Localidade");


--
-- TOC entry 4910 (class 2606 OID 17011)
-- Name: PapelEmCaso PapelEmCaso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PapelEmCaso"
    ADD CONSTRAINT "PapelEmCaso_pkey" PRIMARY KEY ("Caso", "Pessoa", "Papel");


--
-- TOC entry 4908 (class 2606 OID 16997)
-- Name: Pessoa Pessoa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Pessoa"
    ADD CONSTRAINT "Pessoa_pkey" PRIMARY KEY ("CartãoDeCidadão");


--
-- TOC entry 4940 (class 2620 OID 17189)
-- Name: Laudo trg_laudo_immutavel; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_laudo_immutavel BEFORE UPDATE ON public."Laudo" FOR EACH ROW EXECUTE FUNCTION public.trg_laudo_immutavel_func();


--
-- TOC entry 4941 (class 2620 OID 17206)
-- Name: Laudo trg_prevent_delete_laudo; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_prevent_delete_laudo BEFORE DELETE ON public."Laudo" FOR EACH ROW EXECUTE FUNCTION public.trg_prevent_delete_laudo_func();


--
-- TOC entry 4939 (class 2620 OID 17187)
-- Name: CadeiaDeCustodia trg_validar_sobreposicao_cadeia; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_validar_sobreposicao_cadeia BEFORE INSERT ON public."CadeiaDeCustodia" FOR EACH ROW EXECUTE FUNCTION public.trg_validar_sobreposicao_cadeia_func();


--
-- TOC entry 4930 (class 2606 OID 17004)
-- Name: Evidência Caso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Evidência"
    ADD CONSTRAINT "Caso_fkey" FOREIGN KEY ("Caso") REFERENCES public."Caso"("NúmeroDoCaso");


--
-- TOC entry 4928 (class 2606 OID 17012)
-- Name: PapelEmCaso Caso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PapelEmCaso"
    ADD CONSTRAINT "Caso_fkey" FOREIGN KEY ("Caso") REFERENCES public."Caso"("NúmeroDoCaso");


--
-- TOC entry 4936 (class 2606 OID 17114)
-- Name: Exame Caso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Exame"
    ADD CONSTRAINT "Caso_fkey" FOREIGN KEY ("Caso") REFERENCES public."Caso"("NúmeroDoCaso");


--
-- TOC entry 4937 (class 2606 OID 17119)
-- Name: Exame Evidencia_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Exame"
    ADD CONSTRAINT "Evidencia_fkey" FOREIGN KEY ("Evidencia") REFERENCES public."Evidência"("CódigoDeEvidência");


--
-- TOC entry 4933 (class 2606 OID 17146)
-- Name: Laudo Exame_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Laudo"
    ADD CONSTRAINT "Exame_fkey" FOREIGN KEY ("Exame") REFERENCES public."Exame"("Identificador");


--
-- TOC entry 4934 (class 2606 OID 17091)
-- Name: EmAnexo Ficheiro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."EmAnexo"
    ADD CONSTRAINT "Ficheiro_fkey" FOREIGN KEY ("Ficheiro") REFERENCES public."FicheiroDigital"("Identificador");


--
-- TOC entry 4935 (class 2606 OID 17096)
-- Name: EmAnexo Laudo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."EmAnexo"
    ADD CONSTRAINT "Laudo_fkey" FOREIGN KEY ("Laudo") REFERENCES public."Laudo"("Identificador");


--
-- TOC entry 4931 (class 2606 OID 17192)
-- Name: Evidência Local_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Evidência"
    ADD CONSTRAINT "Local_fkey" FOREIGN KEY ("Local") REFERENCES public."Local"("Identificador");


--
-- TOC entry 4927 (class 2606 OID 17199)
-- Name: Caso Local_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Caso"
    ADD CONSTRAINT "Local_fkey" FOREIGN KEY ("Local") REFERENCES public."Local"("Identificador") NOT VALID;


--
-- TOC entry 4932 (class 2606 OID 17037)
-- Name: Local Localidade_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Local"
    ADD CONSTRAINT "Localidade_fkey" FOREIGN KEY ("Localidade") REFERENCES public."Localidade"("Localidade");


--
-- TOC entry 4938 (class 2606 OID 17124)
-- Name: Exame Perito_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Exame"
    ADD CONSTRAINT "Perito_fkey" FOREIGN KEY ("PeritoResponsavel") REFERENCES public."Pessoa"("CartãoDeCidadão");


--
-- TOC entry 4929 (class 2606 OID 17017)
-- Name: PapelEmCaso Pessoa_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PapelEmCaso"
    ADD CONSTRAINT "Pessoa_fkey" FOREIGN KEY ("Pessoa") REFERENCES public."Pessoa"("CartãoDeCidadão");


-- Completed on 2025-12-22 16:47:08

--
-- PostgreSQL database dump complete
--

\unrestrict gkK1g5IU7oj75eH1AcSY8gZRK7Srr5E7yJ2xayyyhBYVCU8PfeMlqAHJkiJAkt3

