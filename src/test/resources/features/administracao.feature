# language: pt
@US11 @administracao @a_desenvolver @proposta @pendente_regra
Funcionalidade: Recuperar os dados a partir de uma cópia de segurança
  Como responsável pela manutenção
  Eu quero gerar e restaurar cópias de segurança
  Para recuperar os dados do estabelecimento quando necessário

  Cenário: Gerar cópia de segurança conforme o formato acordado
    Dado que existem dados consistentes no ambiente selecionado
    E que o formato e o escopo da cópia foram definidos pela equipe
    Quando solicito uma cópia de segurança
    Então devo obter uma cópia identificável dos dados desse escopo
    E devo receber confirmação somente após sua conclusão

  Cenário: Restaurar cópia válida em ambiente de verificação
    Dado que existe uma cópia de segurança válida com dados conhecidos
    E que o destino e os efeitos da restauração foram explicitamente definidos
    Quando restauro essa cópia no ambiente de verificação
    Então os dados restaurados devem corresponder à cópia
    E seus relacionamentos devem permanecer íntegros

  Cenário: Não informar sucesso quando a restauração falhar
    Dado que a cópia selecionada não pode ser restaurada
    Quando solicito sua restauração no ambiente de verificação
    Então devo receber indicação de falha
    E não deve ser apresentada confirmação de restauração concluída
