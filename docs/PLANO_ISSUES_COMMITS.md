# 1. Detalhamento das Issues do Projeto

Data: 17/11/2026. As 11 issues foram publicadas no GitHub com a mesma numeração do plano. A execução dos commits está registrada ao final. Os comandos abaixo documentam o procedimento aprovado e não devem ser repetidos sobre alterações já commitadas.

Referência: [diagnóstico](DIAGNOSTICO_REFATORACAO.md). O PDF descreve uma reconstrução parcial. Regras dos módulos pendentes precisam ser levantadas no legado e confirmadas antes da implementação.

## Issue #1: Centralizar a inicialização e integrar as telas JavaFX

- **Descrição:** compor a aplicação a partir de configuração validada, injetar dependências nos quatro módulos e informar indisponibilidade do banco na interface.
- **Situação:** implementada localmente; compilação e navegação com JDBC substituto verificadas. Persistência real depende da Issue #3.
- **Critérios de aceite:** main e todas as telas compilam juntos; configuração ausente/inválida impede abrir CRUD; início e Configurações continuam acessíveis; credenciais não aparecem na mensagem de inicialização; retorno ao início preserva o conteúdo; conexões têm timeouts.
- **Arquivos relacionados (existentes):**
  - `src/main/java/br/ucs/dante/DanteApp.java`
  - `src/main/java/br/ucs/dante/Inicializacao.java`
  - `src/main/java/br/ucs/dante/ui/TelaLoader.java`
  - `src/main/java/br/ucs/dante/controller/PrincipalController.java`
  - `src/main/java/br/ucs/dante/controller/ClienteController.java`
  - `src/main/java/br/ucs/dante/controller/ProdutoController.java`
  - `src/main/java/br/ucs/dante/controller/ConsumoController.java`
  - `src/main/java/br/ucs/dante/controller/PagamentoController.java`
  - `src/main/java/br/ucs/dante/repository/DatabaseConnection.java`
  - `src/main/java/br/ucs/dante/repository/DatabaseCheck.java`
  - `src/main/resources/fxml/principal.fxml`

## Issue #2: Cobrir inicialização, navegação e limites de RN-01/RN-02

- **Descrição:** registrar regressões de configuração, testar a composição JavaFX e ampliar os cenários de negócio existentes.
- **Situação:** testes escritos; 43 aprovados sem PostgreSQL. Os 7 testes de integração, incluindo RN-02 com pagamento, falham no pré-requisito de configuração ausente.
- **Critérios de aceite:** testes de configuração, falha de conexão, FXML e navegação aprovados; RN-01 cobre vazio/espaços/50/51 caracteres; RN-02 cobre consumo e pagamento; substitutos JDBC são explicitamente diferenciados de persistência real; conclusão da integração exige Issue #3 e XML sem falhas.
- **Arquivos relacionados (existentes):**
  - `src/test/java/br/ucs/dante/InicializacaoTest.java`
  - `src/test/java/br/ucs/dante/repository/DatabaseConnectionTest.java`
  - `src/test/java/br/ucs/dante/ui/TelaLoaderTest.java`
  - `src/test/java/br/ucs/dante/service/ClienteServiceTest.java`
  - `src/test/java/br/ucs/dante/repository/ClienteRepositoryTest.java`
- **Dependência:** código da Issue #1; validação integral depende da Issue #3.

## Issue #3: Preparar PostgreSQL de testes reproduzível

- **Descrição:** fornecer esquema e configuração de testes compatíveis com o legado e permitir reproduzir a suíte em outra máquina.
- **Situação:** pendente.
- **Critérios de aceite:** banco `dante_squash_test` isolado e esquema documentado; DANTE_TEST_DB_* separados da aplicação; CRUD e RN-02 executados contra PostgreSQL; limpeza dos dados de teste; suíte completa aprovada; instruções reproduzidas em outro ambiente; nenhuma credencial real versionada.
- **Arquivos relacionados (existentes):** `src/dao/bd/DaoEsquema.java` como referência; `src/test/java/br/ucs/dante/repository/*RepositoryTest.java`; `docs/validacao/README.md`; `pom.xml` se for necessário configurar uma estratégia de integração.
- **Arquivos propostos:** `sql/schema-test.sql`, `.env.example` sem segredos e `docs/POSTGRESQL_TESTES.md`. São propostas; ainda não existem.

## Issue #4: Migrar reservas de Squash para JavaFX

- **Descrição:** levantar regras de horários, quadras, jogos/aulas e vínculos a clientes no legado e implementar a navegação funcional.
- **Critérios de aceite:** regras documentadas e confirmadas; operações de reservas persistidas; conflitos tratados conforme as regras confirmadas; tela substitui o aviso de pendência; testes de serviço, repositório e navegação aprovados.
- **Arquivos relacionados (referências existentes):** `src/model/bo/servicos/BoSquash.java`, `BoHorarioSquash.java`, `src/dao/servicos/DaoSquash.java`, `DaoHorariosSquash.java`, `src/main/java/br/ucs/dante/controller/PrincipalController.java` e `ui/TelaLoader.java`.
- **Arquivos propostos:** modelo, serviço, repositório e controlador de reservas em `src/main/java/br/ucs/dante/`; `src/main/resources/fxml/reservas.fxml`; testes correspondentes.
- **Dependência:** Issue #3 para validação real.

## Issue #5: Migrar o controle de Sauna para JavaFX

- **Descrição:** reconstruir utilização da sauna e seus vínculos com clientes e consumos.
- **Critérios de aceite:** regras do legado documentadas e confirmadas; operações persistidas sem quebrar a chave composta de consumos; vínculos e restrições de exclusão testados; menu abre a tela implementada.
- **Arquivos relacionados (referências existentes):** `src/model/bo/servicos/BoSauna.java`, `src/dao/servicos/DaoConsumoSauna.java`, `src/model/vo/servicos/VoConsumoSauna.java`, `src/main/java/br/ucs/dante/repository/ConsumoRepository.java`, `controller/PrincipalController.java` e `ui/TelaLoader.java`.
- **Arquivos propostos:** classes do módulo Sauna nas quatro camadas, `src/main/resources/fxml/sauna.fxml` e testes correspondentes.
- **Dependência:** Issue #3.

## Issue #6: Migrar valores e geração de mensalidades

- **Descrição:** levantar os valores, vencimentos e critérios de geração existentes e reconstruir o fluxo de mensalidades.
- **Critérios de aceite:** critérios e limites financeiros confirmados; valores configuráveis; geração, repetição e vencimentos tratados conforme as regras confirmadas; persistência e testes aprovados; menu deixa de exibir apenas aviso.
- **Arquivos relacionados (referências existentes):** `src/model/bo/servicos/BoMensalidade.java`, `src/dao/servicos/DaoMensalidade.java`, `src/model/bo/cadastros/BoValores.java`, `src/dao/cadastros/DaoValores.java`, controlador principal e TelaLoader.
- **Arquivos propostos:** classes de Valores/Mensalidades nas quatro camadas, FXML e testes correspondentes.
- **Dependência:** levantamento da Issue #4 e ambiente da Issue #3.

## Issue #7: Definir e implementar quitação de consumos por pagamentos

- **Descrição:** especificar a integração ainda não confirmada no PDF e implementar as atualizações financeiras de forma transacional.
- **Critérios de aceite:** decisão registrada sobre pagamento parcial/total, período, rateio, edição, exclusão e estorno; gravação de pagamento e quitação consistentes; rollback em falhas; testes de valores, vínculos e concorrência pertinentes aprovados. Não adotar rateio ou estorno presumidos.
- **Arquivos relacionados (existentes):** `src/main/java/br/ucs/dante/service/PagamentoService.java`, `repository/PagamentoRepository.java`, `repository/ConsumoRepository.java`, `controller/PagamentoController.java` e testes de pagamentos/consumos.
- **Arquivo proposto:** `docs/REGRAS_QUITACAO.md` e testes transacionais adicionais.
- **Dependência:** definição funcional e Issue #3.

## Issue #8: Migrar relatórios e consultas gerenciais

- **Descrição:** reconstruir ficha de cliente, fluxo de caixa e resumo mensal a partir das consultas legadas.
- **Critérios de aceite:** filtros, períodos e fórmulas documentados; totais confrontados com dados conhecidos do banco; critérios de pagamentos/consumos consistentes com a Issue #7; telas de consulta funcionais e testes aprovados.
- **Arquivos relacionados (referências existentes):** `src/model/bo/servicos/BoFluxoCaixa.java`, `src/dao/servicos/DaoFluxoCaixa.java`, `src/gui/controller/servicos/CoFichaCliente.java`, `CoResumoMensal.java`, controlador principal e TelaLoader.
- **Arquivos propostos:** serviços/repositórios de consultas, controladores e FXML de relatórios, testes correspondentes.
- **Dependências:** Issues #3 e #7; #4–#6 quando os relatórios incluírem esses módulos.

## Issue #9: Migrar backup, restauração e administração do esquema

- **Descrição:** levantar e disponibilizar as operações administrativas ainda exclusivas do legado, com comportamento documentado.
- **Critérios de aceite:** formatos e compatibilidade definidos; backup e restauração demonstrados em banco de teste; erros tratados; operações administrativas e seus efeitos explícitos ao usuário; teste de restauração verifica integridade e relacionamentos.
- **Arquivos relacionados (referências existentes):** `src/dao/bd/DaoBackupTabelas.java`, `DaoRestaurarTabelas.java`, `DaoEsquema.java`, `src/model/bo/bd/BoBackupTabelas.java`, `BoRestaurarTabelas.java` e controlador principal.
- **Arquivos propostos:** serviços/controladores administrativos JavaFX, telas e testes próprios.
- **Dependência:** Issue #3 e definição dos efeitos administrativos.

## Issue #10: Confirmar exigência e implementar BDD formal

- **Descrição:** confirmar com a disciplina se BDD formal é obrigatório; em caso positivo, integrar Gherkin à execução automatizada.
- **Critérios de aceite:** exigência registrada; se obrigatória, cenários RN-01/RN-02 executáveis e integrados ao Maven; implementação ligada a código real; evidências geradas. Se dispensada, registrar a decisão sem declarar BDD implementado.
- **Arquivos relacionados (existentes):** `pom.xml` e testes atuais de clientes.
- **Arquivos propostos:** `src/test/resources/features/clientes.feature`, definições de passos e configuração de execução.
- **Dependência:** decisão da disciplina; Issue #3 para cenários de persistência.

## Issue #11: Versionar diagnóstico, evidências e histórico resumido

- **Descrição:** consolidar o estado auditado, os resultados completos e o plano de evolução em documentos rastreáveis.
- **Situação:** documentação criada localmente.
- **Critérios de aceite:** diagnóstico distingue implementado de pendente; logs/XML antes e depois disponíveis; resultados não são apresentados como aprovação integral; histórico possui registros curtos; política de ignore preserva ferramentas locais e permite somente os logs de evidência previstos.
- **Arquivos relacionados:** `.gitignore`, `docs/DIAGNOSTICO_REFATORACAO.md`, `docs/PLANO_ISSUES_COMMITS.md`, `docs/validacao/README.md`, logs e XML em `docs/validacao/antes/` e `docs/validacao/depois/`.
- **Dependências:** evidências das Issues #1 e #2.

# 2. Plano de Commits Agrupados (Conventional Commits)

Os três commits seguintes cobrem somente o trabalho desta entrega. Os módulos futuros não entram nesses commits. `iniciar.cmd`, `iniciar.ps1` e o PDF já estão versionados no commit `8eb5bbe` e não precisam ser adicionados novamente. O histórico anterior foi preservado.

Os comandos são para PowerShell na raiz do projeto. O índice estava vazio no levantamento. Antes de começar, conferir `git status --short` e `git diff --cached --name-only`; arquivos previamente preparados por outra pessoa precisam ser analisados antes de continuar.

## Commit 1: `refactor(app): centralize a inicializacao e a integracao das telas`

- **Issue:** #1.
- **Escopo:** todos os 11 arquivos de produção listados na Issue #1, mantendo main, fábrica de controladores, controladores afetados e FXML juntos.
- **Atomicidade:** nenhum construtor é alterado sem incluir o código que o instancia. A validação JDBC integra a mesma mudança de composição.
- **Validação:** `mvn -DskipTests compile`; inicialização e navegação da aplicação. O teste de integração sem ambiente não é critério de aprovação dessa compilação.

```powershell
git add -- src/main/java/br/ucs/dante src/main/resources/fxml/principal.fxml
git diff --cached --check
git diff --cached --stat
git commit -m "refactor(app): centralize a inicializacao e a integracao das telas"
```

O diretório de produção é adicionado inteiro porque, no estado inspecionado, ele contém exatamente as mudanças dessa unidade funcional. Conferir o diff antes de executar caso surjam novas alterações.

## Commit 2: `test(app): cubra inicializacao, navegacao e regras de clientes`

- **Issue:** #2.
- **Escopo:** os cinco arquivos de teste listados na Issue #2.
- **Atomicidade:** reunir a suíte da refatoração e os cenários RN-01/RN-02, sem commits individuais por classe.
- **Dependência:** Commit 1.
- **Validação comprovada:** 43 testes independentes de PostgreSQL aprovados; suíte completa com 50 testes e 7 falhas de configuração de banco. Não encerrar a validação real de RN-02 antes da Issue #3.

```powershell
git add -- src/test/java/br/ucs/dante/InicializacaoTest.java src/test/java/br/ucs/dante/repository/DatabaseConnectionTest.java src/test/java/br/ucs/dante/ui/TelaLoaderTest.java src/test/java/br/ucs/dante/service/ClienteServiceTest.java src/test/java/br/ucs/dante/repository/ClienteRepositoryTest.java
git diff --cached --check
git diff --cached --stat
git commit -m "test(app): cubra inicializacao, navegacao e regras de clientes"
```

## Commit 3: `docs(processo): registre diagnostico, evidencias e plano de evolucao`

- **Issue:** #11.
- **Escopo:** diagnóstico, plano, instruções, logs/XML e `.gitignore`.
- **Atomicidade:** documentação e política de versionamento de seus artefatos entram juntas. `.tools/` continua local; apenas logs das pastas de evidências previstas deixam de ser ignorados.
- **Dependência:** Commits 1 e 2 para que a documentação represente o código e os testes correspondentes.
- **Validação:** links relativos existentes, totais conferidos nos XML, logs aparecendo como arquivos adicionáveis e ferramentas locais permanecendo ignoradas.

```powershell
git add -- .gitignore docs/
git diff --cached --check
git diff --cached --stat
git commit -m "docs(processo): registre diagnostico, evidencias e plano de evolucao"
git status --short
```

## Registro de execução

As issues foram publicadas em `gabrielaPLMPRO/TrabalhoFinal` usando a autenticação GitHub local já configurada, após o conector retornar 403. Nenhuma credencial foi gravada nos artefatos. As issues permanecem abertas para acompanhamento; os commits são locais e não houve push nesta etapa.

| Issue | Link |
|---|---|
| #1 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/1 |
| #2 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/2 |
| #3 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/3 |
| #4 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/4 |
| #5 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/5 |
| #6 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/6 |
| #7 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/7 |
| #8 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/8 |
| #9 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/9 |
| #10 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/10 |
| #11 | https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues/11 |

- `08ca99b`: refatoração da aplicação; corpo do commit referencia #1.
- `c3c0b7b`: suíte de testes; corpo do commit referencia #2.
- O terceiro commit, `docs(processo): registre diagnostico, evidencias e plano de evolucao`, contém este registro e referencia #11. Seu hash é consultável com `git log -3 --oneline`.

Validação preservada: 43 testes independentes de PostgreSQL aprovados e 7 falhas de pré-requisito de banco na suíte completa. A publicação de issues e o versionamento não alteram esse resultado.

## Sequência para as próximas issues

| Issue | Unidade de commit futura | Mensagem proposta |
|---|---|---|
| #3 | Esquema de testes + configuração + instruções | `test(database): prepare um ambiente PostgreSQL reproduzivel` |
| #4 | Modelo + repositório + serviço + controlador + FXML + registro da tela + testes | `feat(reservas): implemente o fluxo de reservas de squash` |
| #5 | Módulo Sauna completo e sua integração/testes | `feat(sauna): implemente o controle de utilizacao e consumos` |
| #6 | Valores + fluxo de mensalidades + persistência + interface/testes | `feat(mensalidades): implemente valores e geracao de cobrancas` |
| #7 | Regras aprovadas + serviço transacional + repositórios afetados + testes | `feat(pagamentos): integre pagamentos a quitacao de consumos` |
| #8 | Consultas + cálculos + telas + testes de relatórios | `feat(relatorios): implemente consultas e resumos gerenciais` |
| #9 | Serviços administrativos + interface + testes de restauração | `feat(administracao): implemente backup e restauracao do banco` |
| #10 | Features + passos + execução Maven, somente se exigido | `test(bdd): integre cenarios executaveis de clientes` |

Esses são agrupamentos propostos, não comandos de staging para arquivos ainda inexistentes. Se uma issue crescer, dividir por fluxo funcional completo que compile, persista e possa ser verificado; não dividir por arquivo ou deixar uma tela com construtor incompatível entre commits.
