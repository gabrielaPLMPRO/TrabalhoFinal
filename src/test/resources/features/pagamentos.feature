# language: pt
@US04 @pagamentos
Funcionalidade: Receber pagamentos e quitar consumos
  Como atendente
  Eu quero receber pagamentos vinculados aos consumos do cliente
  Para manter o saldo devido e a quitação consistentes

  Contexto:
    Dado que o sistema está pronto para receber pagamentos
    E que existe um cliente comum cadastrado

  @a_refatorar @persistencia
  Cenário: Quitar integralmente os consumos em aberto
    Dado que o cliente possui consumos em aberto que totalizam 100,00 reais
    E que não há desconto nem novos consumos durante o recebimento
    Quando recebo 100,00 reais desse cliente
    Então deve ser registrado um pagamento total de 100,00 reais
    E os consumos que compõem essa dívida devem ficar pagos
    E o saldo devido desses consumos deve ser de 0,00 reais
    E os consumos de outros clientes devem permanecer inalterados

  @a_refatorar @persistencia
  Cenário: Receber pagamento parcial sem desconto
    Dado que o cliente possui consumos em aberto que totalizam 100,00 reais
    E que não há desconto nem novos consumos durante o recebimento
    Quando recebo 40,00 reais desse cliente
    Então deve ser registrado um pagamento parcial de 40,00 reais
    E os consumos anteriores que compõem essa dívida devem ficar pagos
    E deve ser gerado um novo consumo não pago de saldo devedor de 60,00 reais
    E o saldo devido deve ser de 60,00 reais

  Esquema do Cenário: Rejeitar dados inválidos de pagamento
    Dado que informei um pagamento com os demais dados válidos
    E que o campo "<campo>" contém "<valor>"
    Quando solicito seu registro
    Então o pagamento deve ser rejeitado por "<motivo>"
    E nenhum pagamento deve ser criado
    E os consumos devem permanecer inalterados

    Exemplos:
      | campo   | valor              | motivo                     |
      | cliente | ausente            | cliente válido obrigatório |
      | cliente | 0                  | cliente válido obrigatório |
      | cliente | -1                 | cliente válido obrigatório |
      | cliente | não cadastrado     | cliente não encontrado     |
      | data    | ausente            | data obrigatória           |
      | valor   | ausente            | valor obrigatório          |
      | valor   | texto não numérico | valor inválido             |
      | valor   | valor não finito   | valor inválido             |
      | valor   | -0,01              | valor não negativo obrigatório |

  Cenário: Rejeitar solicitação sem dados de pagamento
    Quando solicito o registro sem fornecer dados de pagamento
    Então o registro deve ser rejeitado por ausência de dados
    E nenhum pagamento deve ser criado

  @caracterizacao @pendente_regra
  Esquema do Cenário: Caracterizar validações do indicador atual
    Dado que informei um pagamento com os demais dados válidos
    E que o campo "<campo>" contém "<valor>"
    Quando submeto os dados à validação atual de pagamentos
    Então a validação deve ser "<resultado>"

    Exemplos:
      | campo           | valor   | resultado |
      | valor           | 0,00    | aceita    |
      | pagamento total | ausente | rejeitada |
      | sequência       | 0       | rejeitada |
      | sequência       | -1      | rejeitada |

  Esquema do Cenário: Atribuir sequência somente ao novo pagamento
    Dado que informei um pagamento válido
    E que a sequência foi informada como "<sequência>"
    Quando solicito seu registro como novo pagamento
    Então o resultado deve ser "<resultado>"

    Exemplos:
      | sequência | resultado                                       |
      | vazia     | pagamento registrado com sequência automática     |
      | 0         | rejeição de sequência informada em novo pagamento |
      | -1        | rejeição de sequência informada em novo pagamento |
      | 7         | rejeição de sequência informada em novo pagamento |

  Esquema do Cenário: Consultar pagamento pela identificação completa
    Dado que a consulta de pagamento corresponde a "<situação>"
    Quando solicito a consulta de pagamento
    Então devo obter "<resultado>"

    Exemplos:
      | situação                  | resultado                      |
      | identificação existente   | o pagamento solicitado         |
      | identificação inexistente | indicação de não encontrado    |
      | cliente ausente           | solicitação de cliente e data  |
      | data ausente              | solicitação de cliente e data  |
      | sequência ausente         | solicitação de sequência       |
      | sequência não positiva    | rejeição de sequência inválida |
      | sequência não numérica    | rejeição de sequência inválida |

  @caracterizacao @pendente_regra
  Esquema do Cenário: Caracterizar manutenção atual de pagamentos
    Dado que existe um pagamento registrado e consultado
    Quando realizo a operação "<operação>" no cadastro atual
    Então devo observar "<resultado>"

    Exemplos:
      | operação                         | resultado                             |
      | alterar o valor e confirmar      | valor atualizado no pagamento         |
      | alterar o indicador e confirmar  | indicador atualizado no pagamento     |
      | alterar cliente, data ou sequência | identificação alterada rejeitada    |
      | confirmar exclusão               | pagamento removido                    |
      | cancelar exclusão                | pagamento preservado                  |
      | excluir sem selecionar           | solicitação de seleção de pagamento   |
      | atualizar registro já removido   | indicação de não encontrado            |
      | excluir registro já removido    | indicação de não encontrado            |

  @proposta @a_refatorar @persistencia
  Cenário: Preservar a situação anterior quando a quitação falha
    Dado que o cliente possui consumos não pagos que totalizam 100,00 reais
    E que ocorre uma falha antes da conclusão da quitação
    Quando tento receber 100,00 reais para esses consumos
    Então o pagamento não deve ser confirmado
    E nenhum pagamento dessa tentativa deve permanecer registrado
    E os consumos devem continuar não pagos
