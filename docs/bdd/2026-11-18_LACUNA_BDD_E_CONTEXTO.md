# Consolidação de artefatos, diagnóstico de BDD e expansão de contexto

## Metadados

- Data: 18/11/2026.
- Origem: [prompt desta etapa](../prompts/04_ORGANIZACAO_ARTEFATOS_LACUNA_BDD_CONTEXTO.md).
- Base: [auditoria consolidada](../auditorias/2026-11-18_AUDITORIA_DOCUMENTACAO.md), código, testes, `pom.xml` e [fontes recebidas](../contexto%20IA/README.md).
- Status atualizado: documentação organizada; novo PDF recebido e lido; [histórias e arquivos Gherkin criados](HISTORIAS_USUARIO_E_CENARIOS.md) na etapa seguinte. Passos, runner e execução BDD continuam pendentes.
- Decisão de escopo: o usuário definiu BDD como fundamental nesta etapa, superando a condição anterior de apenas confirmar sua exigência na disciplina.
- Limite da entrega: não foram implementados cenários executáveis nem alterados código, dependências ou testes; não houve nova execução da suíte.

## 1. Estrutura de Armazenamento Intuitiva (Relatório e Prompt)

### Registro do Prompt Utilizado

Os prompts completos, formatados com metadados, instruções e template de saída, foram preservados separadamente:

- [03 — Auditoria de documentação, status e coerência](../prompts/03_AUDITORIA_DOCUMENTACAO_STATUS_COERENCIA.md).
- [04 — Organização de artefatos, lacuna BDD e contexto](../prompts/04_ORGANIZACAO_ARTEFATOS_LACUNA_BDD_CONTEXTO.md).

Trecho do objetivo metodológico do prompt 04:

> Registrar formalmente que a revisão do relatório evidenciou a falta da abordagem de BDD (Behavior-Driven Development), identificada como fundamental para a especificação executável e validação das regras de negócio do trabalho.

Navegação principal: [índice de docs](../README.md). O [relatório de auditoria](../auditorias/2026-11-18_AUDITORIA_DOCUMENTACAO.md) preserva as quatro seções solicitadas e distingue o retrato histórico das orientações posteriores.

### Resumo do Relatório Integrado

- Última implementação: inicialização/navegação JavaFX e injeção da configuração nos quatro módulos, commit `08ca99b`; testes ampliados em `c3c0b7b`.
- Últimos commits documentais inspecionados: `cf9fafc` e `cdafa64`, de 17/11/2026.
- Entregue: Clientes, Produtos, Consumos e Pagamentos na arquitetura JavaFX/FXML/CSS, serviços, repositórios e JDBC.
- Alinhamento documental: parcial. O relatório acadêmico descreve uma entrega parcial; o documento de histórias contém regras adicionais que não foram integralmente preservadas.
- Desvios principais: edição de consumos, exclusão de consumos pagos, ausência de quitação por pagamentos, falta de total parcial e verificação inicial limitada à conexão.
- Evidência histórica mais recente: 50 testes, 43 aprovados e 7 falhas por configuração ausente de PostgreSQL. Não é uma suíte integralmente aprovada nem uma execução desta etapa.
- Pendências: integração real com banco, módulos ainda legados e resolução das divergências de requisitos e testes.

## 2. Relatório de Diagnóstico de Lacunas (Identificação da Falta do BDD)

### Apontamento Crítico

**Diagnóstico inicial:** não havia arquivos `.feature`, definições de passos ou integração Cucumber no `pom.xml`. Os testes existentes eram JUnit 5 de serviço, repositório, inicialização e interface. Sua existência não comprova descoberta colaborativa de comportamentos nem uma especificação executável compartilhada.

**Atualização após o prompt de histórias:** foram criadas 11 histórias locais e arquivos `.feature` em `src/test/resources/features`, com fontes, tags e decisões abertas no [catálogo](HISTORIAS_USUARIO_E_CENARIOS.md). A lacuna de especificação foi parcialmente tratada; sem passos, runner ou execução, não há ainda suíte BDD operacional nem comprovação de aceite.

BDD envolve discutir regras e exemplos, formulá-los em linguagem de negócio e automatizar comportamentos acordados. Apenas renomear testes para Dado/Quando/Então ou escrever Gherkin sem executá-lo não fecha a lacuna. A ausência de Gherkin, isoladamente, tampouco prova que nenhuma conversa sobre comportamentos ocorreu; o diagnóstico é sobre o processo e as evidências disponíveis.

| Regra / fonte | Situação atual | Impacto na paridade | Próximo cenário / decisão |
|---|---|---|---|
| RN-01: nome obrigatório, máximo 50 | Testes JUnit existentes, incluindo espaços e 50/51 caracteres | Cobertura técnica sem vínculo a uma especificação compartilhada executável | Exemplos de aceitação ligados a `ClienteService.validar` |
| RN-02: cliente com consumo/pagamento não pode ser excluído | Código e testes de integração escritos; execução depende do banco | Validação real continua sem evidência de aprovação | Cenários separados para cada vínculo, com preservação do cliente |
| Histórias §1.2.4: consumo não pode ser alterado | Repositório e teste permitem alteração | Teste confirma comportamento contrário ao documento | Bloquear edição dos dados originais; distinguir quitação financeira autorizada de edição manual |
| Histórias §1.2.4: consumo pago não pode ser excluído | Repositório não verifica `pago`; teste espera exclusão após marcar como pago | Mesmo uma suíte verde poderia aprovar o desvio | Rejeitar exclusão e comprovar permanência do registro |
| Histórias §1.2.5: pagamento quita consumos | Pagamento é mantido isoladamente | Persistência do pagamento não comprova liquidação da dívida | Exemplos de pagamento total, parcial, desconto e falha transacional |
| Verificação inicial do documento original | Somente conexão verificada | Banco acessível pode não ter esquema/dados necessários | Banco sem tabelas ou dados básicos não deve ser tratado como pronto |

Referências de implementação: [serviços](../../src/main/java/br/ucs/dante/service/ClienteService.java), [consumos](../../src/main/java/br/ucs/dante/repository/ConsumoRepository.java), [pagamentos](../../src/main/java/br/ucs/dante/repository/PagamentoRepository.java), [teste divergente](../../src/test/java/br/ucs/dante/repository/ConsumoRepositoryTest.java) e [configuração Maven](../../pom.xml).

### Justificativa da Necessidade

Neste trabalho, BDD é necessário pela orientação explícita do usuário, pela divergência concreta entre histórias, código e testes e pela obrigatoriedade confirmada na página 2, §3, do [enunciado da Entrega 2](../contexto%20IA/o%20que%20nao%20pode%20faltar.pdf). A equipe precisa acordar o comportamento esperado antes de usar a automação como critério de sucesso da refatoração.

O objetivo é tornar verificáveis os critérios de negócio, permitir revisão pela equipe e revelar quando uma implementação ou um teste contradiz a regra. BDD não garante correção por si só: exemplos errados também podem ser automatizados. Os testes JUnit existentes devem continuar como suporte técnico, e a integração PostgreSQL continua indispensável para verificar SQL, vínculos e transações.

Exemplo documental proposto, **ainda não executável e não contabilizado como teste implementado**:

```gherkin
# language: pt
Funcionalidade: Preservar consumos pagos
  Cenário: Impedir exclusão de consumo pago
    Dado que existe um consumo pago de um cliente
    Quando solicito a exclusão desse consumo
    Então a exclusão deve ser rejeitada
    E o consumo deve permanecer registrado como pago
```

Não presumir valores ou fórmulas de desconto e saldo. O documento de histórias oferece o comportamento básico, mas suas ambiguidades e os efeitos de edição, exclusão e estorno de pagamentos precisam de decisão registrada.

## 3. Plano de Atualização de Base de Conhecimento (Inclusão do Novo PDF)

### Registro de Novo Material

**Recebido e lido:** [o que nao pode faltar.pdf](../contexto%20IA/o%20que%20nao%20pode%20faltar.pdf), 4 páginas. A reconferência solicitada pelo usuário localizou o terceiro PDF. Esta atualização supera o registro inicial de recebimento pendente.

O nome original foi preservado em `docs/contexto IA/`. Trata-se do enunciado da Entrega 2: exige Gherkin executável, vínculo a requisitos e resultados (p. 2, §3), testes de fluxo principal, banco, arquitetura e interface (p. 1, §2), além de ZIP/README, relatório, vídeo e declaração de IA (pp. 2–4). Também proíbe métodos estáticos (p. 2, §4). Metadados no [inventário](../contexto%20IA/README.md) e impactos no [complemento da auditoria](../auditorias/2026-11-18_COMPLEMENTO_ENTREGA_2.md).

O plano combina a solicitação do usuário, a auditoria do repositório e as exigências agora conferidas. Cucumber/JUnit Platform é uma escolha técnica proposta; o enunciado exige ferramenta adequada, sem prescrever Cucumber.

### Orientações para Próximos Passos

1. **Fontes incorporadas:** leitura e metadados concluídos; usar o mapa de páginas acima. O usuário confirmou que a Entrega 1 é `TRABALHO FINAL.pdf`, que declara ausência de BDD formal. Relacionar os testes descritos e justificar a nova especificação BDD como complementação da estratégia; o PDF de histórias é apoio e não substitui a Entrega 1.
2. **Descobrir e acordar comportamentos:** revisar as histórias com a equipe; registrar regras, exemplos, dúvidas e decisões, começando por consumos e pagamentos. Separar divergência de implementação de mudança de requisito aprovada.
3. **Criar rastreabilidade:** identificar cenários por regra/história e ligá-los a serviços, repositórios e testes existentes. RN-01/RN-02 são identificadores do relatório atual; demais identificadores novos devem ser declarados como locais, sem inventar numeração oficial.
4. **Especificar em Gherkin:** propor `src/test/resources/features/` para Clientes, Consumos e Pagamentos, com linguagem de negócio, exemplos positivos, negativos e limites. Definir resultados financeiros antes de automatizá-los.
5. **Integrar a execução:** adicionar Cucumber/JUnit Platform e definições de passos ao Maven, escolhendo versões compatíveis na implementação. Conectar passos a código real; não validar apenas mocks que repetem o resultado esperado. A ausência de cenários descobertos não deve produzir uma aprovação aparente da suíte BDD.
6. **Preparar PostgreSQL:** executar cenários de persistência em `dante_squash_test`, com esquema, dados básicos e limpeza reproduzíveis, separados do banco da aplicação. JDBC vazio continua restrito à validação de navegação.
7. **Refatorar orientado pelos comportamentos acordados:** registrar falhas relevantes antes da correção; ajustar implementação e o teste divergente de consumo; manter a suíte técnica JUnit. Fazer a quitação em transação e testar rollback.
8. **Publicar evidências locais de validação:** registrar comando, data, revisão de código, cenários descobertos/executados, aprovados/falhos e limitações; atualizar o histórico e o plano. Não somar exemplos Markdown aos 50 testes históricos.

### Critérios de conclusão da etapa BDD

- Regras e exemplos revisados pela equipe, com decisões sobre conflitos documentadas.
- Features executáveis ligadas ao código real e descobertas pela execução Maven.
- RN-01, RN-02 e restrições de consumo cobertas; cenários financeiros definidos e implementados conforme as decisões.
- Persistência e rollback comprovados no PostgreSQL de testes; suíte técnica preservada.
- Evidências distinguem claramente o que foi implementado, executado, aprovado e o que continua pendente.
- Recebimento/leitura do PDF registrado (concluído); exigências da Entrega 2 e artefatos da Entrega 1 ligados ao plano de validação.

## Registro de progresso desta entrega

- **Concluído:** índice de documentação, relatório consolidado, dois prompts preservados, diagnóstico de BDD e inventário de contexto; histórico e plano atualizados.
- **Verificação:** caminhos e links locais conferidos após o salvamento; nenhuma nova evidência de execução funcional produzida.
- **Atualização após reconferência:** terceiro PDF recebido e lido; inventário, plano e histórico atualizados com as exigências da Entrega 2. A pendência inicial de recebimento foi encerrada.
- **Atualização de especificação:** histórias e primeiras features criadas; ver [catálogo](HISTORIAS_USUARIO_E_CENARIOS.md). Não foram executadas como testes nem identificadas como cenários originais da Entrega 1.
- **Pendente:** revisão dos exemplos com a equipe, decisões abertas, implementação de passos/runner, execução das features, PostgreSQL, correções de código e demais critérios da Entrega 2 mapeados no complemento da auditoria.
