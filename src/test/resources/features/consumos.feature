# language: pt
@US03 @consumos
Funcionalidade: Registrar e preservar consumos
  Como atendente
  Eu quero registrar consumos e preservar seu histórico
  Para manter uma cobrança fiel aos itens consumidos

  Contexto:
    Dado que o sistema está pronto para registrar consumos
    E que existe um cliente comum e um produto comum cadastrados

  @a_refatorar
  Cenário: Registrar consumo com total parcial
    Dado que a data de atendimento é 18/11/2026
    E que o cliente consumiu 2 unidades de um produto ao valor unitário de 10,00 reais
    Quando registro o consumo como não pago
    Então o consumo deve ser registrado na data de atendimento
    E deve receber uma sequência disponível para esse cliente e essa data
    E o total parcial apresentado deve ser de 20,00 reais
    E o consumo deve permanecer não pago

  Cenário: Registrar consumo já pago
    Dado que informei um consumo válido
    Quando registro o consumo como pago
    Então sua situação consultada deve ser pago

  Esquema do Cenário: Rejeitar dados inválidos de consumo
    Dado que informei um consumo com os demais dados válidos
    E que o campo "<campo>" contém "<valor>"
    Quando solicito o registro do consumo
    Então o registro deve ser rejeitado por "<motivo>"
    E nenhum novo consumo deve ser criado

    Exemplos:
      | campo      | valor              | motivo                            |
      | cliente    | ausente            | cliente válido obrigatório        |
      | cliente    | 0                  | cliente válido obrigatório        |
      | cliente    | -1                 | cliente válido obrigatório        |
      | cliente    | não cadastrado     | cliente não encontrado            |
      | produto    | ausente            | produto válido obrigatório        |
      | produto    | 0                  | produto válido obrigatório        |
      | produto    | -1                 | produto válido obrigatório        |
      | produto    | não cadastrado     | produto não encontrado            |
      | data       | ausente            | data obrigatória                  |
      | quantidade | ausente            | quantidade positiva obrigatória   |
      | quantidade | 0                  | quantidade positiva obrigatória   |
      | quantidade | -1                 | quantidade positiva obrigatória   |
      | quantidade | 1,5                | quantidade inteira obrigatória    |
      | valor      | ausente            | valor obrigatório                 |
      | valor      | texto não numérico | valor inválido                    |
      | valor      | valor não finito   | valor inválido                    |
      | valor      | -0,01              | valor não negativo obrigatório    |
      | situação   | ausente            | situação de pagamento obrigatória |

  Cenário: Rejeitar solicitação sem dados de consumo
    Quando solicito o registro sem fornecer dados de consumo
    Então o registro deve ser rejeitado por ausência de dados
    E nenhum consumo deve ser criado

  Cenário: Permitir consumo com valor zero
    Dado que informei um consumo válido com valor de 0,00 reais
    Quando solicito seu registro
    Então o consumo deve ser registrado com valor de 0,00 reais

  Esquema do Cenário: Preservar a geração de sequência para novo consumo
    Dado que informei um consumo válido
    E que a sequência foi informada como "<sequência>"
    Quando solicito seu registro como novo consumo
    Então o resultado deve ser "<resultado>"

    Exemplos:
      | sequência | resultado                                      |
      | vazia     | consumo registrado com sequência automática     |
      | 0         | rejeição de sequência informada em novo consumo |
      | -1        | rejeição de sequência informada em novo consumo |
      | 7         | rejeição de sequência informada em novo consumo |

  @persistencia
  Cenário: Distinguir consumos do mesmo cliente na mesma data
    Dado que um cliente possui um consumo na data de atendimento
    Quando registro outro consumo válido para o mesmo cliente nessa data
    Então os dois consumos devem ser consultáveis separadamente
    E suas sequências devem ser diferentes

  Esquema do Cenário: Consultar um consumo pela identificação completa
    Dado que a consulta de consumo corresponde a "<situação>"
    Quando solicito a consulta de consumo
    Então devo obter "<resultado>"

    Exemplos:
      | situação                  | resultado                     |
      | identificação existente   | o consumo solicitado          |
      | identificação inexistente | indicação de não encontrado   |
      | cliente ausente           | solicitação de cliente e data |
      | data ausente              | solicitação de cliente e data |
      | sequência ausente         | solicitação de sequência      |
      | sequência zero            | rejeição de sequência inválida |
      | sequência negativa        | rejeição de sequência inválida |
      | sequência não numérica    | rejeição de sequência inválida |

  @a_refatorar
  Esquema do Cenário: Impedir edição manual de um consumo registrado
    Dado que existe um consumo registrado como "<situação>"
    Quando tento alterar manualmente seu "<dado>"
    Então a alteração deve ser rejeitada
    E o consumo deve conservar todos os dados anteriores

    Exemplos:
      | situação | dado       |
      | não pago | produto    |
      | não pago | quantidade |
      | não pago | valor      |
      | não pago | cliente    |
      | não pago | data       |
      | não pago | sequência  |
      | não pago | situação   |
      | pago     | valor      |
      | pago     | situação   |

  @a_refatorar
  Cenário: Impedir exclusão de consumo pago
    Dado que existe um consumo pago
    Quando confirmo a exclusão desse consumo
    Então a exclusão deve ser rejeitada por consumo já pago
    E o consumo deve permanecer registrado como pago

  Cenário: Excluir consumo não pago sem vínculos de sauna
    Dado que existe um consumo não pago sem vínculos de sauna
    Quando confirmo a exclusão desse consumo
    Então ele não deve mais ser encontrado na consulta

  Esquema do Cenário: Tratar impedimentos de exclusão
    Dado que a exclusão de consumo corresponde a "<situação>"
    Quando solicito a exclusão
    Então devo obter "<resultado>"

    Exemplos:
      | situação                              | resultado                                 |
      | nenhum consumo selecionado            | solicitação de seleção de consumo         |
      | exclusão cancelada                    | consumo preservado                        |
      | consumo não encontrado na confirmação | indicação de não encontrado                |
      | consumo associado à conta da sauna    | rejeição por vínculo com a conta da sauna  |
