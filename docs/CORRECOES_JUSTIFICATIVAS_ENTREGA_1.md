# Correções e justificativas perante a Entrega 1

## Metadados

- Data: 18/11/2026.
- Projeto: Dante Sauna & Squash.
- Objetivo: relacionar correções, complementações e mudanças propostas à base da Entrega 1, explicando por que são necessárias e como comprovar sua conclusão.
- Entrega 1 identificada pelo usuário: [TRABALHO FINAL.pdf](contexto%20IA/TRABALHO%20FINAL.pdf), 9 páginas.
- Exigências da entrega atual: [o que nao pode faltar.pdf](contexto%20IA/o%20que%20nao%20pode%20faltar.pdf), 4 páginas.
- Fonte funcional de apoio: [histórias do sistema original](contexto%20IA/6eac3a3f-4194-48c2-a3ab-9c3033dc1b16.pdf), 24 páginas.
- Evidências: [auditoria](auditorias/2026-11-18_AUDITORIA_DOCUMENTACAO.md), [complemento da Entrega 2](auditorias/2026-11-18_COMPLEMENTO_ENTREGA_2.md), [histórias e Gherkin](bdd/HISTORIAS_USUARIO_E_CENARIOS.md), [logs/XML](validacao/README.md) e histórico Git.
- Natureza: registro documental. A criação deste arquivo não implementa correções nem produz novos resultados de testes.
- Numeração CJ-01 a CJ-13: identificadores locais deste registro, sem equivalência presumida à numeração da Entrega 1.

## 1. Base de comparação e critérios de interpretação

A Entrega 1 descreve a reconstrução parcial de Clientes, Produtos, Consumos e Pagamentos, com JavaFX/FXML/CSS, JDBC/PostgreSQL, Maven e camadas de controladores, modelos, serviços e repositórios. RN-01 e RN-02 são regras explicitamente identificadas no relatório. Sua seção 7.2 reconhece módulos e validações ainda pendentes.

O enunciado da entrega atual exige continuidade dessa base. Na página 3, estabelece que mudanças de requisitos ou de estratégias propostas na Entrega 1 devem ser justificadas no relatório técnico. Este documento fornece essa rastreabilidade, mas não substitui o relatório final nem aprova mudanças de regra em nome da equipe.

Aplicar as seguintes distinções:

- **Correção técnica:** trata erro ou fragilidade sem mudar o resultado de negócio esperado.
- **Complementação de validação:** acrescenta critérios, testes ou evidências antes ausentes.
- **Mudança funcional:** altera comportamento ou requisito; exige decisão explícita e justificativa no relatório.
- **Evolução de escopo:** implementa funcionalidade antes pendente; não deve ser apresentada como correção já entregue.

O documento do legado detalha regras, mas não substitui a Entrega 1 nem torna todo módulo legado automaticamente parte do escopo obrigatório. Divergências precisam ser conciliadas e registradas.

## 2. Quadro de correções e situação atual

| ID | Correção / complementação | Prioridade | Situação na data do registro |
|---|---|---|---|
| CJ-01 | Centralizar inicialização e navegação | Base existente | Implementada; persistência real ainda não comprovada |
| CJ-02 | Ampliar validação de RN-01 e RN-02 | Alta | Testes ampliados; integração RN-02 pendente |
| CJ-03 | Distinguir resultados históricos e evidências atuais | Alta | Distinção documentada; suíte completa aprovada pendente |
| CJ-04 | Completar a estratégia BDD | Alta | Histórias/features criadas; passos e execução pendentes |
| CJ-05 | Conciliar regras de manutenção de consumos | Alta | Divergência identificada; decisão/refatoração pendentes |
| CJ-06 | Integrar pagamentos à quitação de consumos | Alta | Critérios parciais propostos; decisões e implementação pendentes |
| CJ-07 | Verificar prontidão do esquema e dados básicos | Alta | Pendente |
| CJ-08 | Completar cálculos e interação da interface | Média | Especificação parcial; implementação e validação pendentes |
| CJ-09 | Comprovar as quatro categorias de testes exigidas | Alta | Cobertura parcial; execução/evidências completas pendentes |
| CJ-10 | Tratar a restrição a métodos estáticos | Alta | Conflito identificado; solução pendente |
| CJ-11 | Confirmar e controlar a evolução de escopo | Antes de ampliar módulos | Catálogo proposto; escopo final ainda precisa ser consolidado |
| CJ-12 | Tornar configuração e distribuição reproduzíveis | Alta | Scripts existentes; banco/README/pacote final pendentes |
| CJ-13 | Atualizar relatório, demonstração e declaração de IA | Antes da entrega final | Documentação de processo existente; materiais finais pendentes |

## 3. Detalhamento das correções e justificativas

### CJ-01 — Inicialização, dependências e navegação

- **Base da Entrega 1:** §§1.2 e 3, pp. 2–4, definem JavaFX e organização em camadas; §7.1, p. 7, registra problemas de carregamento e retorno à tela inicial.
- **Mudança realizada:** `DanteApp` compõe a aplicação; `Inicializacao` prepara a configuração; `TelaLoader` centraliza telas e injeção de dependências. A ausência de conexão fica visível e impede acesso aos cadastros.
- **Justificativa:** fortalecer a composição e reduzir duplicação e falhas de navegação, preservando as regras dos quatro módulos. É uma evolução técnica coerente com a arquitetura declarada, sem substituir os cadastros por funções novas.
- **Evidência:** commit `08ca99b`; testes ampliados em `c3c0b7b`; resultados preservados em `docs/validacao`.
- **Limite:** configuração compartilhada não significa conexão JDBC permanente; o teste com JDBC vazio comprova composição/navegação, não persistência real.

### CJ-02 — Validação e proteção de clientes

- **Base da Entrega 1:** §4, p. 4, define RN-01 (nome obrigatório, até 50 caracteres) e RN-02 (não excluir cliente com consumos ou pagamentos).
- **Mudança realizada/proposta:** acrescentados exemplos de nome em branco e nos limites 50/51, além do teste de vínculo com pagamento; criados cenários correspondentes na US-01. Conservar a proteção existente para vínculos de sauna e reservas.
- **Justificativa:** ampliar a demonstração das regras já declaradas, sem substituir nome obrigatório por uma regra diferente nem confundir ausência de banco com falha de negócio.
- **Situação:** validações de serviço têm evidência de aprovação; RN-02 depende da integração PostgreSQL ainda não aprovada nesta auditoria.
- **Aceite:** demonstrar os limites e, para cada vínculo, rejeitar a exclusão mantendo cliente e dados associados; cliente sem vínculos deve poder ser excluído após confirmação.

### CJ-03 — Resultados de testes e qualidade das evidências

- **Base da Entrega 1:** §6, pp. 6–7, apresenta 30 testes aprovados como resultado reportado, com limitação expressa da evidência; §7.2, p. 8, pede confirmação dos relatórios completos.
- **Correção documental:** separar esse resultado histórico da execução posterior de 50 testes, com 43 aprovados e 7 falhas por configuração ausente do PostgreSQL de testes.
- **Justificativa:** as execuções têm escopos e condições diferentes. Não apagar a informação histórica, afirmar que o resultado anterior era falso ou apresentar 43/50 como aprovação integral.
- **Aceite:** nova execução completa com data, revisão de código, comando e pré-requisitos registrados; logs e XML coerentes; indicação explícita de aprovados, falhos e pendentes. Não desabilitar casos para produzir uma aprovação aparente.

### CJ-04 — BDD como complementação necessária da estratégia

- **Base da Entrega 1:** §5.2, p. 6, declara ausência de BDD formal e prevê sua inclusão se exigido; §7.2, p. 8, mantém essa lacuna.
- **Exigência atual:** enunciado, p. 2, §3, exige cenários em Gherkin ligados aos requisitos, código de automação, execução e apresentação dos resultados.
- **Mudança de estratégia:** complementar o JUnit com especificações de comportamento e execução BDD. Foram criadas 11 histórias locais, 11 features, 88 cenários/esquemas e 220 casos após expansão dos exemplos.
- **Justificativa:** atender à exigência agora confirmada e permitir comparação entre regra de negócio e resultado observável. Os cenários são uma contribuição posterior, não conteúdo já presente na Entrega 1.
- **Situação:** especificação inicial concluída; revisão de negócio, decisões abertas, definições de passos, runner e execução pendentes. As contagens incluem propostas e caracterizações, não casos aprovados.
- **Aceite:** cenários relevantes revisados, descobertos e executados contra código real; persistência validada em banco de teste; resultados publicados sem somar a especificação aos testes já executados. Cucumber é opção proposta, não ferramenta imposta pelo enunciado.

### CJ-05 — Conciliação das regras de consumos

- **Base da Entrega 1:** §4, p. 5, descreve registro e manutenção por chave composta, sem detalhar todas as permissões de edição/exclusão.
- **Fonte adicional:** histórias do legado §1.2.4, p. 5, proíbem alterar um consumo e excluir um consumo já pago.
- **Divergência observada:** `ConsumoRepository` permite atualização e exclusão sem verificar `pago`; `ConsumoRepositoryTest` altera o consumo, marca-o como pago e espera excluí-lo.
- **Correção proposta:** após registrar o entendimento funcional, impedir edição manual de registros e exclusão de consumos pagos, ajustando o teste divergente. A quitação por pagamento deve ser um fluxo autorizado distinto da edição manual.
- **Justificativa:** preservar o comportamento original conforme solicitado pelo enunciado e evitar que a automação valide um desvio. Não basta alterar a documentação para coincidir com o teste atual.
- **Aceite:** demonstrar rejeição sem alteração dos dados, exclusão de consumo não pago elegível e preservação da identificação cliente/data/sequência. Os cenários da US-03 registram o comportamento proposto; o código ainda não foi corrigido nesta etapa.

### CJ-06 — Pagamentos com efeitos financeiros consistentes

- **Base da Entrega 1:** §4, p. 5, descreve cadastro, consulta, atualização e exclusão de pagamentos; §7.2, p. 8, declara não confirmada a integração com quitação automática.
- **Fonte adicional:** histórias §1.2.5, p. 7, descrevem quitação de consumos, recebimento parcial e desconto.
- **Mudança proposta:** estabelecer o contrato financeiro e implementar pagamento/quitação de forma transacional. Os cenários iniciais cobrem quitação integral e recebimento parcial sem desconto.
- **Justificativa:** fechar uma limitação reconhecida, evitando pagamento confirmado com consumos em estado incompatível. As regras básicas existem, mas desconto, excedente, período, concorrência, edição e estorno não devem receber decisões presumidas.
- **Cuidado perante a Entrega 1:** atualização e exclusão de pagamentos são funcionalidades expressamente declaradas. Não removê-las silenciosamente como se fossem apenas defeitos técnicos. Registrar como seus efeitos financeiros funcionarão e justificar qualquer restrição ou substituição no relatório.
- **Aceite:** demonstrar saldo correto, isolamento entre clientes e consistência em falha; assegurar que uma tentativa incompleta não deixe pagamento ou quitação parciais. Casos financeiros não definidos continuam pendentes.

### CJ-07 — Prontidão do banco além da conectividade

- **Base da Entrega 1:** §§1.2 e 7.2, pp. 2–3 e 8, estabelecem PostgreSQL e a dependência da configuração local e criação do banco.
- **Fonte adicional:** histórias, p. 24, exigem verificar tabelas, valores e registros básicos ao iniciar.
- **Lacuna:** `DatabaseCheck` consulta apenas o nome do banco; um banco acessível com esquema incompleto pode ser apresentado como conectado e falhar durante o atendimento.
- **Correção proposta:** verificar os pré-requisitos definidos para os módulos liberados e orientar sobre pendências. Não presumir autorização para criação, alteração ou exclusão automática de dados.
- **Justificativa:** tornar operacional a configuração prevista na Entrega 1 e evitar confirmação de prontidão baseada somente em conexão.
- **Aceite:** banco completo permite atendimento; ausência de tabela ou dado básico impede operações dependentes com orientação clara, sem expor credenciais.

### CJ-08 — Cálculos e fluxos de interface

- **Base da Entrega 1:** §§3 e 7.1, pp. 3–4 e 7, descrevem melhoria da interface, separação de responsabilidades e correções de navegação.
- **Fonte adicional:** histórias §1.2.4 e testes de foco, pp. 5 e 18–21, descrevem total parcial e direcionamento do atendimento.
- **Correção proposta:** apresentar total parcial quantidade × valor unitário e revisar foco, consulta e limpeza. O retorno ao início já foi tratado pela refatoração da navegação.
- **Justificativa:** tornar verificáveis comportamentos funcionais da interface, sem confundir novo desenho visual com preservação de todas as ações antigas.
- **Decisão necessária:** definir se o campo financeiro persistido é unitário ou total e sua compatibilidade com os dados existentes; não alterar silenciosamente cálculos e relatórios. Mudanças de interação devem ser documentadas.
- **Aceite:** total exibido coerente, retorno ao início funcional e fluxo de atendimento validado; testes não devem depender de seletores ou detalhes técnicos na descrição Gherkin.

### CJ-09 — Cobertura de fluxo, banco, arquitetura e interface

- **Base da Entrega 1:** §5.1, pp. 5–6, relaciona testes de serviços e repositórios; §6, pp. 6–7, relata validação manual da interface.
- **Exigência atual:** enunciado, p. 1, §2, exige testes de fluxo principal, banco, arquitetura e interface gráfica.
- **Complementação proposta:** construir uma matriz que identifique requisito, categoria, teste, dados, resultado esperado e evidência. Os testes atuais são parte dessa base, não comprovação automática de todas as categorias.
- **Justificativa:** explicitar como cada exigência é verificada. Compilar ou carregar FXML não equivale a testar toda a arquitetura ou todos os fluxos da interface.
- **Aceite:** evidenciar as quatro categorias, incluindo restrições arquiteturais pertinentes à estrutura declarada e banco real de teste para SQL, chaves e transações; corrigir os problemas detectados e repetir os casos afetados.

### CJ-10 — Restrição de métodos estáticos

- **Base da Entrega 1:** §1.2 e §3, pp. 2–4, adotam Java e organização orientada a objetos; não apresentam proibição explícita de métodos estáticos.
- **Exigência atual:** enunciado, p. 2, §4, proíbe métodos estáticos em linguagens orientadas a objetos.
- **Conflito identificado:** `DanteApp.main` e métodos de preparação, encerramento e apoio em `TelaLoaderTest` são estáticos. Um campo estático constante não deve ser contado como método.
- **Correção a definir:** rever métodos auxiliares e ciclo de vida dos testes; avaliar a estratégia de lançamento Java e registrar o critério aplicável ao ponto de entrada. O enunciado não registra exceção para `main` nem para testes.
- **Justificativa:** adequação a uma restrição expressa da entrega atual. Não declarar dispensa por conveniência, remover o ponto de entrada sem alternativa ou afirmar conformidade antes de resolver o conflito.
- **Aceite:** inventário do escopo entregue, solução documentada e compilação/execução comprovadas após os ajustes. Eventual exceção precisa de fundamento explícito, não de presunção da equipe.

### CJ-11 — Evolução de escopo e módulos pendentes

- **Base da Entrega 1:** identificação/escopo, p. 2, delimita a reconstrução parcial; §7.2, p. 8, registra Reservas, Sauna, Mensalidades, Relatórios e demais funcionalidades para continuidade.
- **Ajuste de planejamento:** tratar as US-06 a US-11 como evolução conhecida/proposta, diferenciando o que está citado na Entrega 1 do detalhamento extraído do legado e das propostas administrativas.
- **Justificativa:** continuidade não equivale a aprovar automaticamente cada hipótese ou módulo listado no novo catálogo. O enunciado exige preservar os requisitos e justificar alterações; é necessário consolidar o escopo final e suas limitações.
- **Aceite:** matriz de escopo por história, fonte, decisão e evidência. Para módulos assumidos na entrega final, substituir avisos de pendência por fluxos funcionais testados; para limitações remanescentes, declará-las sem afirmar que isso elimina uma obrigação do enunciado.

### CJ-12 — Configuração, README e reprodução em outra máquina

- **Base da Entrega 1:** §7.2, p. 8, reconhece a dependência de PostgreSQL, banco, variáveis e validação por outro integrante/ambiente.
- **Exigência atual:** enunciado, pp. 2–3, exige ZIP com código, testes, BDD, dependências ou instruções e README de configuração, compilação, execução e testes.
- **Mudança proposta:** fornecer esquema/dados de teste reproduzíveis, instruções completas, ambiente de testes separado e pacote final verificável. `iniciar.cmd` e `iniciar.ps1` já existem; o README da raiz ainda não cobre esse procedimento completo.
- **Justificativa:** converter uma limitação assumida em procedimento reproduzível. Não depender apenas das ferramentas locais de um computador nem versionar credenciais reais.
- **Aceite:** outro ambiente consegue configurar, compilar, executar e testar com o material entregue; banco exclusivo de testes e limpeza documentados; o ZIP contém tudo que as instruções exigem.

### CJ-13 — Relatório final, demonstração e uso de IA

- **Base da Entrega 1:** §2, p. 3, identifica apoio do ChatGPT; §§6–7, pp. 6–8, relatam resultados e limitações.
- **Exigência atual:** enunciado, pp. 3–4, exige relatório técnico, vídeo do sistema e dos testes/BDD em execução e declaração das ferramentas de IA com etapas e finalidades. A equipe deve compreender e validar o que entrega.
- **Complementação proposta:** atualizar o relatório com o estado efetivamente entregue, justificar as mudanças deste registro e incluir evidências reais. Preparar vídeo com execução do aplicativo e das suítes, não apenas imagens ou slides.
- **Justificativa:** demonstrar a continuidade e a autoria responsável, sem apresentar código gerado, cenários escritos ou afirmações históricas como prova de funcionamento atual.
- **Aceite:** relatório com os oito tópicos exigidos, declaração de IA coerente com o processo e vídeo demonstrando os resultados. Identificar claramente funções não implementadas, testes não executados e decisões ainda abertas.

## 4. Elementos que devem ser preservados

- Arquitetura JavaFX/FXML/CSS e JDBC/PostgreSQL descrita na Entrega 1, salvo mudança explicitamente justificada.
- Organização em controladores, modelos, serviços e repositórios, com regras de negócio fora do ponto de entrada.
- RN-01 e RN-02, dados de clientes/produtos e chaves compostas de consumos/pagamentos.
- Integridade dos vínculos e distinção entre cadastros comuns e registros internos do legado.
- Histórico da Entrega 1 e das execuções anteriores, identificado por data/contexto.
- Testes técnicos existentes que continuam válidos; ajustar os que contrariem a regra funcional acordada, sem excluir cobertura para esconder falhas.

## 5. Evidências necessárias para encerrar uma correção

Para atualizar qualquer item CJ, registrar:

1. Fonte e comportamento anterior, distinguindo falha técnica de mudança funcional.
2. Decisão tomada e justificativa, incluindo dúvidas resolvidas e eventuais impactos no escopo.
3. Arquivos alterados e commit quando houver.
4. Requisito/história/cenário correspondente e teste técnico pertinente.
5. Comando, ambiente, data e resultado da execução; apontar logs/XML ou outros artefatos existentes.
6. Limitações e trabalho restante; marcar concluído apenas quando os critérios do item estiverem comprovados.

Modelo de atualização:

```markdown
### DD/MM/AAAA — CJ-XX: título

- **Decisão e justificativa perante a Entrega 1:** ...
- **Alterações efetivamente realizadas:** ...
- **Arquivos/commit:** ...
- **Validação e evidências:** ...
- **Situação e pendências:** ...
```

## 6. Sequência recomendada

1. Consolidar escopo e decisões funcionais, principalmente consumos, pagamentos e a restrição a métodos estáticos.
2. Preparar PostgreSQL de testes e mapear as quatro categorias de validação.
3. Implementar passos/runner BDD e executar os cenários prioritários; corrigir os desvios com evidência.
4. Implementar os módulos de evolução efetivamente assumidos e validar os fluxos integrados.
5. Reproduzir a instalação em outro ambiente e concluir README, ZIP, relatório, vídeo e declaração de IA.

**Situação deste registro:** as correções técnicas já existentes possuem referência histórica; as demais estão justificadas e rastreadas, mas permanecem propostas ou pendentes. Não há nova aprovação de testes produzida pela criação deste documento.
