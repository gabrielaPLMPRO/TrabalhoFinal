# language: pt
@US09 @mensalidades @a_desenvolver
Funcionalidade: Gerar mensalidades e multas de squash
  Como gestor
  Eu quero gerar as cobranças mensais das reservas
  Para distribuir os valores e identificar débitos por atraso

  Contexto:
    Dado que os valores e vencimentos estão configurados

  Cenário: Dividir mensalidade de quadra entre participantes
    Dado que existe uma reserva de jogo com 2 participantes
    E que a mensalidade da reserva é de 200,00 reais
    E que a cobrança dessa reserva ainda não foi gerada para novembro de 2026
    Quando gero as mensalidades de novembro de 2026
    Então cada participante deve receber um consumo de mensalidade de 100,00 reais

  Cenário: Cobrar quatro aulas mensais por aluno
    Dado que existe uma reserva de aula para "Ana"
    E que cada aula custa 25,00 reais
    E que a cobrança dessa reserva ainda não foi gerada para novembro de 2026
    Quando gero as mensalidades de novembro de 2026
    Então "Ana" deve receber um consumo de mensalidade de 100,00 reais

  Cenário: Não duplicar mensalidades já geradas
    Dado que as mensalidades de novembro de 2026 já foram geradas
    Quando solicito novamente a geração para novembro de 2026
    Então nenhum consumo de mensalidade deve ser duplicado

  @pendente_regra
  Cenário: Gerar primeira multa para jogo em atraso
    Dado que uma mensalidade de jogo com 2 participantes permanece não paga após o primeiro vencimento
    E que os valores do primeiro e segundo vencimentos são 200,00 e 240,00 reais
    E que nenhuma multa dessa reserva foi gerada para o mês selecionado
    E que ainda não chegou o vencimento da segunda multa
    Quando gero a primeira multa do mês selecionado
    Então cada participante devedor deve receber um consumo de multa de 20,00 reais

  Esquema do Cenário: Não gerar multa quando não há atraso elegível
    Dado que a cobrança corresponde a "<situação>"
    Quando solicito a geração de multas
    Então nenhum consumo de multa deve ser criado para essa cobrança

    Exemplos:
      | situação                               |
      | mensalidade de jogo ainda não vencida |
      | mensalidade de jogo já paga           |
      | mensalidade de aula em atraso         |

  @pendente_regra
  Cenário: Calcular segunda multa a partir das duas últimas faixas
    Dado que os valores do segundo e terceiro vencimentos são 240,00 e 280,00 reais
    E que existe reserva de jogo com 2 participantes devedores após o segundo vencimento
    E que essa cobrança atende à política acordada de geração de multas
    Quando gero a segunda multa do mês selecionado
    Então cada participante devedor deve receber um consumo adicional de 20,00 reais
