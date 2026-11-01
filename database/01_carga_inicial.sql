-- Loyalty Platform - Sprint 4 - Carga inicial de dados
-- Turma 1TDSPW
-- Andrei Oliveira de Sousa (RM 573373), Felipe Coelho da Fonseca (RM 572133),
-- Henrique Soares Pereira (RM 569898), Luiz Felipe Lourenço da Silva (RM 572576)

-- Executar depois do script DDL da Sprint 3

SET DEFINE OFF;
ALTER SESSION SET NLS_DATE_FORMAT = 'DD/MM/YYYY HH24:MI';

-- TB_NIVEL
INSERT INTO TB_NIVEL (id_nivel, nome_nivel, pontos_minimos_nivel, pontos_maximos_nivel) VALUES (1, 'Bronze',      0,   199);
INSERT INTO TB_NIVEL (id_nivel, nome_nivel, pontos_minimos_nivel, pontos_maximos_nivel) VALUES (2, 'Prata',     200,   499);
INSERT INTO TB_NIVEL (id_nivel, nome_nivel, pontos_minimos_nivel, pontos_maximos_nivel) VALUES (3, 'Ouro',      500,   899);
INSERT INTO TB_NIVEL (id_nivel, nome_nivel, pontos_minimos_nivel, pontos_maximos_nivel) VALUES (4, 'Platina',   900,  1399);
INSERT INTO TB_NIVEL (id_nivel, nome_nivel, pontos_minimos_nivel, pontos_maximos_nivel) VALUES (5, 'Diamante', 1400, 99999);

-- TB_EMPRESA
INSERT INTO TB_EMPRESA (id_empresa, nome_empresa, cnpj_empresa, email_empresa, telefone_empresa, data_cadastro_empresa, status_empresa)
VALUES (1, 'SoulUp Educação',        '32456789000110', 'contato@soulup.com.br',       '(11) 3456-7810', DATE '2025-11-10', 'ATIVA');

INSERT INTO TB_EMPRESA (id_empresa, nome_empresa, cnpj_empresa, email_empresa, telefone_empresa, data_cadastro_empresa, status_empresa)
VALUES (2, 'FinPlus Investimentos',  '41789234000155', 'parcerias@finplus.com.br',    '(11) 4002-8922', DATE '2026-01-15', 'ATIVA');

INSERT INTO TB_EMPRESA (id_empresa, nome_empresa, cnpj_empresa, email_empresa, telefone_empresa, data_cadastro_empresa, status_empresa)
VALUES (3, 'VivaBem Saúde',          '28934561000172', 'engajamento@vivabem.com.br',  '(21) 3321-4500', DATE '2026-03-02', 'ATIVA');

INSERT INTO TB_EMPRESA (id_empresa, nome_empresa, cnpj_empresa, email_empresa, telefone_empresa, data_cadastro_empresa, status_empresa)
VALUES (4, 'Mercadão Express',       '19283746000108', 'marketing@mercadao.com.br',   '(11) 2765-1200', DATE '2026-05-20', 'ATIVA');

INSERT INTO TB_EMPRESA (id_empresa, nome_empresa, cnpj_empresa, email_empresa, telefone_empresa, data_cadastro_empresa, status_empresa)
VALUES (5, 'FitZone Academias',      '50617283000191', 'relacionamento@fitzone.com.br', NULL,           DATE '2025-12-05', 'INATIVA');

-- TB_USUARIO
INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (1,  'Ana Beatriz Costa',     'ana.costa@email.com',      RAWTOHEX(STANDARD_HASH('Ana@2025',   'SHA256')), DATE '2025-12-01');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (2,  'Bruno Henrique Lima',   'bruno.lima@email.com',     RAWTOHEX(STANDARD_HASH('Bruno#88',   'SHA256')), DATE '2026-01-05');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (3,  'Carla Mendes Rocha',    'carla.rocha@email.com',    RAWTOHEX(STANDARD_HASH('Carla!321',  'SHA256')), DATE '2026-01-20');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (4,  'Diego Almeida Santos',  'diego.santos@email.com',   RAWTOHEX(STANDARD_HASH('Diego$2026', 'SHA256')), DATE '2026-02-10');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (5,  'Eduarda Ferreira Nunes','eduarda.nunes@email.com',  RAWTOHEX(STANDARD_HASH('Duda@777',   'SHA256')), DATE '2026-02-15');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (6,  'Felipe Augusto Ramos',  'felipe.ramos@email.com',   RAWTOHEX(STANDARD_HASH('Fe1ipe#R',   'SHA256')), DATE '2026-01-08');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (7,  'Gabriela Souza Martins','gabriela.martins@email.com',RAWTOHEX(STANDARD_HASH('Gabi2026!', 'SHA256')), DATE '2026-03-01');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (8,  'Hugo Pereira Tavares',  'hugo.tavares@email.com',   RAWTOHEX(STANDARD_HASH('Hugo#Tav9',  'SHA256')), DATE '2026-03-10');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (9,  'Isabela Carvalho Dias', 'isabela.dias@email.com',   RAWTOHEX(STANDARD_HASH('Isa*Dias5',  'SHA256')), DATE '2026-04-02');

INSERT INTO TB_USUARIO (id_usuario, nome_usuario, email_usuario, senha_usuario, data_cadastro)
VALUES (10, 'João Victor Moreira',   'joao.moreira@email.com',   RAWTOHEX(STANDARD_HASH('JV@novo01',  'SHA256')), TRUNC(SYSDATE) - 1);

-- TB_EMPRESA_USUARIO
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (1, 1,  4, 1350, DATE '2025-12-01');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (1, 2,  3,  850, DATE '2026-01-05');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (1, 3,  3,  550, DATE '2026-01-20');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (1, 4,  1,  150, DATE '2026-02-10');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (1, 6,  5, 1550, DATE '2026-01-08');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (1, 10, 1,    0, TRUNC(SYSDATE) - 1);

INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (2, 1,  1,  120, DATE '2026-02-01');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (2, 5,  2,  250, DATE '2026-02-15');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (2, 7,  2,  370, DATE '2026-03-01');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (2, 8,  1,    0, DATE '2026-03-10');

INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (3, 3,  2,  350, DATE '2026-03-15');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (3, 5,  1,  140, DATE '2026-04-01');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (3, 6,  1,    0, DATE '2026-04-10');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (3, 9,  2,  490, DATE '2026-04-02');

INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (4, 2,  1,  100, DATE '2026-06-01');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (4, 4,  2,  380, DATE '2026-06-05');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (4, 7,  2,  200, DATE '2026-06-10');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (4, 8,  2,  300, DATE '2026-06-12');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (4, 9,  1,    0, DATE '2026-06-20');

INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (5, 1,  1,    0, DATE '2025-12-10');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (5, 3,  1,    0, DATE '2026-01-25');
INSERT INTO TB_EMPRESA_USUARIO (TB_EMPRESA_id_empresa, TB_USUARIO_id_usuario, TB_NIVEL_id_nivel, pontuacao_acumulada, data_associacao) VALUES (5, 6,  2,  400, DATE '2026-01-12');

-- TB_CAMPANHA
INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (1, 'Maratona de Estudos de Outubro',
        'Incentiva os alunos a manter o ritmo de estudos durante o mês, com missões de aulas, módulos e exercícios.',
        DATE '2026-10-01', DATE '2026-10-31', 'ATIVA', 1);

INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (2, 'Desafio do Primeiro Semestre',
        'Campanha semestral de retenção de alunos novos. Encerrada; a pontuação conquistada foi mantida.',
        DATE '2026-02-01', DATE '2026-06-30', 'ENCERRADA', 1);

INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (3, 'Investidor Consciente',
        'Estimula o primeiro aporte e o consumo de conteúdos de educação financeira dentro do app.',
        DATE '2026-09-01', DATE '2026-12-31', 'ATIVA', 2);

INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (4, 'Indique e Ganhe Black Friday',
        'Rascunho salvo para ativação na Black Friday. Missão já cadastrada, aguardando ativação.',
        DATE '2026-11-20', DATE '2026-11-30', 'INATIVA', 2);

INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (5, 'Hábitos Saudáveis',
        'Programa contínuo de hábitos de saúde: hidratação diária e realização de check-up preventivo.',
        DATE '2026-08-01', NULL, 'ATIVA', 3);

INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (6, 'Cliente Fiel Mercadão',
        'Recompensa a recorrência de compras pelo aplicativo e a avaliação de produtos.',
        DATE '2026-07-01', DATE '2026-12-31', 'ATIVA', 4);

INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (7, 'Verão em Movimento',
        'Desafio de frequência na academia durante o verão. Encerrada com a inativação da empresa.',
        DATE '2026-01-10', DATE '2026-03-31', 'ENCERRADA', 5);

INSERT INTO TB_CAMPANHA (id_campanha, nome_campanha, descricao_campanha, data_inicio_campanha, data_fim_campanha, status_campanha, TB_EMPRESA_id_empresa)
VALUES (8, 'Semana do Hortifrúti',
        'Campanha relâmpago de compras de hortifrúti. Pausada pela empresa por falta de estoque.',
        DATE '2026-10-05', DATE '2026-10-25', 'INATIVA', 4);

-- TB_MISSAO
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (1,  'Assistir 5 aulas',            'Assistir a 5 videoaulas completas na plataforma.',            5,  150, 'ATIVA',    1);
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (2,  'Concluir 1 módulo',           'Finalizar todas as aulas e a avaliação de um módulo.',        1,  300, 'ATIVA',    1);
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (3,  'Responder 10 exercícios',     'Responder 10 exercícios práticos do banco de questões.',      10, 200, 'ATIVA',    1);

INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (4,  'Completar trilha de Lógica',  'Concluir a trilha introdutória de lógica de programação.',     1,  500, 'INATIVA',  2);
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (5,  'Estudar por 20 dias',         'Acessar a plataforma e estudar em 20 dias diferentes.',       20, 400, 'INATIVA',  2);

INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (6,  'Fazer o primeiro aporte',     'Realizar o primeiro investimento em qualquer produto.',        1,  250, 'ATIVA',    3);
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (7,  'Ler 3 artigos financeiros',   'Ler 3 artigos da seção de educação financeira até o fim.',     3,  120, 'ATIVA',    3);
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (8,  'Ativar Pix automático',       'Missão removida pela empresa antes de ter participantes.',     1,  180, 'EXCLUIDA', 3);

INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (9,  'Indicar um amigo',            'Indicar um amigo que abra conta e faça um aporte.',            1,  300, 'ATIVA',    4);

INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (10, 'Registrar hidratação 7 dias', 'Registrar no app o consumo de água por 7 dias.',              7,  140, 'ATIVA',    5);
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (11, 'Agendar check-up',            'Agendar e comparecer a um check-up preventivo da rede.',       1,  350, 'ATIVA',    5);

INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (12, 'Fazer 3 compras no app',      'Concluir 3 pedidos pelo aplicativo.',                          3,  200, 'ATIVA',    6);
INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (13, 'Avaliar 5 produtos',          'Deixar avaliação em 5 produtos comprados.',                    5,  100, 'ATIVA',    6);

INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (14, 'Treinar 12 vezes',            'Registrar 12 check-ins de treino na academia.',               12,  400, 'INATIVA',  7);

INSERT INTO TB_MISSAO (id_missao, nome_missao, descricao_missao, meta_missao, pontos_recompensa_missao, status_missao, TB_CAMPANHA_id_campanha)
VALUES (15, 'Comprar 2 itens de hortifrúti','Incluir 2 itens de hortifrúti em um pedido.',                 2,   80, 'ATIVA',    8);

-- TB_USUARIO_MISSAO
INSERT INTO TB_USUARIO_MISSAO VALUES (1, 4,  1, DATE '2026-02-03', DATE '2026-03-20', DATE '2026-03-20');
INSERT INTO TB_USUARIO_MISSAO VALUES (1, 5, 20, DATE '2026-02-03', DATE '2026-04-15', DATE '2026-04-15');
INSERT INTO TB_USUARIO_MISSAO VALUES (1, 1,  5, DATE '2026-10-01', DATE '2026-10-06', DATE '2026-10-06');
INSERT INTO TB_USUARIO_MISSAO VALUES (1, 2,  1, DATE '2026-10-01', DATE '2026-10-12', DATE '2026-10-12');
INSERT INTO TB_USUARIO_MISSAO VALUES (1, 3,  7, DATE '2026-10-02', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (2, 4,  1, DATE '2026-02-05', DATE '2026-04-02', DATE '2026-04-02');
INSERT INTO TB_USUARIO_MISSAO VALUES (2, 1,  5, DATE '2026-10-01', DATE '2026-10-08', DATE '2026-10-08');
INSERT INTO TB_USUARIO_MISSAO VALUES (2, 3, 10, DATE '2026-10-03', DATE '2026-10-14', DATE '2026-10-14');
INSERT INTO TB_USUARIO_MISSAO VALUES (2, 2,  0, DATE '2026-10-10', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (3, 5, 20, DATE '2026-02-10', DATE '2026-05-30', DATE '2026-05-30');
INSERT INTO TB_USUARIO_MISSAO VALUES (3, 1,  5, DATE '2026-10-02', DATE '2026-10-09', DATE '2026-10-09');
INSERT INTO TB_USUARIO_MISSAO VALUES (3, 3,  4, DATE '2026-10-05', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (4, 1,  5, DATE '2026-10-01', DATE '2026-10-11', DATE '2026-10-11');
INSERT INTO TB_USUARIO_MISSAO VALUES (4, 2,  0, DATE '2026-10-12', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (6, 4,  1, DATE '2026-02-02', DATE '2026-03-01', DATE '2026-03-01');
INSERT INTO TB_USUARIO_MISSAO VALUES (6, 5, 20, DATE '2026-02-02', DATE '2026-03-05', DATE '2026-03-05');
INSERT INTO TB_USUARIO_MISSAO VALUES (6, 1,  5, DATE '2026-10-01', DATE '2026-10-04', DATE '2026-10-04');
INSERT INTO TB_USUARIO_MISSAO VALUES (6, 2,  1, DATE '2026-10-01', DATE '2026-10-10', DATE '2026-10-10');
INSERT INTO TB_USUARIO_MISSAO VALUES (6, 3, 10, DATE '2026-10-02', DATE '2026-10-13', DATE '2026-10-13');
INSERT INTO TB_USUARIO_MISSAO VALUES (10, 1, 1, TRUNC(SYSDATE) - 1, NULL, NULL);

INSERT INTO TB_USUARIO_MISSAO VALUES (1, 7,  3, DATE '2026-09-02', DATE '2026-09-10', DATE '2026-09-10');
INSERT INTO TB_USUARIO_MISSAO VALUES (1, 6,  0, DATE '2026-09-02', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (5, 6,  1, DATE '2026-09-03', DATE '2026-09-05', DATE '2026-09-05');
INSERT INTO TB_USUARIO_MISSAO VALUES (5, 7,  2, DATE '2026-09-03', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (7, 6,  1, DATE '2026-09-01', DATE '2026-09-04', DATE '2026-09-04');
INSERT INTO TB_USUARIO_MISSAO VALUES (7, 7,  3, DATE '2026-09-01', DATE '2026-09-20', DATE '2026-09-20');
INSERT INTO TB_USUARIO_MISSAO VALUES (8, 7,  1, DATE '2026-09-15', NULL, NULL);

INSERT INTO TB_USUARIO_MISSAO VALUES (9, 10, 7, DATE '2026-08-01', DATE '2026-08-07', DATE '2026-08-07');
INSERT INTO TB_USUARIO_MISSAO VALUES (9, 11, 1, DATE '2026-08-01', DATE '2026-08-20', DATE '2026-08-20');
INSERT INTO TB_USUARIO_MISSAO VALUES (3, 11, 1, DATE '2026-08-03', DATE '2026-09-12', DATE '2026-09-12');
INSERT INTO TB_USUARIO_MISSAO VALUES (3, 10, 5, DATE '2026-08-03', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (5, 10, 7, DATE '2026-08-10', DATE '2026-08-16', DATE '2026-08-16');
INSERT INTO TB_USUARIO_MISSAO VALUES (6, 10, 3, DATE '2026-09-01', NULL, NULL);

INSERT INTO TB_USUARIO_MISSAO VALUES (4, 12, 3, DATE '2026-07-02', DATE '2026-07-20', DATE '2026-07-20');
INSERT INTO TB_USUARIO_MISSAO VALUES (4, 13, 5, DATE '2026-07-02', DATE '2026-08-15', DATE '2026-08-15');
INSERT INTO TB_USUARIO_MISSAO VALUES (4, 15, 2, DATE '2026-10-05', DATE '2026-10-08', DATE '2026-10-08');
INSERT INTO TB_USUARIO_MISSAO VALUES (8, 12, 3, DATE '2026-07-05', DATE '2026-08-01', DATE '2026-08-01');
INSERT INTO TB_USUARIO_MISSAO VALUES (8, 13, 5, DATE '2026-07-05', DATE '2026-09-02', DATE '2026-09-02');
INSERT INTO TB_USUARIO_MISSAO VALUES (7, 12, 3, DATE '2026-07-10', DATE '2026-09-18', DATE '2026-09-18');
INSERT INTO TB_USUARIO_MISSAO VALUES (7, 15, 1, DATE '2026-10-06', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (2, 13, 5, DATE '2026-07-03', DATE '2026-07-30', DATE '2026-07-30');
INSERT INTO TB_USUARIO_MISSAO VALUES (2, 12, 1, DATE '2026-07-03', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (9, 12, 2, DATE '2026-07-15', NULL, NULL);

INSERT INTO TB_USUARIO_MISSAO VALUES (6, 14, 12, DATE '2026-01-12', DATE '2026-02-20', DATE '2026-02-20');
INSERT INTO TB_USUARIO_MISSAO VALUES (1, 14,  8, DATE '2026-01-10', NULL, NULL);
INSERT INTO TB_USUARIO_MISSAO VALUES (3, 14,  5, DATE '2026-01-26', NULL, NULL);

-- TB_INTERACAO
INSERT INTO TB_INTERACAO VALUES (1, 1, TO_DATE('2026-10-06 19:20', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (1, 1, TO_DATE('2026-10-06 19:21', 'YYYY-MM-DD HH24:MI'), 'RECOMPENSA_RESGATADA');
INSERT INTO TB_INTERACAO VALUES (1, 1, TO_DATE('2026-10-12 20:05', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (1, 1, TO_DATE('2026-10-12 20:06', 'YYYY-MM-DD HH24:MI'), 'RECOMPENSA_RESGATADA');
INSERT INTO TB_INTERACAO VALUES (1, 1, TO_DATE('2026-10-17 08:15', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (1, 1, TO_DATE('2026-10-17 08:17', 'YYYY-MM-DD HH24:MI'), 'RANKING_VISUALIZADO');
INSERT INTO TB_INTERACAO VALUES (2, 1, TO_DATE('2026-10-14 18:30', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (2, 1, TO_DATE('2026-10-14 18:31', 'YYYY-MM-DD HH24:MI'), 'RECOMPENSA_RESGATADA');
INSERT INTO TB_INTERACAO VALUES (2, 1, TO_DATE('2026-10-17 07:40', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (3, 1, TO_DATE('2026-10-15 21:10', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (3, 1, TO_DATE('2026-10-15 21:12', 'YYYY-MM-DD HH24:MI'), 'NIVEL_CONSULTADO');
INSERT INTO TB_INTERACAO VALUES (4, 1, TO_DATE('2026-10-11 19:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (6, 1, TO_DATE('2026-10-13 22:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (6, 1, TO_DATE('2026-10-17 06:55', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (6, 1, TO_DATE('2026-10-17 06:58', 'YYYY-MM-DD HH24:MI'), 'NIVEL_CONSULTADO');
INSERT INTO TB_INTERACAO VALUES (10, 1, TRUNC(SYSDATE) - 1 + 20/24,           'LOGIN');
INSERT INTO TB_INTERACAO VALUES (10, 1, TRUNC(SYSDATE) - 1 + 20/24 + 2/1440,  'MISSAO_INICIADA');
INSERT INTO TB_INTERACAO VALUES (10, 1, TRUNC(SYSDATE) - 1 + 20/24 + 5/1440,  'RANKING_VISUALIZADO');

INSERT INTO TB_INTERACAO VALUES (1, 2, TO_DATE('2026-09-10 12:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (1, 2, TO_DATE('2026-10-17 12:30', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (5, 2, TO_DATE('2026-09-05 10:15', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (5, 2, TO_DATE('2026-10-02 09:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (7, 2, TO_DATE('2026-09-20 17:45', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (7, 2, TO_DATE('2026-10-17 09:10', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (7, 2, TO_DATE('2026-10-17 09:12', 'YYYY-MM-DD HH24:MI'), 'RANKING_VISUALIZADO');
INSERT INTO TB_INTERACAO VALUES (8, 2, TO_DATE('2026-10-16 09:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');

INSERT INTO TB_INTERACAO VALUES (9, 3, TO_DATE('2026-08-20 08:30', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (9, 3, TO_DATE('2026-10-17 07:00', 'YYYY-MM-DD HH24:MI'), 'SEQUENCIA_VISUALIZADA');
INSERT INTO TB_INTERACAO VALUES (3, 3, TO_DATE('2026-09-12 14:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (3, 3, TO_DATE('2026-10-17 13:20', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (5, 3, TO_DATE('2026-08-16 21:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (6, 3, TO_DATE('2026-10-15 20:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');

INSERT INTO TB_INTERACAO VALUES (4, 4, TO_DATE('2026-10-08 18:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (4, 4, TO_DATE('2026-10-17 11:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (8, 4, TO_DATE('2026-09-02 19:30', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (8, 4, TO_DATE('2026-10-17 10:05', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (7, 4, TO_DATE('2026-10-06 12:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_INICIADA');
INSERT INTO TB_INTERACAO VALUES (7, 4, TO_DATE('2026-10-15 18:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (2, 4, TO_DATE('2026-07-30 20:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (9, 4, TO_DATE('2026-10-17 15:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');

INSERT INTO TB_INTERACAO VALUES (6, 5, TO_DATE('2026-02-20 07:00', 'YYYY-MM-DD HH24:MI'), 'MISSAO_CONCLUIDA');
INSERT INTO TB_INTERACAO VALUES (6, 5, TO_DATE('2026-03-28 06:30', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (1, 5, TO_DATE('2026-03-15 18:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');
INSERT INTO TB_INTERACAO VALUES (3, 5, TO_DATE('2026-02-10 19:00', 'YYYY-MM-DD HH24:MI'), 'LOGIN');

-- TB_RANKING
INSERT INTO TB_RANKING VALUES (6, 1, 1, 1550, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (1, 1, 2, 1350, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (2, 1, 3,  850, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (3, 1, 4,  550, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (4, 1, 5,  150, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (7, 2, 1,  370, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (5, 2, 2,  250, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (1, 2, 3,  120, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (8, 2, 4,    0, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (9, 3, 1,  490, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (3, 3, 2,  350, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (5, 3, 3,  140, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (6, 3, 4,    0, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (4, 4, 1,  380, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (8, 4, 2,  300, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (7, 4, 3,  200, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (2, 4, 4,  100, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (9, 4, 5,    0, DATE '2026-10-17');
INSERT INTO TB_RANKING VALUES (6, 5, 1,  400, DATE '2026-03-31');
INSERT INTO TB_RANKING VALUES (1, 5, 2,    0, DATE '2026-03-31');
INSERT INTO TB_RANKING VALUES (3, 5, 3,    0, DATE '2026-03-31');

-- TB_SEQUENCIA
INSERT INTO TB_SEQUENCIA VALUES (1,  1, 16, 'ATIVA',    DATE '2026-10-02', TIMESTAMP '2026-10-17 08:17:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (2,  1,  9, 'ATIVA',    DATE '2026-10-09', TIMESTAMP '2026-10-17 07:40:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (3,  1,  4, 'SUSPENSA', DATE '2026-10-12', TIMESTAMP '2026-10-15 21:12:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (4,  1,  0, 'ZERADA',   DATE '2026-10-08', TIMESTAMP '2026-10-11 19:00:00', DATE '2026-10-13');
INSERT INTO TB_SEQUENCIA VALUES (6,  1, 30, 'ATIVA',    DATE '2026-09-18', TIMESTAMP '2026-10-17 06:58:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (10, 1,  1, 'ATIVA',    TRUNC(SYSDATE) - 1, CAST(TRUNC(SYSDATE) - 1 + 20/24 + 5/1440 AS TIMESTAMP), NULL);
INSERT INTO TB_SEQUENCIA VALUES (1,  2,  3, 'ATIVA',    DATE '2026-10-15', TIMESTAMP '2026-10-17 12:30:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (5,  2,  0, 'ZERADA',   DATE '2026-09-28', TIMESTAMP '2026-10-02 09:00:00', DATE '2026-10-04');
INSERT INTO TB_SEQUENCIA VALUES (7,  2, 12, 'ATIVA',    DATE '2026-10-06', TIMESTAMP '2026-10-17 09:12:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (8,  2,  1, 'SUSPENSA', DATE '2026-10-16', TIMESTAMP '2026-10-16 09:00:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (3,  3,  7, 'ATIVA',    DATE '2026-10-11', TIMESTAMP '2026-10-17 13:20:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (5,  3,  0, 'ZERADA',   DATE '2026-08-10', TIMESTAMP '2026-08-16 21:00:00', DATE '2026-08-18');
INSERT INTO TB_SEQUENCIA VALUES (6,  3,  2, 'SUSPENSA', DATE '2026-10-14', TIMESTAMP '2026-10-15 20:00:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (9,  3, 21, 'ATIVA',    DATE '2026-09-27', TIMESTAMP '2026-10-17 07:00:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (2,  4,  0, 'ZERADA',   DATE '2026-07-25', TIMESTAMP '2026-07-30 20:00:00', DATE '2026-08-01');
INSERT INTO TB_SEQUENCIA VALUES (4,  4, 14, 'ATIVA',    DATE '2026-10-04', TIMESTAMP '2026-10-17 11:00:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (7,  4,  3, 'SUSPENSA', DATE '2026-10-13', TIMESTAMP '2026-10-15 18:00:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (8,  4,  5, 'ATIVA',    DATE '2026-10-13', TIMESTAMP '2026-10-17 10:05:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (9,  4,  1, 'ATIVA',    DATE '2026-10-17', TIMESTAMP '2026-10-17 15:00:00', NULL);
INSERT INTO TB_SEQUENCIA VALUES (1,  5,  0, 'ZERADA',   DATE '2026-03-10', TIMESTAMP '2026-03-15 18:00:00', DATE '2026-03-17');
INSERT INTO TB_SEQUENCIA VALUES (3,  5,  0, 'ZERADA',   DATE '2026-02-05', TIMESTAMP '2026-02-10 19:00:00', DATE '2026-02-12');
INSERT INTO TB_SEQUENCIA VALUES (6,  5,  0, 'ZERADA',   DATE '2026-03-01', TIMESTAMP '2026-03-28 06:30:00', DATE '2026-03-30');

COMMIT;

-- Ajusta o contador dos IDs (IDENTITY) após a carga
ALTER TABLE TB_NIVEL    MODIFY (id_nivel    GENERATED BY DEFAULT ON NULL AS IDENTITY (START WITH LIMIT VALUE));
ALTER TABLE TB_EMPRESA  MODIFY (id_empresa  GENERATED BY DEFAULT ON NULL AS IDENTITY (START WITH LIMIT VALUE));
ALTER TABLE TB_USUARIO  MODIFY (id_usuario  GENERATED BY DEFAULT ON NULL AS IDENTITY (START WITH LIMIT VALUE));
ALTER TABLE TB_CAMPANHA MODIFY (id_campanha GENERATED BY DEFAULT ON NULL AS IDENTITY (START WITH LIMIT VALUE));
ALTER TABLE TB_MISSAO   MODIFY (id_missao   GENERATED BY DEFAULT ON NULL AS IDENTITY (START WITH LIMIT VALUE));

-- Conferência: quantidade de registros por tabela
SELECT 'TB_NIVEL'           AS tabela, COUNT(*) AS registros FROM TB_NIVEL
UNION ALL SELECT 'TB_EMPRESA',         COUNT(*) FROM TB_EMPRESA
UNION ALL SELECT 'TB_USUARIO',         COUNT(*) FROM TB_USUARIO
UNION ALL SELECT 'TB_EMPRESA_USUARIO', COUNT(*) FROM TB_EMPRESA_USUARIO
UNION ALL SELECT 'TB_CAMPANHA',        COUNT(*) FROM TB_CAMPANHA
UNION ALL SELECT 'TB_MISSAO',          COUNT(*) FROM TB_MISSAO
UNION ALL SELECT 'TB_USUARIO_MISSAO',  COUNT(*) FROM TB_USUARIO_MISSAO
UNION ALL SELECT 'TB_INTERACAO',       COUNT(*) FROM TB_INTERACAO
UNION ALL SELECT 'TB_RANKING',         COUNT(*) FROM TB_RANKING
UNION ALL SELECT 'TB_SEQUENCIA',       COUNT(*) FROM TB_SEQUENCIA;
