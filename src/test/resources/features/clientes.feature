# language: pt
@US01 @clientes
Funcionalidade: Manter clientes
  Como atendente
  Eu quero manter os dados dos clientes
  Para identificar os responsáveis por consumos e pagamentos

  Contexto:
    Dado que o sistema está pronto para manter cadastros

  @RN01
  Cenário: Cadastrar cliente com os dados obrigatórios
    Dado que informei o nome "Ana Souza" sem informar um código
    Quando solicito o cadastro do cliente
    Então o cliente deve receber um código positivo disponível
    E deve aparecer na consulta de clientes com o nome informado
    E seus dados opcionais podem permanecer ausentes

  Cenário: Cadastrar com código informado disponível
    Dado que o código de cliente 120 está disponível
    E que informei o nome "Ana Souza" e o código 120
    Quando solicito o cadastro do cliente
    Então o cliente deve ser cadastrado com o código 120

  @RN01
  Esquema do Cenário: Validar o preenchimento e o tamanho do nome
    Dado que os demais dados do cliente são válidos
    E que o nome informado corresponde a "<nome>"
    Quando solicito o cadastro do cliente
    Então o cadastro deve ser "<resultado>"
    E o motivo deve corresponder a "<motivo>"

    Exemplos:
      | nome                   | resultado | motivo                     |
      | ausente                | rejeitado | nome obrigatório           |
      | vazio                  | rejeitado | nome obrigatório           |
      | apenas espaços         | rejeitado | nome obrigatório           |
      | 1 caractere não branco | aceito    | dados válidos              |
      | 50 caracteres válidos  | aceito    | dados válidos              |
      | 51 caracteres válidos  | rejeitado | nome acima de 50 caracteres |

  Esquema do Cenário: Validar dados de identificação
    Dado que informei um cliente com os demais dados válidos
    E que o campo "<campo>" contém "<valor>"
    Quando solicito o cadastro do cliente
    Então o cadastro deve ser "<resultado>"
    E o motivo deve corresponder a "<motivo>"

    Exemplos:
      | campo    | valor          | resultado | motivo                      |
      | código   | 0              | rejeitado | código positivo obrigatório |
      | código   | -1             | rejeitado | código positivo obrigatório |
      | código   | letras         | rejeitado | código inteiro obrigatório  |
      | CPF      | 11 caracteres  | aceito    | dados válidos               |
      | CPF      | 12 caracteres  | rejeitado | CPF acima de 11 caracteres  |
      | telefone | 15 caracteres  | aceito    | dados válidos               |
      | telefone | 16 caracteres  | rejeitado | telefone acima de 15 caracteres |

  Cenário: Rejeitar solicitação sem dados de cliente
    Quando solicito o cadastro sem fornecer dados de cliente
    Então o cadastro deve ser rejeitado por ausência de dados
    E nenhum cliente deve ser criado

  Cenário: Não sobrescrever cliente durante um novo cadastro
    Dado que existe um cliente de código 120 com o nome "Ana Souza"
    Quando tento cadastrar outro cliente com o código 120
    Então o cadastro deve ser rejeitado por código já cadastrado
    E o cliente 120 deve continuar com o nome "Ana Souza"

  Cenário: Atualizar cliente consultado
    Dado que consultei o cliente 120 chamado "Ana Souza"
    Quando altero seu nome para "Ana Lima" e confirmo a atualização
    Então uma nova consulta ao cliente 120 deve mostrar "Ana Lima"
    E nenhum novo cliente deve ser criado

  Cenário: Preservar o código do cliente em edição
    Dado que consultei o cliente 120
    Quando tento alterar seu código para 121
    Então a atualização deve ser rejeitada
    E o cliente deve continuar identificado pelo código 120

  Esquema do Cenário: Consultar clientes
    Dado que a consulta de cliente corresponde a "<situação>"
    Quando realizo a consulta
    Então devo obter "<resultado>"

    Exemplos:
      | situação              | resultado                      |
      | código existente      | os dados do cliente solicitado |
      | código inexistente    | indicação de não encontrado    |
      | código não positivo   | indicação de código inválido   |
      | código não numérico   | indicação de código inválido   |
      | nenhum código         | a relação de clientes comuns   |

  @RN02
  Esquema do Cenário: Impedir exclusão de cliente com vínculos
    Dado que existe um cliente com "<vínculo>" associado
    Quando confirmo sua exclusão
    Então a exclusão deve ser rejeitada por existência de vínculo
    E o cliente e seus registros associados devem permanecer inalterados

    Exemplos:
      | vínculo              |
      | consumo              |
      | pagamento            |
      | participação na sauna |
      | reserva de squash    |

  Esquema do Cenário: Tratar a exclusão de cliente sem vínculos
    Dado que a exclusão corresponde a "<situação>"
    Quando solicito a exclusão do cliente
    Então devo obter "<resultado>"

    Exemplos:
      | situação                           | resultado                            |
      | cliente existente e confirmação    | cliente removido da consulta         |
      | cliente existente e cancelamento   | cliente preservado                   |
      | cliente inexistente e confirmação  | indicação de não encontrado          |
      | código inválido                    | rejeição sem alteração dos cadastros |

  @proposta @interface
  Cenário: Direcionar o atendimento após uma consulta
    Dado que pesquisei um cliente existente
    Quando seus dados são apresentados para edição
    Então a próxima entrada deve ser direcionada ao nome do cliente

  @interface
  Cenário: Limpar os dados para novo atendimento
    Dado que consultei um cliente
    Quando solicito a limpeza dos dados de atendimento
    Então os dados anteriormente consultados devem deixar de estar selecionados
    E a próxima entrada deve ser direcionada ao código

  @proposta @persistencia
  Cenário: Não confirmar cadastro que não foi gravado
    Dado que informei um cliente válido
    E que ocorre uma falha ao gravar seu cadastro
    Quando solicito o cadastro do cliente
    Então devo receber indicação de falha
    E o sistema não deve informar que o cadastro foi concluído
