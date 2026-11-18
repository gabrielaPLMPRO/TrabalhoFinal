# Complemento da auditoria: exigências da Entrega 2

- Data da conferência: 18/11/2026, após nova indicação do usuário.
- Fonte: [o que nao pode faltar.pdf](../contexto%20IA/o%20que%20nao%20pode%20faltar.pdf), 4 páginas, lidas integralmente.
- Natureza: atualização documental; não houve implementação nem nova execução dos testes.
- Relacionados: [auditoria original](2026-11-18_AUDITORIA_DOCUMENTACAO.md), [plano BDD](../bdd/2026-11-18_LACUNA_BDD_E_CONTEXTO.md) e [inventário de fontes](../contexto%20IA/README.md).

## O que a nova fonte estabelece

| Referência | Exigência | Impacto no projeto |
|---|---|---|
| p. 1, §1 | Reconstruir a partir dos requisitos e melhorias da Entrega 1, preservando regras e comportamentos | A entrega parcial não comprova atendimento integral; relacionar os artefatos da Entrega 1 ao código atual |
| p. 1, §2 | Implementar e executar testes de fluxo principal, banco, arquitetura de software e interface gráfica | Evidenciar cada categoria, com entradas, condições, resultados e correções; não presumir cobertura só pela existência de classes de teste |
| p. 2, §3 | Implementar cenários BDD da Entrega 1 em Gherkin, associados aos requisitos, com código de execução e resultados | **BDD é obrigatório no enunciado**, além da orientação do usuário; exemplos Markdown e JUnit isolados não satisfazem esse item |
| p. 2, §4 | Não utilizar métodos estáticos em linguagens orientadas a objetos | Há conflito literal com métodos atuais; registrar e resolver o alcance da restrição antes de declarar conformidade |
| pp. 2–3 | Entregar ZIP completo com código, testes, automação BDD, dependências/instruções e README | Preparar pacote reproduzível; o README atual da raiz contém apenas um link e precisa de instruções locais completas |
| p. 3 | Relatório com metodologia, IA, melhorias, requisitos, testes/BDD, resultados, limitações e referências | Atualizar o relatório final com evidências efetivas, distinguindo implementado de validado |
| p. 3 | Vídeo do software funcionando e da execução dos testes e BDD | Registrar execução real; slides e imagens estáticas não são suficientes |
| pp. 3–4 | Justificar mudanças frente à Entrega 1 e declarar ferramentas de IA, etapas e finalidades | Manter rastreabilidade das decisões e responsabilidade da equipe sobre o código e testes |

## Lacunas confirmadas e limites da evidência

- Na inspeção deste complemento, não havia `.feature`, passos BDD ou integração Cucumber. Na etapa posterior de histórias foram criadas as [primeiras especificações Gherkin](../bdd/HISTORIAS_USUARIO_E_CENARIOS.md), ainda sem passos, runner ou execução. O documento exige Gherkin e ferramenta de execução adequada; **não determina especificamente Cucumber**. Cucumber permanece a opção proposta para este projeto Java.
- Os registros históricos continuam em 50 testes, com 43 aprovados e 7 falhas de configuração de banco. Não houve nova execução nesta conferência.
- Existem testes de inicialização/navegação, interface e repositórios; ainda é necessário demonstrar o fluxo principal e a cobertura de arquitetura exigida. Não foi identificada suíte dedicada de conformidade arquitetural; não confundir compilação ou carregamento FXML com comprovação integral dessa categoria.
- Métodos estáticos próprios confirmados: `DanteApp.main`, `TelaLoaderTest.iniciarJavafx`, `TelaLoaderTest.encerrarJavafx` e `TelaLoaderTest.vazio`. `TelaLoader.MODULOS` é um campo estático, não um método, e não deve ser contado como tal. Esta listagem considera o código Maven de produção e testes; não é inventário completo do legado excluído da compilação.
- O enunciado não explicita exceção para o ponto de entrada Java ou para testes. Registrar o conflito do `main`, avaliar uma estratégia de lançamento e esclarecer o critério da disciplina; não apagar o ponto de entrada nem presumir dispensa. Métodos auxiliares e ciclo de vida dos testes devem ser revistos à luz da restrição.
- **Esclarecimento posterior do usuário:** a Entrega 1 é `TRABALHO FINAL.pdf`, já recebido e analisado, e não um arquivo separado chamado `entregafinal.pdf`. Está encerrada a pendência de identificação do documento. O PDF declara ausência de BDD formal e não contém o catálogo de 11 histórias/220 casos criado posteriormente; tratar esses critérios como complementação proposta e justificar a evolução no relatório final, sem atribuí-los à entrega anterior.
- ZIP final, vídeo e validação em outra máquina não tiveram sua conclusão comprovada pelos artefatos de `docs` inspecionados; registrar evidências antes de declarar atendimento.

## Sequência recomendada

1. Relacionar a Entrega 1 identificada (`TRABALHO FINAL.pdf`) a cada item do novo enunciado, registrando complementações e mudanças de estratégia.
2. Especificar e executar BDD, priorizando RN-01/RN-02 e os desvios de consumos e pagamentos já auditados.
3. Preparar PostgreSQL e uma matriz explícita para as quatro categorias obrigatórias de testes.
4. Revisar a restrição de métodos estáticos e documentar a solução/critério aplicável ao lançamento Java.
5. Atualizar README e relatório; preparar e validar o ZIP em outro ambiente; gravar o vídeo com o sistema e as suítes em execução.

O novo PDF amplia a base de obrigações da atividade e substitui a pendência de recebimento. Ele não encerra as lacunas de implementação, nem resolve as ambiguidades financeiras do documento de histórias.
