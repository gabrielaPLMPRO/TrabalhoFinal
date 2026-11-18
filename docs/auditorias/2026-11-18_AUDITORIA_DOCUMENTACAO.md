# Auditoria de documentação, status e análise de coerência

## Metadados

- Data: 18/11/2026.
- Projeto: Dante Sauna & Squash.
- Origem: [prompt de auditoria](../prompts/03_AUDITORIA_DOCUMENTACAO_STATUS_COERENCIA.md).
- Escopo: os 30 arquivos então presentes em `docs`, incluindo dois PDFs, Markdown, logs e XML, confrontados com código e histórico Git.
- Natureza: consolidação do relatório produzido na conversa; retrato do estado anterior a este salvamento documental.
- Validação: inspeção dos artefatos existentes. Os testes não foram reexecutados na auditoria.
- Continuidade: [lacuna BDD e expansão de contexto](../bdd/2026-11-18_LACUNA_BDD_E_CONTEXTO.md).

## 1. Ponto de Parada Atual (Onde Paramos)

O projeto parou após concluir a refatoração da inicialização e da navegação JavaFX, ampliar os testes e registrar o processo. A migração funcional completa ainda está pendente.

| Última entrega | Evidência no Git | Situação |
|---|---|---|
| Inicialização e integração das telas | `08ca99b`, de 17/11/2026 | Implementada em 11 arquivos de produção |
| Ampliação dos testes | `c3c0b7b` | Cinco arquivos de testes adicionados ou ampliados |
| Diagnóstico, evidências e plano de evolução | `cf9fafc` | Documentados |
| Preservação dos prompts | `cdafa64`, às 21h29 de 17/11/2026 | Último commit da branch `main` na inspeção |

Os arquivos centrais da última implementação são `DanteApp`, `Inicializacao`, `TelaLoader`, `PrincipalController`, os quatro controladores de cadastro, `DatabaseConnection`, `DatabaseCheck` e `principal.fxml`.

O próximo bloqueio registrado é preparar o PostgreSQL de testes e validar a persistência real, conforme a issue #3 do [plano de evolução](../PLANO_ISSUES_COMMITS.md).

Estado do diretório observado na auditoria:

- Exclusão local, ainda não preparada para commit, de `TRABALHO FINAL.pdf` na raiz.
- Pasta `docs/contexto IA/` ainda não versionada, contendo dois PDFs.
- O relatório nessa pasta tem o mesmo hash do PDF versionado na raiz: evidência de realocação sem mudança de conteúdo (`159f5902cdbdb746b249b0076611d1af33e7d405`).
- `main` e a referência local `origin/main` apontavam para `cdafa64`; não foi feita consulta atual ao servidor remoto.

A última suíte completa registrada executou **50 testes: 43 aprovados e 7 falhas por ausência da configuração do banco de testes**. Os números foram conferidos nos [XML e logs existentes](../validacao/README.md); não houve nova execução.

## 2. Resumo Executivo do Desenvolvimento

| Componente | O que está efetivamente construído |
|---|---|
| Aplicação desktop | JavaFX, FXML e CSS, com compilação Maven configurada para Java 21 |
| Organização | Pacotes de controladores, modelos, serviços e repositórios |
| Clientes | Cadastro, consulta, listagem, atualização e exclusão condicionada à ausência de vínculos |
| Produtos | Cadastro, consulta, listagem, atualização e exclusão com tratamento de dependências |
| Consumos | Cadastro, consulta, listagem, atualização e exclusão, identificados por cliente/data/sequência |
| Pagamentos | Cadastro, consulta, listagem, atualização e exclusão, com indicador informado de pagamento total |
| Persistência | JDBC/PostgreSQL, consultas parametrizadas, fechamento de recursos e transações em operações específicas |
| Inicialização | Validação das variáveis de ambiente, verificação de conexão, timeouts e mensagem controlada de indisponibilidade |
| Navegação | Carregamento centralizado, injeção da configuração nos controladores e retorno ao início |
| Testes | Validações de serviços, configuração, inicialização, navegação/FXML e cenários de repositório |

A aplicação permite abrir a tela inicial sem banco configurado, mas bloqueia os quatro módulos de cadastro nessa situação.

O legado Swing permanece em `src/dao`, `src/gui` e `src/model`. Esses pacotes não integram a compilação Maven atual. Funções existentes exclusivamente no legado não estão disponíveis na aplicação JavaFX.

Reservas, Sauna, Mensalidades e Relatórios possuem entradas no menu, mas exibem avisos de pendência. Backup, restauração e administração do esquema também não foram migrados. A reconstrução parcial está descrita no [diagnóstico histórico](../DIAGNOSTICO_REFATORACAO.md).

## 3. Análise de Coerência com a Pasta docs

### Diagnóstico de alinhamento: Parcialmente

A implementação acompanha a arquitetura e o escopo parcial dos registros recentes, mas não atende integralmente às histórias funcionais do PDF de 24 páginas.

- [Histórias do sistema](../contexto%20IA/6eac3a3f-4194-48c2-a3ab-9c3033dc1b16.pdf): comportamentos esperados, regras dos módulos e estrutura das tabelas.
- [Relatório acadêmico](../contexto%20IA/TRABALHO%20FINAL.pdf): reconstrução parcial e resultados históricos.

### Pontos coerentes

- JavaFX/FXML/CSS, Maven e JDBC correspondem à solução documentada.
- As quatro áreas implementadas correspondem ao escopo declarado no relatório parcial.
- RN-01 está expressa em serviço: nome obrigatório e limite de 50 caracteres, com testes dos limites.
- RN-02 está implementada no repositório: bloqueio da exclusão de clientes com consumos ou pagamentos, incluindo outros vínculos legados.
- As chaves compostas de consumos e pagamentos são preservadas.
- A documentação distingue JDBC substituto de persistência real e declara a ausência de BDD formal e dos módulos restantes.

### Incoerências e desvios

| Relevância | Divergência e evidência |
|---|---|
| Alta | A história §1.2.4 proíbe alterar consumos; `ConsumoController` chama `ConsumoRepository.atualizar`, que modifica produto, quantidade, valor e situação de pagamento. |
| Alta | A história proíbe excluir consumos pagos; `ConsumoRepository.excluir` não verifica `pago`. O legado em `BoConsumo.excluir` já contém esse bloqueio. |
| Alta | `ConsumoRepositoryTest` altera um consumo, marca-o como pago e espera excluí-lo. O teste reforça a divergência em vez de validar a história original. |
| Alta | A história §1.2.5 prevê quitação dos consumos, pagamento parcial e desconto. A implementação mantém apenas pagamentos; o indicador de pagamento total é informado manualmente. |
| Alta | O documento original pede verificar tabelas, valores e registros básicos ao iniciar. `DatabaseCheck` executa somente `SELECT current_database()`: conectividade não comprova prontidão do esquema. |
| Média | A história exige total parcial calculado a partir de quantidade e valor; o FXML de consumos não apresenta esse campo nem o cálculo. |
| Média | Pesquisa ao perder foco, foco após consulta e próximo código ao limpar não estão integralmente reproduzidos; tabelas embutidas substituem parte das consultas antigas sem decisão documental explícita. |
| Média | O diagnóstico e o plano anteriores precisam incorporar as histórias: existem regras básicas de quitação, embora persistam ambiguidades sobre desconto, edição e estorno. |
| Média | PDFs de contexto ainda não versionados, caminho do relatório alterado e ausência de ADR formal e de matriz requisito–código–teste consolidada. |

Evidências de código:

- [ConsumoRepository](../../src/main/java/br/ucs/dante/repository/ConsumoRepository.java) e [ConsumoController](../../src/main/java/br/ucs/dante/controller/ConsumoController.java).
- [Teste de consumo](../../src/test/java/br/ucs/dante/repository/ConsumoRepositoryTest.java).
- [PagamentoRepository](../../src/main/java/br/ucs/dante/repository/PagamentoRepository.java) e [tela de pagamentos](../../src/main/resources/fxml/pagamentos.fxml).
- [DatabaseCheck](../../src/main/java/br/ucs/dante/repository/DatabaseCheck.java) e [tela de consumos](../../src/main/resources/fxml/consumos.fxml).

Os 30 testes aprovados citados no relatório acadêmico são resultado histórico informado pela equipe. A evidência posterior registra 50 testes com 7 falhas de configuração: são execuções distintas.

A organização em camadas não comprova adesão integral a Clean Architecture/SOLID; parte da coordenação das operações permanece nos controladores.

## 4. Matriz de Lacunas e Próximos Passos Sugeridos

| Prioridade | Lacuna / próxima tarefa | Critério de conclusão |
|---|---|---|
| P0 | Consolidar documentação e precedência das especificações | PDFs versionados, caminhos corrigidos e matriz requisito–implementação–teste |
| P0 | PostgreSQL reproduzível — issue #3 | Esquema, dados básicos, banco exclusivo e suíte completa aprovada |
| P1 | Corrigir consumos | Bloqueio de edição e exclusão de pagos conforme a história; revisão do teste divergente |
| P1 | Verificação inicial | Detectar tabelas e dados básicos ausentes |
| P1 | Quitação — issue #7 | Decisões sobre ambiguidades e operação financeira transacional com rollback testado |
| P1 | Tela de consumos e interação | Total parcial e fluxos de foco/pesquisa validados ou substituição formalmente aceita |
| P2 | Reservas — issue #4 | Grade, reservas/aulas, participantes e conflitos |
| P2 | Sauna — issue #5 | Clientes avulsos, cobrança inicial, consumos, pagamento e restrições por data |
| P2 | Valores e Mensalidades — issue #6 | Vencimentos distintos até dia 28, divisão das cobranças, quatro aulas mensais e multas |
| P2 | Relatórios — issue #8 | Ficha do cliente, fluxo de caixa e consultas com totais conferidos |
| P3 | Administração — issue #9 | Backup/restauração demonstrados com integridade |
| Condicional na auditoria original | Confirmar BDD — issue #10 | Decisão registrada; Gherkin/Cucumber se exigidos |
| Antes da entrega | Validar em outra máquina | Instalação reproduzível, testes e fluxos reais documentados |

**Atualização de contexto de 18/11/2026:** após o relatório acima, o usuário definiu BDD como fundamental para a próxima fase. A condição da linha histórica da issue #10 foi superada por essa orientação; consultar o [diagnóstico de BDD](../bdd/2026-11-18_LACUNA_BDD_E_CONTEXTO.md) e o plano atualizado.

A retomada recomendada é consolidar requisitos e preparar o banco, especificando os comportamentos por BDD antes de corrigir e expandir a base financeira.
