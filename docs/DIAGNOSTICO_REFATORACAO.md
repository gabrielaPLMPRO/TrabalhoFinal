# Diagnóstico e evolução da rotina principal

Data: 17/11/2026.

Este arquivo mantém o diagnóstico inicial e o histórico do processo. As próximas atualizações devem ser curtas e acrescentadas à seção 6, com data, alterações, validação e pendências. Os resultados abaixo retratam a execução de 17/11/2026; novas execuções devem ser registradas separadamente.

## 1. Diagnóstico da refatoração atual

Fontes: código inicial em `C:/Users/guiba/Downloads/6e342d91-3580-4a3f-a999-04e6b3e0904a (1)/src`, código atual deste repositório e `TRABALHO FINAL.pdf` (9 páginas).

O PDF fornecido é um relatório da reconstrução parcial, e não uma especificação completa de todos os módulos. As regras numeradas que ele explicita são RN-01 e RN-02. A seção 4 descreve os quatro módulos implementados; a seção 7.2 declara pendências. Não há regra de distribuição de pagamentos entre consumos, nem critérios completos de reservas, sauna, mensalidades e relatórios.

A pasta inicial contém 105 arquivos. Antes desta entrega, 53 eram idênticos byte a byte aos arquivos correspondentes mantidos em `src`. Os outros 52 diferem em bytes; esse número inclui diferenças de apresentação/codificação e não representa 52 funcionalidades novas. A comparação não depende do histórico Git.

A arquitetura inicial é Swing, com GUI/controladores, BO, VO e DAO. O controlador `CoPrincipal` prepara as telas legadas e a conexão. A reconstrução Maven compila `src/main/java`, com JavaFX, FXML, CSS, JDBC, modelos, repositórios e serviços; os antigos pacotes diretamente em `src` não são compilados pela configuração Maven atual. Assim, uma função presente apenas no legado não está integrada ao aplicativo JavaFX.

Melhorias já existentes: quatro telas CRUD, validações de serviços, consultas parametrizadas, fechamento de recursos JDBC com try-with-resources, transações e locks consultivos para geração de códigos/sequências, datas com java.time, restrições de exclusão e 30 testes JUnit. O código preserva a chave composta cliente/data/sequência dos consumos e pagamentos.

## 2. Funcionalidades pendentes/faltantes

| Referência no PDF | Comportamento | Situação auditada |
|---|---|---|
| §4, Clientes | Cadastrar com código automático ou informado disponível | Implementado no controlador/repositório; integração não confirmada neste ambiente |
| §4, Clientes | Consultar por código e listar | Implementado; integração pendente de PostgreSQL |
| §4, Clientes | Atualizar cliente existente | Implementado; integração pendente de PostgreSQL |
| §4, Clientes / RN-02 | Excluir somente sem registros dependentes | Implementado; bloqueia consumos, pagamentos e também vínculos do legado |
| §4, RN-01 | Nome obrigatório e máximo de 50 caracteres | Implementado e validado por testes de serviço, incluindo os limites 50/51 e espaços |
| §4, RN-02 | Bloquear exclusão com consumos ou pagamentos | Código presente; teste original cobria consumo, novo teste cobre pagamento; execução real depende do banco |
| §4, Produtos | Cadastrar, consultar, atualizar, listar e excluir | Implementado; integração pendente de PostgreSQL |
| §4, Consumos | Cliente/produto, data, sequência, quantidade, valor e pago | Implementado; validações e carregamento da tela verificados |
| §4, Consumos | Manter por chave composta | Implementado nos WHERE e no controle de edição; integração pendente |
| §4, Pagamentos | Cliente/data/sequência, valor e indicador de pagamento total | Implementado; validações e carregamento da tela verificados |
| §4, Pagamentos | Consultar, atualizar, listar e excluir | Implementado; integração pendente |
| §7.2 | Reservas de Squash | Sem implementação JavaFX; botão informa pendência |
| §7.2 | Sauna | Sem implementação JavaFX; botão informa pendência |
| §7.2 | Mensalidades | Sem implementação JavaFX; botão informa pendência |
| §7.2 | Relatórios | Sem implementação JavaFX; botão informa pendência |
| §7.2 | Demais funções do legado | Cadastro de valores, ficha de cliente, fluxo de caixa, resumo mensal, backup/restauração e administração do esquema não migrados para a nova interface |
| §7.2 | Pagamento quitar automaticamente consumos | Não implementado: PagamentoRepository mantém apenas pagamentos; faltam critérios documentados para rateio, período, pagamento parcial e estorno |
| §5.2 / §7.2 | BDD formal | Não há Gherkin/Cucumber; o próprio PDF condiciona sua necessidade à exigência da disciplina |
| §6 / §7.2 | Evidências completas e reprodução em outro ambiente | Logs e XML desta auditoria anexados; execução real de banco e validação em outra máquina continuam pendentes |
| §7.2 | PostgreSQL, criação do banco e variáveis | São pré-requisitos locais; o main não cria banco/tabelas nem usa credenciais presumidas |

O escopo de implementação desta entrega é a rotina principal e a integração dos quatro módulos documentados. Os módulos explicitamente pendentes não foram inventados a partir de nomes de botões. O relatório não deve ser usado para declarar a migração integral concluída.

## 3. Relatório de erros e falhas identificadas via testes

### Diagnóstico reproduzido antes da alteração

- Suíte original: 30 testes, 24 aprovados e 6 falhas por ausência de DANTE_TEST_DB_URL/USER/PASSWORD. São falhas de pré-requisitos de execução, não prova de violação de RN-01 ou RN-02. Evidências em `validacao/antes/maven-test.log` e XML originais.
- `DatabaseConnectionTest`: 6 cenários, 5 falhas antes da correção. O construtor aceitava URL nula, URL vazia, URL de outro tipo de banco, usuário em branco e senha não configurada. São defeitos de validação/configuração relacionados a §1.2 e §7.2; o PDF não atribui uma RN específica a essas condições. Evidência em `validacao/antes/regressoes-inicializacao.log`.
- Uma senha explicitamente vazia já era aceita e continua aceita: o PDF não exige senha não vazia e PostgreSQL pode ter outros mecanismos de autenticação.

### Correções e verificações

| Cenário | Teste | Resultado/limite |
|---|---|---|
| RN-01: vazio, espaços, 50 e 51 caracteres | ClienteServiceTest | Validado sem banco |
| Configuração inválida | DatabaseConnectionTest | Os cinco casos antes falhos passaram após validação |
| Configuração ausente/parcial | InicializacaoTest | Não acessa JDBC; inicia interface com orientação |
| Conexão inválida/sem resposta | InicializacaoTest | Não disponibiliza dependências para CRUD nem expõe o texto sensível do driver |
| Uma verificação inicial compartilhada | InicializacaoTest | Verificador invocado uma vez; conexão/configuração centralizada |
| FXML/CSS ausente ou tela desconhecida | TelaLoaderTest | Erro descritivo; rotas desconhecidas rejeitadas |
| Quatro CRUD sem conexão | TelaLoaderTest | Bloqueados antes de criar controladores |
| Navegação, Configurações e retorno ao início | TelaLoaderTest | FXML real e eventos de botões executados na thread JavaFX |
| Quatro telas com injeção de dependências | TelaLoaderTest | Carregadas com JDBC substituído por consultas vazias; não comprova persistência |
| RN-02 com consumo | ClienteRepositoryTest.deveImpedirExclusaoDeClienteComConsumo | Teste de integração existente; requer banco exclusivo de testes |
| RN-02 com pagamento | ClienteRepositoryTest.deveImpedirExclusaoDeClienteComPagamentoConformeRN02 | Novo cenário; requer banco exclusivo de testes |

A suíte final contém 50 testes: 43 passam sem PostgreSQL e 7 testes de integração falham no pré-requisito de configuração ausente. A compilação de produção e de testes é executada com release 21. A execução usa o JDK 25 já instalado; o teste de interface em classpath emite aviso de JavaFX, registrado no log. Não houve validação visual manual completa nem confirmação das operações CRUD em banco real.

Após a recompilação, a versão refatorada foi iniciada pelo script local. O Windows confirmou o processo Java 24892 com a janela `Dante Sauna & Squash 2.0` e `Responding=True`. Isso comprova a abertura da janela, sem substituir os testes funcionais do banco.

Comando da suíte completa: `mvn clean test`. Evidências em `validacao/depois/maven-clean-test.log` e nos XML dessa pasta. Os testes não são removidos, desabilitados ou convertidos em sucesso para ocultar a falta de banco.

## 4. Código main refatorado e evoluído

O código completo está em `../src/main/java/br/ucs/dante/DanteApp.java`. Depende das classes novas `Inicializacao` e `ui/TelaLoader`, dos controladores atualizados e do FXML principal atualizado; não é um arquivo isolado para copiar sobre uma versão antiga.

`main` delega ao ciclo de vida JavaFX. `init` prepara e verifica a configuração fora da thread gráfica. `start` compõe a cena, aplica CSS, define a janela e apresenta erro de carregamento quando necessário. Sem banco configurado, a tela inicial e as instruções continuam acessíveis, mas as operações CRUD são bloqueadas.

Para iniciar no computador preparado: executar `iniciar.cmd` na raiz. Para operar os módulos, configurar DANTE_DB_URL, DANTE_DB_USER e DANTE_DB_PASSWORD e preparar previamente o esquema compatível com o legado. As variáveis DANTE_TEST_DB_* são independentes e destinam-se somente ao banco `dante_squash_test` para integração.

## 5. Explicação das decisões de refatoração

- Main pequeno: composição da aplicação; regras de nome, valores e vínculos permanecem em serviços/repositórios.
- Inicialização testável: Verificador injetável permite simular indisponibilidade sem rede e sem interface.
- Injeção por construtor: os quatro controladores recebem o mesmo DatabaseConnection, evitando releitura duplicada de ambiente e serviços parcialmente configurados. O objeto configura conexões; não é uma conexão JDBC única compartilhada entre threads.
- Navegação centralizada: TelaLoader registra recursos/controladores e propaga a mesma configuração a telas abertas posteriormente; PrincipalController preserva os componentes iniciais e elimina quatro blocos duplicados de FXMLLoader.
- Timeouts de conexão e leitura JDBC: limitam esperas no caminho comum sem alterar parâmetros globais do DriverManager.
- Estado explícito na tela: conexão ausente/indisponível é visível e não fica apenas no stderr. O main não apresenta conexão falha como módulo operacional.
- Sem exposição de credenciais: falha técnica de inicialização usa mensagem controlada, testada com exceção contendo um segredo fictício.
- Escopo fiel à evidência: sem implementar quitação automática ou critérios novos de módulos ausentes a partir de um relatório que não os define. Essas decisões alterariam regras financeiras e precisam de definição funcional.
- O legado na pasta Downloads e o PDF não foram modificados.

## 6. Relatórios curtos do processo

### 17/11/2026 — Refatoração da inicialização

- **Alterações:** main refatorado; configuração de banco validada e compartilhada entre os quatro módulos; carregamento de telas centralizado; situação da conexão exibida na interface.
- **Validação:** compilação do zero; 50 testes executados, 43 aprovados e 7 falhas por falta da configuração do PostgreSQL de testes. Janela da aplicação iniciada e respondendo.
- **Pendências:** validar CRUD e RN-02 em banco real; definir e implementar os módulos restantes; esclarecer as regras de quitação automática de consumos.
- **Evidências:** [log da suíte](validacao/depois/maven-clean-test.log) e [instruções de validação](validacao/README.md).

### 17/11/2026 — Organização de issues e commits

- **Alterações:** criado [plano de issues e commits](PLANO_ISSUES_COMMITS.md), com 11 issues propostas e três commits coesos para as alterações atuais; logs de evidências liberados no `.gitignore` somente nas pastas previstas.
- **Validação:** estado do Git e arquivos relacionados conferidos; comandos de staging e Conventional Commits documentados. Nenhum commit ou issue remota executado nesta etapa.
- **Pendências:** revisar/executar os commits propostos e publicar as issues quando solicitado; manter a validação PostgreSQL como pendência explícita.

### 17/11/2026 — Publicação das issues e execução dos commits

- **Alterações:** publicadas as issues [#1 a #11](https://github.com/gabrielaPLMPRO/TrabalhoFinal/issues); criado o commit `08ca99b` para a aplicação e `c3c0b7b` para os testes. Este registro integra o terceiro commit, de documentação, conforme o [plano executado](PLANO_ISSUES_COMMITS.md).
- **Validação:** agrupamentos e diffs preparados conferidos; referências #1, #2 e #11 incluídas nos commits correspondentes. Os resultados anteriores dos testes permanecem 43 aprovados e 7 falhas de configuração de banco.
- **Pendências:** enviar os commits locais ao remoto; preparar o PostgreSQL de testes e seguir as issues de evolução. Issues mantidas abertas para acompanhamento.

### Modelo para as próximas atualizações

```markdown
### DD/MM/AAAA — Etapa realizada

- **Alterações:** o que foi implementado ou corrigido.
- **Validação:** verificações executadas e resultados.
- **Pendências:** o que ainda falta ou depende de definição.
- **Evidências:** links para arquivos ou logs, quando houver.
```
