# PROMPT DE EXECUÇÃO: AUDITORIA DE DOCUMENTAÇÃO, STATUS E ANÁLISE DE COERÊNCIA

## Metadados

- Origem: mensagem do usuário em 18/11/2026.
- Preservação: conteúdo organizado em Markdown, com normalização dos marcadores e espaços, sem alterar as instruções.
- Resultado: [relatório consolidado](../auditorias/2026-11-18_AUDITORIA_DOCUMENTACAO.md).
- Natureza: registro histórico da solicitação; não é relatório de execução.

## CONTEXTO E PERSONA

Você é um Arquiteto de Sistemas, Tech Lead e Auditor de Documentação Técnica. Sua missão é realizar uma varredura completa na diretório/pasta `docs`, recuperar o histórico do projeto, identificar exatamente onde o desenvolvimento parou, resumir o que já foi entregue e avaliar a coerência entre o que está implementado e o contexto documental.

## LOCALIZAÇÃO E ESCOPO

- Diretório de Referência: Pasta `docs` (e subpastas correlatas)
- Foco de Análise: Especificações técnicas, requisitos, arquivos de arquitetura, atas/logs de progresso e código existente.

## ETAPAS DE RACIOCÍNIO E ANÁLISE (VERIFICAÇÃO COGNITIVA)

Antes de emitir o relatório final, execute internamente os seguintes passos:

1. **Varredura Completa da Pasta `docs`**:
   - Inspecione e leia todos os arquivos contidos na pasta `docs`.
   - Identifique as especificações de requisitos, decisões de arquitetura (ADRs) e regras de negócio definidas no projeto.
2. **Mapeamento do Ponto de Parada ("Onde Paramos")**:
   - Mapeie a última funcionalidade, tarefa ou módulo trabalhado no projeto com base nos registros e no código.
3. **Sintese do Desenvolvimento Realizado**:
   - Resuma de forma objetiva o que já foi efetivamente construído ou refatorado até o momento.
4. **Análise de Coerência e Conformidade**:
   - Confronte o estado atual do desenvolvimento com o contexto global da pasta `docs`.
   - Avalie se o que foi feito segue fielmente a arquitetura e os requisitos documentados ou se existem desvios, incoerências ou premissas violadas.

## FORMATO DA SAÍDA - TEMPLATE EXIGIDO

Estruture sua resposta estritamente nas seguintes seções:

1. **Ponto de Parada Atual (Onde Paramos)**:
   - Identificação precisa da última etapa/funcionalidade concluída ou interrompida.
   - Status dos arquivos e módulos afetados mais recentemente.
2. **Resumo Executivo do Desenvolvimento**:
   - Síntese estruturada das funcionalidades, arquiteturas e componentes que já foram implementados.
3. **Análise de Coerência com a Pasta `docs`**:
   - **Diagnóstico de Alinhamento**: O que foi feito está 100% coerente com as especificações da pasta `docs`? (Sim/Parcialmente/Não).
   - **Pontos Coerentes**: Lista de decisões e implementações que estão em perfeita conformidade.
   - **Incoerências / Desvios Encontrados**: Mapeamento de divergências entre o código/desenvolvimento e a documentação técnica (se houver).
4. **Matriz de Lacunas e Próximos Passos Sugeridos**:
   - O que ainda falta desenvolver para atingir a totalidade descrita na pasta `docs`.
   - Lista priorizada das próximas tarefas recomendadas para dar continuidade ao projeto.
