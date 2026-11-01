-- Loyalty Platform - Sprint 4 - Consultas SQL
-- Turma 1TDSPW
-- Andrei Oliveira de Sousa (RM 573373), Felipe Coelho da Fonseca (RM 572133),
-- Henrique Soares Pereira (RM 569898), Luiz Felipe Lourenço da Silva (RM 572576)

SET DEFINE OFF;

-- Consulta 1 - Ranking de usuários da empresa
SELECT r.posicao_ranking                              AS posicao,
       u.nome_usuario                                 AS usuario,
       n.nome_nivel                                   AS nivel,
       r.pontuacao_ranking                            AS pontuacao,
       TO_CHAR(r.data_atualizacao_ranking, 'DD/MM/YYYY') AS atualizado_em
  FROM TB_RANKING r
  JOIN TB_USUARIO u
    ON u.id_usuario = r.TB_USUARIO_id_usuario
  JOIN TB_EMPRESA_USUARIO eu
    ON eu.TB_USUARIO_id_usuario = r.TB_USUARIO_id_usuario
   AND eu.TB_EMPRESA_id_empresa = r.TB_EMPRESA_id_empresa
  JOIN TB_NIVEL n
    ON n.id_nivel = eu.TB_NIVEL_id_nivel
 WHERE r.TB_EMPRESA_id_empresa = 1
 ORDER BY r.posicao_ranking;

-- Consulta 2 - Nível do usuário em cada empresa e pontos para o próximo nível
SELECT e.nome_empresa                                 AS empresa,
       n.nome_nivel                                   AS nivel_atual,
       eu.pontuacao_acumulada                         AS pontos,
       NVL(prox.nome_nivel, '(nível máximo)')         AS proximo_nivel,
       CASE
         WHEN prox.id_nivel IS NULL THEN 0
         ELSE prox.pontos_minimos_nivel - eu.pontuacao_acumulada
       END                                            AS pontos_faltantes,
       CASE
         WHEN prox.id_nivel IS NULL THEN 100
         ELSE ROUND( (eu.pontuacao_acumulada - n.pontos_minimos_nivel) * 100
                     / (prox.pontos_minimos_nivel - n.pontos_minimos_nivel), 1)
       END                                            AS progresso_pct
  FROM TB_EMPRESA_USUARIO eu
  JOIN TB_EMPRESA e
    ON e.id_empresa = eu.TB_EMPRESA_id_empresa
  JOIN TB_NIVEL n
    ON n.id_nivel = eu.TB_NIVEL_id_nivel
  LEFT JOIN TB_NIVEL prox
    ON prox.pontos_minimos_nivel = n.pontos_maximos_nivel + 1
 WHERE eu.TB_USUARIO_id_usuario = 6
 ORDER BY eu.pontuacao_acumulada DESC;

-- Consulta 3 - Missões disponíveis e progresso do usuário
SELECT c.nome_campanha                                AS campanha,
       m.nome_missao                                  AS missao,
       m.pontos_recompensa_missao                     AS pontos,
       NVL(um.progresso_atual_missao, 0) || ' / ' || m.meta_missao AS progresso,
       CASE
         WHEN um.data_resgate IS NOT NULL         THEN 'CONCLUÍDA E RESGATADA'
         WHEN um.TB_USUARIO_id_usuario IS NOT NULL THEN 'EM ANDAMENTO'
         ELSE 'NÃO INICIADA'
       END                                            AS situacao
  FROM TB_MISSAO m
  JOIN TB_CAMPANHA c
    ON c.id_campanha = m.TB_CAMPANHA_id_campanha
  JOIN TB_EMPRESA e
    ON e.id_empresa = c.TB_EMPRESA_id_empresa
  LEFT JOIN TB_USUARIO_MISSAO um
    ON um.TB_MISSAO_id_missao   = m.id_missao
   AND um.TB_USUARIO_id_usuario = 3
 WHERE e.id_empresa      = 1
   AND e.status_empresa  = 'ATIVA'
   AND c.status_campanha = 'ATIVA'
   AND m.status_missao   = 'ATIVA'
 ORDER BY m.id_missao;

-- Consulta 4 - Métricas por campanha (GROUP BY)
SELECT e.nome_empresa                                 AS empresa,
       c.nome_campanha                                AS campanha,
       c.status_campanha                              AS status,
       COUNT(DISTINCT m.id_missao)                    AS missoes,
       COUNT(DISTINCT um.TB_USUARIO_id_usuario)       AS participantes,
       COUNT(um.TB_MISSAO_id_missao)                  AS missoes_iniciadas,
       COUNT(um.data_conclusao)                       AS missoes_concluidas,
       NVL(ROUND(COUNT(um.data_conclusao) * 100
                 / NULLIF(COUNT(um.TB_MISSAO_id_missao), 0), 1), 0) AS taxa_conclusao_pct,
       NVL(SUM(CASE WHEN um.data_resgate IS NOT NULL
                    THEN m.pontos_recompensa_missao END), 0)        AS pontos_distribuidos
  FROM TB_CAMPANHA c
  JOIN TB_EMPRESA e
    ON e.id_empresa = c.TB_EMPRESA_id_empresa
  LEFT JOIN TB_MISSAO m
    ON m.TB_CAMPANHA_id_campanha = c.id_campanha
   AND m.status_missao <> 'EXCLUIDA'
  LEFT JOIN TB_USUARIO_MISSAO um
    ON um.TB_MISSAO_id_missao = m.id_missao
 GROUP BY e.nome_empresa, c.id_campanha, c.nome_campanha, c.status_campanha
 ORDER BY pontos_distribuidos DESC;

-- Consulta 5 - Usuários com sequência de dias em risco
SELECT u.nome_usuario                                 AS usuario,
       u.email_usuario                                AS email,
       e.nome_empresa                                 AS empresa,
       s.dias_consecutivos                            AS dias_em_risco,
       TO_CHAR(s.ultimo_acesso, 'DD/MM/YYYY HH24:MI') AS ultimo_acesso
  FROM TB_SEQUENCIA s
  JOIN TB_USUARIO u
    ON u.id_usuario = s.TB_USUARIO_id_usuario
  JOIN TB_EMPRESA e
    ON e.id_empresa = s.TB_EMPRESA_id_empresa
 WHERE s.status_sequencia = 'SUSPENSA'
   AND e.status_empresa   = 'ATIVA'
 ORDER BY s.dias_consecutivos DESC;

-- Consulta 6 - Engajamento por empresa (GROUP BY + agregações)
SELECT e.nome_empresa                                 AS empresa,
       COUNT(*)                                       AS usuarios_vinculados,
       SUM(eu.pontuacao_acumulada)                    AS pontos_totais,
       ROUND(AVG(eu.pontuacao_acumulada), 1)          AS media_pontos,
       MAX(eu.pontuacao_acumulada)                    AS maior_pontuacao,
       COUNT(CASE WHEN s.status_sequencia = 'ATIVA' THEN 1 END) AS sequencias_ativas,
       ROUND(COUNT(CASE WHEN s.status_sequencia = 'ATIVA' THEN 1 END) * 100
             / COUNT(*), 1)                           AS usuarios_ativos_pct
  FROM TB_EMPRESA e
  JOIN TB_EMPRESA_USUARIO eu
    ON eu.TB_EMPRESA_id_empresa = e.id_empresa
  LEFT JOIN TB_SEQUENCIA s
    ON s.TB_EMPRESA_id_empresa = eu.TB_EMPRESA_id_empresa
   AND s.TB_USUARIO_id_usuario = eu.TB_USUARIO_id_usuario
 WHERE e.status_empresa = 'ATIVA'
 GROUP BY e.id_empresa, e.nome_empresa
 ORDER BY media_pontos DESC;

-- Consulta 7 - Histórico de pontos do usuário
SELECT e.nome_empresa                                 AS empresa,
       TO_CHAR(um.data_resgate, 'DD/MM/YYYY')         AS data_resgate,
       c.nome_campanha                                AS campanha,
       m.nome_missao                                  AS missao,
       m.pontos_recompensa_missao                     AS pontos_ganhos,
       SUM(m.pontos_recompensa_missao)
         OVER (PARTITION BY e.id_empresa
               ORDER BY um.data_resgate)              AS saldo_acumulado
  FROM TB_USUARIO_MISSAO um
  JOIN TB_MISSAO m
    ON m.id_missao = um.TB_MISSAO_id_missao
  JOIN TB_CAMPANHA c
    ON c.id_campanha = m.TB_CAMPANHA_id_campanha
  JOIN TB_EMPRESA e
    ON e.id_empresa = c.TB_EMPRESA_id_empresa
 WHERE um.TB_USUARIO_id_usuario = 1
   AND um.data_resgate IS NOT NULL
 ORDER BY e.nome_empresa, um.data_resgate;
