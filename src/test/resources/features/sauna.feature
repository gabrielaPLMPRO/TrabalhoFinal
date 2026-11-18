# language: pt
@US07 @sauna @a_desenvolver
Funcionalidade: Atender clientes avulsos da sauna
  Como atendente
  Eu quero manter as contas dos clientes da sauna
  Para cobrar o uso e os produtos consumidos por pessoa

  Contexto:
    Dado que os valores e os dados básicos da sauna estão configurados
    E que a data de atendimento é 18/11/2026

  Cenário: Abrir o atendimento da data atual
    Quando acesso o atendimento da sauna
    Então devo visualizar os clientes da sauna de 18/11/2026

  Cenário: Adicionar cliente avulso com cobrança inicial
    Dado que o valor configurado da sauna é de 30,00 reais
    Quando adiciono o cliente avulso "Carlos" ao atendimento de hoje
    Então sua conta deve ser criada com cobrança inicial de 30,00 reais
    E não deve ser exigido um cadastro comum para esse cliente avulso

  Cenário: Acrescentar consumo à conta selecionada
    Dado que selecionei a conta de "Carlos" do atendimento de hoje
    E que seu total atual é de 30,00 reais
    Quando acrescento 2 unidades de um produto ao valor unitário de 5,00 reais
    Então esse consumo deve aparecer na conta de "Carlos"
    E o total apresentado deve ser de 40,00 reais
    E o consumo deve integrar os registros de consumo do estabelecimento

  Cenário: Consultar a conta de um cliente avulso
    Dado que existem duas contas avulsas na data de atendimento
    Quando seleciono uma dessas contas
    Então devo visualizar somente os produtos e o total da conta selecionada

  Cenário: Excluir conta ainda não paga
    Dado que existe uma conta da sauna de hoje ainda não paga
    Quando confirmo sua exclusão
    Então a conta e seus consumos associados devem ser removidos
    E as demais contas devem permanecer inalteradas

  Cenário: Preservar conta paga contra exclusão
    Dado que existe uma conta da sauna já paga
    Quando tento excluir essa conta
    Então a exclusão deve ser rejeitada
    E seus registros devem permanecer disponíveis para consulta

  Esquema do Cenário: Restringir alterações fora da data atual
    Dado que consultei o atendimento da sauna de 17/11/2026
    Quando tento "<ação>"
    Então a alteração deve ser rejeitada por atendimento fora da data atual
    E os registros anteriores devem ser preservados

    Exemplos:
      | ação                  |
      | adicionar um cliente |
      | excluir um cliente   |
      | adicionar um consumo |
      | excluir um consumo   |

  Cenário: Permitir pagamento de conta anterior
    Dado que consultei uma conta não paga de 17/11/2026 no valor de 40,00 reais
    Quando recebo o pagamento integral de 40,00 reais sem desconto
    Então o pagamento deve ser registrado
    E os consumos dessa conta devem ficar pagos
