# language: pt
@US06 @valores @a_desenvolver
Funcionalidade: Configurar valores e vencimentos
  Como gestor
  Eu quero configurar os preços dos serviços e os vencimentos
  Para aplicar as cobranças previstas para sauna e squash

  Cenário: Registrar os valores obrigatórios
    Dado que ainda não existe configuração de valores
    Quando informo os valores de sauna, aula e quadra com primeiro vencimento no dia 10
    Então a configuração deve ser gravada
    E deve ser apresentada quando eu consultar os valores novamente

  Cenário: Atualizar a configuração existente
    Dado que já existe uma configuração de valores
    Quando altero um dos valores e confirmo a atualização
    Então a configuração existente deve ser atualizada
    E deve continuar existindo apenas uma configuração de valores

  Esquema do Cenário: Validar dias de vencimento
    Dado que os demais valores e vencimentos informados são válidos
    Quando informo "<vencimentos>"
    Então a configuração deve ser "<resultado>"

    Exemplos:
      | vencimentos                       | resultado |
      | primeiro vencimento ausente        | rejeitada |
      | primeiro vencimento no dia 28      | aceita    |
      | primeiro vencimento no dia 29      | rejeitada |
      | dois vencimentos no mesmo dia      | rejeitada |
      | três vencimentos distintos até 28 | aceita    |

  Esquema do Cenário: Exigir valores básicos
    Dado que os demais dados de configuração são válidos
    Quando tento salvar sem informar "<campo>"
    Então a configuração deve ser rejeitada
    E a ausência desse campo deve ser indicada

    Exemplos:
      | campo         |
      | valor da sauna |
      | valor da aula |
      | valor da quadra |
