-- =============================================================================
-- FICHEIRO DE QUERIES -- Tiago Pinto m68106 -- João Lóios m68290
-- =============================================================================

-- 1. RELATÓRIO DE CARGA DE TRABALHO POR PERITO

SELECT 
    p."NomeCompleto",
    e."Estado",
    COUNT(*) AS "Total_Exames"
FROM public."Pessoa" p
JOIN public."Exame" e ON p."CartãoDeCidadão" = e."PeritoResponsavel"
GROUP BY p."NomeCompleto", e."Estado"
ORDER BY "Total_Exames" DESC;

-- 2. PESQUISA DE EVIDÊNCIAS SEM CADEIA DE CUSTÓDIA

    SELECT 
        e."CódigoDeEvidência", 
        e."Descrição", 
        e."Caso"
    FROM public."Evidência" e
    LEFT JOIN public."CadeiaDeCustodia" c ON e."CódigoDeEvidência"::text = c."Evidência"
    WHERE c."Evidência" IS NULL;

-- 3. LISTAGEM DE FICHEIROS DIGITAIS ANEXADOS A LAUDOS

SELECT 
    l."Identificador" AS "ID_Laudo",
    fd."CaminhoURL",
    fd."Hash",
    l."Sumário"
FROM public."Laudo" l
JOIN public."EmAnexo" ea ON l."Identificador" = ea."Laudo"
JOIN public."FicheiroDigital" fd ON ea."Ficheiro" = fd."Identificador";

-- 4. TEMPO MÉDIO DE EXECUÇÃO DE EXAMES POR TIPO

SELECT 
    "TipoExame",
    AVG("DataConclusão" - "DataPedido") AS "Tempo_Medio_Processamento"
FROM public."Exame"
WHERE "DataConclusão" IS NOT NULL
GROUP BY "TipoExame";

-- 5. HISTÓRICO DE ENVOLVIMENTO DE UMA PESSOA EM CASOS

SELECT 
    p."NomeCompleto",
    pc."Papel",
    c."NúmeroDoCaso",
    c."Descrição" AS "Resumo_Caso",
    c."Estado" AS "Estado_Caso"
FROM public."Pessoa" p
JOIN public."PapelEmCaso" pc ON p."CartãoDeCidadão" = pc."Pessoa"
JOIN public."Caso" c ON pc."Caso" = c."NúmeroDoCaso"
ORDER BY c."DataAbertura" DESC;

-- 6. AUDITORIA DE DISCREPÂNCIA DE LOCALIZAÇÃO

SELECT 
    e."CódigoDeEvidência", 
    e."Descrição", 
    e."Local" AS "Local_No_Sistema",
    c."LocalEvento" AS "Ultimo_Local_Cadeia"
FROM public."Evidência" e
JOIN (
    SELECT DISTINCT ON ("Evidência") "Evidência", "LocalEvento"
    FROM public."CadeiaDeCustodia"
    ORDER BY "Evidência", "DataEvento" DESC
) c ON e."CódigoDeEvidência"::text = c."Evidência"
WHERE e."Local"::text != c."LocalEvento";

-- 7. MONITORIZAÇÃO DE SLA (PRAZOS)

SELECT 
    "Identificador", 
    "TipoExame", 
    "PeritoResponsavel", 
    "DataPedido",
    CURRENT_DATE - "DataPedido"::date AS "Dias_Em_Aberto"
FROM public."Exame"
WHERE "DataConclusão" IS NULL 
  AND "Estado" != 'Aprovado'
  AND ("DataPedido" < CURRENT_DATE - INTERVAL '30 days');

-- 8. MAPA DE CUSTÓDIA POR LOCALIDADE

SELECT 
    l."Localidade", 
    COUNT(DISTINCT e."CódigoDeEvidência") AS "Total_Evidencias"
FROM public."Local" l
JOIN public."Evidência" e ON l."Identificador" = e."Local"
GROUP BY l."Localidade"
ORDER BY "Total_Evidencias" DESC;

-- 9. INTEGRIDADE DE EVIDÊNCIAS DIGITAIS

SELECT 
    ex."Identificador" AS "ID_Exame", 
    ex."PeritoResponsavel", 
    ev."Descrição" AS "Evidencia_Digital"
FROM public."Exame" ex
JOIN public."Evidência" ev ON ex."Evidencia" = ev."CódigoDeEvidência"
LEFT JOIN public."FicheiroDigital" fd ON ex."Identificador" = fd."Exame"
WHERE ex."TipoExame" = 'Digitais' 
  AND fd."Hash" IS NULL;

-- 10. ANÁLISE DE RECORRÊNCIA DE SUSPEITOS

SELECT 
    p."NomeCompleto", 
    COUNT(pc."Caso") AS "Numero_De_Casos"
FROM public."Pessoa" p
JOIN public."PapelEmCaso" pc ON p."CartãoDeCidadão" = pc."Pessoa"
WHERE pc."Papel" = 'suspeito'
GROUP BY p."NomeCompleto"
HAVING COUNT(pc."Caso") > 1;