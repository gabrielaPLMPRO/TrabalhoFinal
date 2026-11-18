# language: pt
@US10 @relatorios @a_desenvolver
Funcionalidade: Consultar a situação dos clientes e do caixa
  Como gestor
  Eu quero consultar reservas, consumos e totais financeiros
  Para acompanhar débitos e resultados do estabelecimento

  Cenário: Consultar a ficha de um cliente
    Dado que "Ana" possui reservas de quadra, aulas e consumos registrados
    E que seu saldo devido é de 100,00 reais
    Quando consulto a ficha de "Ana"
    Então devo visualizar suas reservas de quadra e aulas
    E devo visualizar todos os seus consumos
    E o total devido apresentado deve ser de 100,00 reais

  Cenário: Não misturar dados de clientes na ficha
    Dado que "Ana" e "Bruno" possuem consumos distintos
    Quando consulto a ficha de "Ana"
    Então os consumos exclusivos de "Bruno" não devem aparecer na ficha de "Ana"

  Cenário: Informar cliente não encontrado
    Dado que não existe cliente com o código informado
    Quando solicito sua ficha
    Então devo receber indicação de cliente não encontrado
    E nenhuma ficha de outro cliente deve ser apresentada

  Cenário: Separar valores pagos e não pagos por categoria e data
    Dado que na data 18/11/2026 existem os seguintes totais conhecidos
      | categoria               | pago   | não pago |
      | sauna                   | 30,00  | 40,00    |
      | mensalidades e multas   | 100,00 | 120,00   |
      | produtos                | 20,00  | 10,00    |
    E que não há descontos nem saldos de pagamentos parciais nesses dados
    E que existem outros consumos em uma data diferente
    Quando consulto o fluxo de caixa de 18/11/2026
    Então cada categoria deve apresentar seus totais pagos e não pagos conhecidos
    E os consumos de outras datas não devem compor esses totais

  Cenário: Consultar data sem movimento
    Dado que não existem movimentos na data selecionada
    Quando consulto seu fluxo de caixa
    Então os totais pagos e não pagos de cada categoria devem ser zero
