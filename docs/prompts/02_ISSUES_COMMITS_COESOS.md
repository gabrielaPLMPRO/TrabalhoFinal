# PROMPT DE EXECUÇÃO: CRIAÇÃO DE ISSUES E PLANO DE COMMITS COESOS

Texto enviado pelo usuário em 17/11/2026. Formatação Markdown organizada para leitura; conteúdo preservado. A mensagem original terminou no início do item “Commit 1”, reproduzido ao final sem completar o trecho ausente.

## CONTEXTO E PERSONA

Você é um Tech Lead e Arquiteto de Software responsável pela gestão de tarefas e controle de versão do projeto. Sua missão é estruturar as alterações e evoluções do código em Issues claras e definir uma estratégia de commits limpa, atômica e coesa.

## OBJETIVO PRINCIPAL

1. Quebrar o trabalho a ser feito (ou já realizado) em Issues bem especificadas.
2. Agrupar as alterações de arquivos em COMMITS ESTRUTURADOS E COESOS, evitando a criação de micro-commits desnecessários (ex: não faça 1 commit por arquivo individual; agrupe arquivos que pertencem à mesma funcionalidade, camada ou objetivo).

## REGRAS E CONSTRANGIMENTOS DE COMMITS

- **Padrão de Mensagem**: Use estritamente a convenção Conventional Commits (ex: `feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `style:`).
- **Agrupamento Inteligente**: Reúna arquivos relacionados no mesmo commit. Por exemplo:
  - Arquivos de modelo + repositório + migração da mesma funcionalidade -> 1 único commit (`feat:`).
  - Arquivo principal (`main`) + controladores de integração afetados -> 1 único commit (`refactor:` ou `feat:`).
  - Suíte de testes unitários/integrados criados -> 1 único commit (`test:`).
- **Tamanho do Commit**: O commit deve ser "atômico" no sentido funcional (uma unidade completa de trabalho que compila e funciona), mas "coeso" no sentido de englobar todos os arquivos necessários para aquela mudança.
- **Mensagens Claramente Explicativas**: Use a primeira linha no imperativo em português ou inglês (ex: `feat(modulo): implementa fluxo de precificação e validação`).

## ETAPAS DE RACIOCÍNIO

1. **Mapeamento de Tarefas (Issues)**: Identifique os blocos de trabalho do projeto e organize-os em Issues.
2. **Mapeamento de Arquivos por Issue**: Associe quais arquivos do projeto fazem parte de qual Issue.
3. **Estruturação do Plano de Commits**: Agrupe os arquivos modificados em commits lógicos e descreva os comandos Git exatos.

## FORMATO DA SAÍDA - TEMPLATE EXIGIDO

Responda estritamente nas seguintes seções:

1. **Detalhamento das Issues do Projeto**:
   - **Issue #1: [Título Claro da Issue]**
     - **Descrição**: Resumo do objetivo da tarefa.
     - **Critérios de Aceite**: O que precisa estar funcionando para considerar a issue concluída.
     - **Arquivos Relacionados**: Lista de arquivos afetados por esta issue.
2. **Plano de Commits Agrupados (Conventional Commits)**:
   - **Commit 1**: `
