# Histórias de usuário e critérios de aceite BDD

## Metadados e escopo

- Data: 18/11/2026.
- Solicitação: [prompt de criação de histórias e BDD](../prompts/05_HISTORIAS_USUARIO_CENARIOS_BDD.md).
- Escopo assumido: funcionalidades atuais a refatorar (Clientes, Produtos, Consumos, Pagamentos e Inicialização) e evolução conhecida (Valores, Sauna, Reservas, Mensalidades, Relatórios e Administração).
- IDs US-01 a US-11 são identificadores locais criados nesta etapa; não são uma numeração oficial da Entrega 1. Tags `@US01` a `@US11` fazem a correspondência nos arquivos.
- Estado: histórias e especificações Gherkin criadas. Sem definições de passos, runner ou dependência Cucumber adicionados; nenhum cenário foi executado como teste. Não somar estes cenários aos resultados históricos de JUnit.
- Inventário conferido: 11 arquivos `.feature`, 88 cenários/esquemas e 220 casos após expansão das tabelas de exemplos. A contagem inclui caracterizações, propostas e critérios com regras pendentes; não significa 220 casos prontos para aceite ou aprovados.
- Fontes funcionais: [histórias do legado](../contexto%20IA/6eac3a3f-4194-48c2-a3ab-9c3033dc1b16.pdf), [relatório parcial](../contexto%20IA/TRABALHO%20FINAL.pdf), [exigências da Entrega 2](../contexto%20IA/o%20que%20nao%20pode%20faltar.pdf), código atual de serviços/controladores/repositórios e [auditoria](../auditorias/2026-11-18_AUDITORIA_DOCUMENTACAO.md).
- O usuário identificou `TRABALHO FINAL.pdf` como a Entrega 1. Os cenários deste catálogo são novos critérios propostos a partir das fontes disponíveis: o PDF declara ausência de BDD formal e não contém este conjunto de histórias/casos. Justificar sua inclusão como complementação da estratégia, sem atribuí-los à Entrega 1. O escopo amplo do catálogo não equivale a escopo obrigatório aprovado; cruzar cada história com a Entrega 1 e o enunciado.

## 1. História de Usuário (User Story)

### US-01: Manter clientes

- **Como** atendente,
- **Eu quero** cadastrar, consultar, atualizar e excluir clientes conforme seus vínculos,
- **Para que** eu identifique os responsáveis por consumos e pagamentos e preserve seu histórico.
- **Fontes:** histórias §1.2.2 e testes de foco pp. 18–19; RN-01/RN-02 do relatório; `ClienteService`, `ClienteController` e `ClienteRepository`.
- **Critérios:** [clientes.feature](../../src/test/resources/features/clientes.feature). Nome obrigatório e limites 50/51; CPF até 11 e telefone até 15; geração de código; distinção entre criação e edição; busca e exclusão; confirmação/cancelamento; vínculos de consumo, pagamento, sauna e squash.
- **Limite:** validar formato matemático de CPF não é exigido pelo documento original. Limites de comprimento são validações do código/esquema. O cliente interno zero não é um cadastro comum.

### US-02: Manter produtos

- **Como** atendente,
- **Eu quero** manter descrição, unidade e preço dos produtos,
- **Para que** eu registre corretamente os itens consumidos.
- **Fontes:** histórias §1.2.1 e tabela §3.1; `ProdutoService`, `ProdutoController` e `ProdutoRepository`.
- **Critérios:** [produtos.feature](../../src/test/resources/features/produtos.feature). Campos obrigatórios, limites 100/101, preço finito e não negativo, separadores decimal ponto/vírgula, código automático/informado, atualização e exclusão com integridade de consumos.
- **Limite:** estas validações de código positivo aplicam-se ao cadastro comum. Os códigos reservados 0, 1, -1, -2 e -3 existem como dados internos; a política de manutenção deles permanece pendente.

### US-03: Registrar e preservar consumos

- **Como** atendente,
- **Eu quero** registrar consumos, consultar sua identificação e preservar os registros pagos,
- **Para que** eu mantenha uma cobrança fiel aos itens consumidos.
- **Fontes:** histórias §1.2.4; `ConsumoService`, `ConsumoController`, `ConsumoRepository` e teste de consumo atual.
- **Critérios:** [consumos.feature](../../src/test/resources/features/consumos.feature). Cliente/produto válidos, quantidade inteira positiva, data, valor finito não negativo, situação obrigatória, identificação cliente/data/sequência, total parcial, bloqueio da edição manual e exclusão de consumo pago.
- **Desvio explícito:** o código atual e seu teste permitem edição e exclusão após pagamento. Os cenários de proibição são o comportamento esperado pela história e estão marcados `@a_refatorar`.
- **Limite:** quitação automática é uma operação do recebimento de pagamentos, não edição manual do consumo. O contrato de armazenamento do valor precisa ser decidido antes de alterar os cálculos financeiros.

### US-04: Receber pagamentos e quitar consumos

- **Como** atendente,
- **Eu quero** receber pagamentos vinculados aos consumos do cliente,
- **Para que** eu mantenha o saldo devido e a quitação consistentes.
- **Fontes:** histórias §1.2.5; relatório parcial §4; `PagamentoService`, `PagamentoController` e `PagamentoRepository`; issue #7 do plano.
- **Critérios:** [pagamentos.feature](../../src/test/resources/features/pagamentos.feature). Quitação integral sem desconto; parcial com geração do saldo remanescente; validações; sequência; consultas; proposta de preservação integral em falha.
- **Limite:** alterações/exclusões atuais e valor zero estão em cenários `@caracterizacao @pendente_regra`, sem aprovação como regras do fluxo financeiro futuro. Desconto, excedente, período e estorno dependem de definição.

### US-05: Iniciar o atendimento com situação operacional clara

- **Como** operador,
- **Eu quero** identificar se o sistema está pronto para atendimento,
- **Para que** eu evite operações sem acesso aos dados necessários.
- **Fontes:** verificações ao iniciar na p. 24 das histórias; diagnóstico de inicialização; `Inicializacao`, `DatabaseConnection`, `DatabaseCheck`, `TelaLoader` e `PrincipalController`.
- **Critérios:** [inicializacao.feature](../../src/test/resources/features/inicializacao.feature). Configuração válida/inválida, senha ausente versus explicitamente vazia, falhas de acesso sem exposição de credenciais, esquema/dados básicos, retorno ao início e falha de módulo.
- **Desvio explícito:** conexão confirmada não comprova a presença do esquema e dados básicos. Esses cenários estão em `@a_refatorar`. O aviso de módulo pendente é caracterização transitória, não aceite final da migração.

### US-06: Configurar valores e vencimentos

- **Como** gestor,
- **Eu quero** configurar os preços dos serviços e os vencimentos,
- **Para que** eu aplique as cobranças previstas para sauna e squash.
- **Fonte:** histórias §1.2.3.
- **Critérios:** [valores.feature](../../src/test/resources/features/valores.feature). Configuração única, consulta/atualização, valores obrigatórios, primeiro vencimento obrigatório, dias distintos até 28 e duas faixas adicionais opcionais.
- **Limite:** completude dos pares opcionais, ordem dos vencimentos e admissibilidade de valores zero/negativos precisam de decisão antes de adicionar expectativas novas.

### US-07: Atender clientes avulsos da sauna

- **Como** atendente,
- **Eu quero** manter contas individuais dos clientes avulsos da sauna,
- **Para que** eu cobre a utilização e os produtos consumidos por pessoa.
- **Fonte:** histórias §1.3, §1.3.1, §1.3.2 e §1.3.3.
- **Critérios:** [sauna.feature](../../src/test/resources/features/sauna.feature). Data atual, entrada por nome, cobrança inicial, consumo por conta, exclusão apenas antes do pagamento e restrição de alteração em data diferente da atual, mantendo pagamento de conta anterior.
- **Limite:** o código comum não deve ser aplicado diretamente à conta avulsa baseada nos registros internos do legado. Desconto da sauna e múltiplos clientes homônimos exigem detalhamento adicional.

### US-08: Reservar quadras e aulas de squash

- **Como** atendente,
- **Eu quero** organizar reservas de quadras e aulas,
- **Para que** eu consulte a ocupação e evite reservas conflitantes.
- **Fonte:** histórias §1.4 e §1.4.2.
- **Critérios:** [reservas.feature](../../src/test/resources/features/reservas.feature). Jogo com múltiplos participantes, aula, conflito no mesmo dia/horário/quadra, outras reservas do cliente em horários livres, seleção de participantes, consulta por dia e exclusão confirmada/cancelada.
- **Limite:** duração, sobreposição entre horários diferentes, limites de quadras, horários válidos e quantidade de alunos por aula não estão determinados nos critérios; definir antes da automação desses casos.

### US-09: Gerar mensalidades e multas de squash

- **Como** gestor,
- **Eu quero** gerar cobranças mensais a partir das reservas,
- **Para que** eu distribua os valores e identifique débitos por atraso.
- **Fonte:** histórias §1.4.3.
- **Critérios:** [mensalidades.feature](../../src/test/resources/features/mensalidades.feature). Divisão igual da mensalidade, aula multiplicada por quatro, não duplicação mensal, ausência de multa para aula ou dívida paga/não vencida.
- **Limite:** os cenários de primeira e segunda multa estão em `@pendente_regra`, pois a redação original sobre datas/geração não fixa todas as condições. Os exemplos usam divisões exatas e não presumem regra de arredondamento.

### US-10: Consultar a situação dos clientes e do caixa

- **Como** gestor,
- **Eu quero** consultar reservas, consumos e totais financeiros,
- **Para que** eu acompanhe débitos e resultados do estabelecimento.
- **Fonte:** histórias §1.5.1 e §1.5.2.
- **Critérios:** [relatorios.feature](../../src/test/resources/features/relatorios.feature). Ficha por cliente, isolamento dos dados, cliente não encontrado, totais pagos/não pagos por data e categoria e data sem movimento.
- **Limite:** fixture de fluxo de caixa sem desconto nem saldo parcial evita inventar fórmulas pendentes. Resumo mensal e composição final após desconto/estorno precisam de levantamento próprio.

### US-11: Recuperar dados por cópia de segurança

- **Como** responsável pela manutenção,
- **Eu quero** gerar e restaurar cópias de segurança,
- **Para que** eu recupere os dados do estabelecimento quando necessário.
- **Fonte:** issue #9 e referências de backup/restauração do legado; não há história detalhada no PDF de 24 páginas.
- **Critérios propostos:** [administracao.feature](../../src/test/resources/features/administracao.feature). Cópia identificável, restauração verificável e ausência de confirmação falsa em erro.
- **Limite:** toda a funcionalidade está marcada `@proposta @pendente_regra`; formato, escopo, destino, permissões e efeitos precisam de definição. Não executar contra banco da aplicação.

## 2. Especificação de Cenários BDD (Sintaxe Gherkin)

Os arquivos `.feature` vinculados acima são a fonte dos critérios e usam `# language: pt`, `Funcionalidade`, `Contexto`, `Cenário`, `Esquema do Cenário`, `Exemplos`, `Dado`, `Quando`, `Então`, `E` e `Mas`. Cada cenário deve partir de dados próprios; não depender da execução de outro cenário.

### Estados e tags

| Tag | Significado |
|---|---|
| `@US01` … `@US11` | História local correspondente |
| `@RN01`, `@RN02` | Regras identificadas no relatório de clientes |
| `@a_refatorar` | Comportamento esperado cuja implementação atual diverge ou está incompleta |
| `@a_desenvolver` | Módulo ainda não migrado para JavaFX |
| `@caracterizacao` | Retrato do comportamento atual; não autoriza regra financeira futura |
| `@proposta` | Critério derivado/recomendado, ainda sujeito à revisão da equipe |
| `@pendente_regra` | Decisão funcional pendente; não usar para atestar aceite |
| `@persistencia`, `@interface` | Necessidade indicada de validação nessa fronteira, sem prescrever ferramenta |

Nenhuma tag desabilita testes por si só. Quando o runner existir, separar explicitamente os casos pendentes e de caracterização dos critérios de aceite, publicar as contagens de cada grupo e não apresentar casos excluídos como aprovados. Essa separação não pode ocultar falta de implementação ou cobertura.

### Vocabulário e convenções para automatizar os passos

- **Cliente/produto comum:** cadastro de atendimento, distinto dos registros internos reservados. Exemplos de código não positivo não proíbem a existência dos registros especiais do legado.
- **Dados ausentes:** valor não informado; **vazio:** texto sem caracteres; **apenas espaços:** texto em branco. O passo prepara cada caso explicitamente.
- **N caracteres válidos:** gerar uma sequência determinística de letras com exatamente N caracteres, sem espaços nas extremidades. Para a fronteira de serviço, não normalizar o valor antes da validação; para o formulário, respeitar o tratamento documentado da entrada.
- **Valor não finito:** expandir em dados de teste para número indefinido e infinitos positivo/negativo; rejeitar todos. Texto não numérico deve ser rejeitado na entrada, sem chegar como valor financeiro válido.
- **Valor zero:** aceito pelos validadores atuais de produto/consumo; em pagamentos permanece caracterização pendente, sem presumir recebimento zero válido no fluxo de quitação.
- **Dados válidos:** um objeto/registro identificável, com todos os campos obrigatórios preenchidos, sem vínculos inesperados; definir fixtures estáveis ao implementar passos.
- **Consulta vazia ou inexistente:** distinguir lista sem registros, identificação que não existe e falta de dados para pesquisar. Não substituir ausência de cadastro por falha do banco.
- **Total parcial:** quantidade × valor unitário exibido ao atendente. A representação persistida do campo `valor` continua pendente e não deve ser inferida desse cálculo.
- **Falha:** o resultado deve verificar tanto a rejeição quanto a ausência de alteração indevida. Mensagens são verificadas pelo significado, sem impor texto literal não exigido pelas fontes.
- **Datas e dinheiro:** exemplos usam datas explícitas e valores decimais em reais; preparar relógio controlável e comparação monetária coerente ao implementar automação.

### Decisões abertas que não foram convertidas em resultados presumidos

| ID local | Questão | Impacto |
|---|---|---|
| DEC-01 | Política de alteração/exclusão dos produtos internos 0, 1, -1, -2, -3 | US-02 e módulos de serviço |
| DEC-02 | `valor` do consumo representa preço unitário ou total? Como compatibilizar o legado? | US-03, US-04, US-07, US-10 |
| DEC-03 | Fórmula, sinal, contabilização e consumo gerado para desconto | US-04, US-07, US-10 |
| DEC-04 | Excedente, recebimento zero, cliente sem dívida, período e concorrência da quitação | US-04 |
| DEC-05 | Edição, exclusão e estorno de pagamentos após quitação | US-04 e relatórios |
| DEC-06 | Ordem/completude dos vencimentos opcionais e limites dos valores | US-06 |
| DEC-07 | Identificação de homônimos na sauna e desconto de conta avulsa | US-07 |
| DEC-08 | Duração, sobreposição, quadras e alunos por aula | US-08 |
| DEC-09 | Datas, repetição das multas, segunda faixa e arredondamento | US-09 |
| DEC-10 | Fórmulas finais de relatórios, descontos, saldos e resumo mensal | US-10 |
| DEC-11 | Formato, escopo, permissões, destino e efeitos de backup/restauração | US-11 |

### Rastreabilidade das validações existentes

| Origem | Critérios correspondentes |
|---|---|
| `ClienteService.validar` | US-01: dados ausentes, código positivo, nome obrigatório/limite, limites de CPF/telefone |
| `ProdutoService.validar` | US-02: dados ausentes, código positivo, descrição/unidade obrigatórias/limites e preço válido |
| `ConsumoService.validar` | US-03: dados ausentes, cliente/produto, data, quantidade, valor, situação e sequência; sequência positiva do contrato também deve ser verificada diretamente na fronteira de serviço |
| `PagamentoService.validar` | US-04: dados ausentes, cliente/data/valor; contrato atual de situação total e sequência em caracterização |
| Controladores de cadastro | US-01/02: novo versus edição, identidade preservada, pesquisa, confirmação/cancelamento e dados numéricos inválidos |
| Controladores de consumo/pagamento | US-03/04: seleção obrigatória, consulta completa, registro removido, sequência automática; edição financeira depende das histórias e decisões |
| Restrições de repositórios | US-01/02/03: vínculos e exclusões; US-03/04: identificação e persistência; banco exclusivo para testar |
| Inicialização e navegação | US-05: configuração, acesso, esquema/dados básicos, conteúdo inicial e falha de abertura |

Não é uma alegação de cobertura de todo ramo Java ou de todos os componentes legados. Condições de objetos internos e infraestrutura continuam exigindo testes técnicos JUnit; critérios funcionais não devem expor construtores, exceções, SQL, seletores ou detalhes de FXML.

### Próxima etapa de automação

1. Revisar os critérios e fechar as decisões necessárias com a equipe; relacioná-los à Entrega 1 identificada (`TRABALHO FINAL.pdf`) e registrar as complementações no relatório técnico.
2. Implementar definições de passos e execução Cucumber compatíveis com o projeto JavaFX/Maven. Cypress só se aplica se houver uma interface web; não é a ferramenta proposta para automatizar diretamente esta aplicação desktop.
3. Usar serviços/casos de uso reais, PostgreSQL de testes para persistência e automação JavaFX quando o comportamento depender da interface. Um passo que sempre retorna o resultado esperado não valida o requisito.
4. Executar primeiro as regras de clientes e de preservação de consumos; registrar falhas reais antes de corrigir os desvios. Manter o JUnit e revisar o teste atual que aceita exclusão de consumo pago.
5. Gerar resultados com revisão do código, cenários descobertos, implementados, pendentes, executados e aprovados separados. Os arquivos Gherkin, sozinhos, não cumprem a exigência de execução da Entrega 2.

## Registro de progresso

- Criados catálogo de histórias, arquivos Gherkin e registro do prompt.
- Validação desta etapa: 11 funcionalidades com idioma declarado, presença de ação e resultado em cada cenário, títulos locais sem duplicação, parâmetros dos esquemas correspondentes às colunas e tabelas consistentes; 55 links locais conferidos, sem links quebrados. Verificação estrutural própria, sem execução por Cucumber nem validação por parser oficial de Gherkin.
- Pendentes: revisão de negócio, decisões abertas, passos, runner, integração PostgreSQL e correções.
