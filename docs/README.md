# Documentação do Dante Sauna & Squash

Atualizado em: 18/11/2026.

## Por onde começar

1. [Auditoria de documentação, status e coerência](auditorias/2026-11-18_AUDITORIA_DOCUMENTACAO.md): ponto de parada, entregas, divergências e prioridades.
2. [Consolidação de artefatos, lacuna BDD e expansão de contexto](bdd/2026-11-18_LACUNA_BDD_E_CONTEXTO.md): orientação da próxima etapa e integração do PDF recebido.
3. [Fontes de contexto](contexto%20IA/README.md): três PDFs recebidos, seus papéis e metadados.
4. [Plano de issues e commits](PLANO_ISSUES_COMMITS.md): tarefas existentes e atualização da prioridade de BDD.
5. [Evidências de validação](validacao/README.md): como interpretar e reproduzir os testes.
6. [Complemento da auditoria: exigências da Entrega 2](auditorias/2026-11-18_COMPLEMENTO_ENTREGA_2.md): BDD obrigatório, categorias de testes, métodos estáticos e materiais finais.
7. [Histórias de usuário e cenários Gherkin](bdd/HISTORIAS_USUARIO_E_CENARIOS.md): 11 histórias locais, critérios por funcionalidade, rastreabilidade e decisões abertas. Arquivos `.feature` em `src/test/resources/features`, ainda sem passos ou execução BDD.
8. [Correções e justificativas perante a Entrega 1](CORRECOES_JUSTIFICATIVAS_ENTREGA_1.md): registro CJ-01 a CJ-13, com base documental, mudança, justificativa, situação e critérios de validação.

## Histórico e instruções

**Fontes identificadas pelo usuário:** `contexto IA/TRABALHO FINAL.pdf` é a Entrega 1; `contexto IA/o que nao pode faltar.pdf` é o enunciado da entrega atual. O documento de histórias do legado é fonte de apoio. Consultar o [inventário](contexto%20IA/README.md) para interpretar seu papel e registrar mudanças de escopo/estratégia.

- [Diagnóstico e histórico do processo](DIAGNOSTICO_REFATORACAO.md): registros de 17/11 e complemento de 18/11. As conclusões antigas devem ser lidas no contexto das fontes disponíveis à época.
- [Prompts de execução](prompts/README.md): solicitações preservadas; instruções históricas não representam tarefas já executadas nem substituem decisões posteriores.

## Convenções

- `auditorias/`: relatórios datados; preservar resultados históricos e registrar novas verificações separadamente.
- `bdd/`: diagnóstico metodológico e plano de especificação executável; não confundir exemplos documentais com cenários automatizados.
- `contexto IA/`: fontes recebidas e seu inventário. Material anunciado só passa a recebido após conferência do arquivo.
- `prompts/`: origem das solicitações, metadados e instruções de execução.
- `validacao/`: logs/XML históricos, com limites de evidência explícitos.

BDD foi definido como necessário pela orientação de 18/11/2026 e confirmado como obrigatório pelo enunciado da Entrega 2, recebido em `contexto IA/o que nao pode faltar.pdf`. A leitura e a primeira especificação de histórias/features foram concluídas; definições de passos, execução BDD e integração PostgreSQL continuam pendentes.
