# language: pt
@US02 @produtos
Funcionalidade: Manter produtos
  Como atendente
  Eu quero manter os produtos e seus preços
  Para registrar corretamente os itens consumidos

  Contexto:
    Dado que o sistema está pronto para manter cadastros

  Cenário: Cadastrar produto com código automático
    Dado que informei descrição "Água", unidade "UN" e preço de 5,00 reais
    E que não informei um código de produto
    Quando solicito o cadastro do produto
    Então o produto deve receber um código positivo disponível
    E deve aparecer nas consultas com os dados informados

  Cenário: Cadastrar com código de produto disponível
    Dado que o código de produto 50 está disponível
    Quando cadastro o produto "Água" com código 50, unidade "UN" e preço de 5,00 reais
    Então devo poder consultar o produto pelo código 50

  Esquema do Cenário: Validar campos do produto comum
    Dado que informei um produto com os demais dados válidos
    E que o campo "<campo>" contém "<valor>"
    Quando solicito o cadastro do produto
    Então o cadastro deve ser "<resultado>"
    E o motivo deve corresponder a "<motivo>"

    Exemplos:
      | campo     | valor                 | resultado | motivo                         |
      | código    | 0                     | rejeitado | código positivo obrigatório    |
      | código    | -1                    | rejeitado | código positivo obrigatório    |
      | código    | letras                | rejeitado | código inteiro obrigatório     |
      | descrição | ausente               | rejeitado | descrição obrigatória          |
      | descrição | vazio                 | rejeitado | descrição obrigatória          |
      | descrição | apenas espaços        | rejeitado | descrição obrigatória          |
      | descrição | 100 caracteres válidos | aceito   | dados válidos                  |
      | descrição | 101 caracteres válidos | rejeitado | descrição acima de 100 caracteres |
      | unidade   | ausente               | rejeitado | unidade obrigatória            |
      | unidade   | vazio                 | rejeitado | unidade obrigatória            |
      | unidade   | apenas espaços        | rejeitado | unidade obrigatória            |
      | unidade   | 100 caracteres válidos | aceito   | dados válidos                  |
      | unidade   | 101 caracteres válidos | rejeitado | unidade acima de 100 caracteres |
      | preço     | ausente               | rejeitado | preço obrigatório              |
      | preço     | texto não numérico    | rejeitado | preço inválido                 |
      | preço     | valor não finito      | rejeitado | preço inválido                 |
      | preço     | -0,01                 | rejeitado | preço não negativo obrigatório |
      | preço     | 0,00                  | aceito    | dados válidos                  |

  Cenário: Rejeitar solicitação sem dados de produto
    Quando solicito o cadastro sem fornecer dados de produto
    Então o cadastro deve ser rejeitado por ausência de dados
    E nenhum produto deve ser criado

  Esquema do Cenário: Aceitar os separadores decimais documentados
    Dado que informei um produto válido com preço escrito como "<preço>"
    Quando solicito o cadastro do produto
    Então o preço registrado deve ser de 12,50 reais

    Exemplos:
      | preço |
      | 12,50 |
      | 12.50 |

  Cenário: Não sobrescrever produto em novo cadastro
    Dado que o produto 50 já está cadastrado
    Quando tento realizar um novo cadastro com o código 50
    Então o novo cadastro deve ser rejeitado
    E os dados anteriores devem ser preservados

  Cenário: Atualizar um produto selecionado
    Dado que selecionei o produto 50 com preço de 5,00 reais
    Quando altero seu preço para 6,00 reais e confirmo a atualização
    Então uma nova consulta ao produto 50 deve apresentar preço de 6,00 reais

  Cenário: Preservar a identificação do produto em edição
    Dado que selecionei o produto 50
    Quando tento alterar seu código para 51
    Então a atualização deve ser rejeitada
    E seu código deve continuar sendo 50

  Esquema do Cenário: Consultar produtos
    Dado que a consulta de produto corresponde a "<situação>"
    Quando realizo a consulta de produto
    Então devo obter "<resultado>"

    Exemplos:
      | situação           | resultado                         |
      | código existente   | os dados do produto solicitado    |
      | código inexistente | indicação de não encontrado       |
      | código inválido    | indicação de código inválido      |
      | nenhum código      | a relação dos produtos cadastrados |

  Cenário: Impedir exclusão de produto com consumo
    Dado que existe um consumo associado ao produto 50
    Quando confirmo a exclusão do produto 50
    Então a exclusão deve ser rejeitada por existência de consumo
    E o produto e o consumo devem permanecer registrados

  Esquema do Cenário: Excluir produto sem consumo associado
    Dado que a exclusão de produto corresponde a "<situação>"
    Quando solicito a exclusão do produto
    Então devo obter "<resultado>"

    Exemplos:
      | situação                          | resultado                  |
      | produto existente e confirmação   | produto removido           |
      | produto existente e cancelamento  | produto preservado         |
      | produto inexistente e confirmação | indicação de não encontrado |
