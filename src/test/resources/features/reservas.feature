# language: pt
@US08 @reservas @a_desenvolver
Funcionalidade: Reservar quadras e aulas de squash
  Como atendente
  Eu quero organizar reservas de quadras e aulas
  Para consultar a ocupação e evitar reservas conflitantes

  Contexto:
    Dado que existem clientes cadastrados e uma grade de squash disponível

  Cenário: Reservar quadra para vários clientes
    Dado que a quadra 1 está livre na segunda-feira às 18:00
    Quando reservo esse horário para os clientes "Ana" e "Bruno" como jogo
    Então os dois clientes devem integrar a mesma reserva
    E a ocupação deve aparecer na grade de segunda-feira

  Cenário: Reservar aula para um cliente
    Dado que a quadra 1 está livre na terça-feira às 19:00
    Quando reservo esse horário como aula para "Ana"
    Então a reserva deve identificar "Ana" como aluna
    E deve ser apresentada como aula na grade de terça-feira

  Esquema do Cenário: Rejeitar reserva no mesmo horário e quadra
    Dado que a quadra 1 já está ocupada na segunda-feira às 18:00 por "<ocupação>"
    Quando tento cadastrar outra reserva nesse mesmo dia, horário e quadra
    Então a nova reserva deve ser rejeitada por conflito de ocupação
    E a reserva anterior deve permanecer inalterada

    Exemplos:
      | ocupação |
      | jogo     |
      | aula     |

  Cenário: Permitir outra reserva do mesmo cliente em horário livre
    Dado que "Ana" já possui reserva na segunda-feira às 18:00
    E que a quadra 1 está livre na terça-feira às 18:00
    Quando cadastro uma reserva para "Ana" nesse horário livre
    Então ambas as reservas devem continuar registradas

  Cenário: Remover participante antes de confirmar a reserva
    Dado que selecionei "Ana" e "Bruno" para uma nova reserva de jogo
    Quando retiro "Bruno" da seleção e confirmo a reserva em horário livre
    Então somente "Ana" deve integrar a reserva criada

  Cenário: Consultar a ocupação por dia da semana
    Dado que existem reservas de segunda-feira e terça-feira
    Quando consulto a grade de segunda-feira
    Então devo visualizar apenas as reservas de segunda-feira

  Cenário: Excluir reserva com confirmação
    Dado que existe uma reserva consultada
    Quando confirmo sua exclusão
    Então ela deve deixar de aparecer na grade e nas consultas de reservas

  Cenário: Cancelar exclusão de reserva
    Dado que existe uma reserva consultada
    Quando cancelo sua exclusão
    Então ela deve permanecer na grade e nas consultas de reservas
