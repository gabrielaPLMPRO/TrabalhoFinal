# language: pt
@US05 @inicializacao
Funcionalidade: Iniciar o atendimento com situação operacional clara
  Como operador
  Eu quero saber se o sistema está pronto para atendimento
  Para evitar operações sem acesso aos dados necessários

  Cenário: Liberar atendimento quando os pré-requisitos estão disponíveis
    Dado que a configuração de acesso é válida
    E que o banco está acessível e confirma a conexão
    E que todas as tabelas e dados básicos exigidos estão disponíveis
    Quando inicio o sistema
    Então devo poder acessar Clientes, Produtos, Consumos e Pagamentos
    E a situação de disponibilidade deve ser apresentada

  Esquema do Cenário: Iniciar com configuração incompleta ou inválida
    Dado que a configuração de acesso contém "<problema>"
    Quando inicio o sistema
    Então a tela inicial e as orientações de configuração devem permanecer acessíveis
    E os cadastros devem ficar indisponíveis
    E devo receber orientação sobre "<campo>"

    Exemplos:
      | problema                   | campo                  |
      | endereço ausente          | endereço de conexão    |
      | endereço vazio            | endereço de conexão    |
      | endereço de outro banco   | endereço PostgreSQL    |
      | usuário ausente           | usuário                |
      | usuário em branco         | usuário                |
      | senha não configurada     | configuração da senha  |

  Cenário: Aceitar senha explicitamente vazia quando o ambiente permite
    Dado que a senha foi explicitamente configurada como vazia
    E que os demais pré-requisitos de atendimento estão disponíveis
    E que o banco permite essa forma de autenticação
    Quando inicio o sistema
    Então a configuração não deve ser rejeitada apenas por senha vazia
    E o atendimento deve ser liberado

  Esquema do Cenário: Informar indisponibilidade sem expor credenciais
    Dado que a configuração de acesso foi informada
    E que o acesso ao banco apresenta "<problema>"
    Quando inicio o sistema
    Então os cadastros devem ficar indisponíveis
    E devo receber orientação para verificar a conexão
    E a mensagem não deve apresentar senhas nem detalhes sensíveis da conexão

    Exemplos:
      | problema                       |
      | falha de autenticação          |
      | serviço indisponível           |
      | conexão sem resposta           |
      | resposta sem confirmação de banco |

  @a_refatorar
  Esquema do Cenário: Distinguir banco acessível de banco pronto
    Dado que o banco está acessível
    Mas existe "<pendência>"
    Quando inicio o sistema
    Então o atendimento deve ser bloqueado com indicação da pendência
    E a tela de orientações deve permanecer acessível

    Exemplos:
      | pendência                              |
      | uma tabela obrigatória ausente         |
      | cadastro de valores básicos ausente    |
      | produtos internos obrigatórios ausentes |
      | cliente básico da sauna ausente        |

  @interface
  Cenário: Retornar ao início após consultar um módulo
    Dado que abri um dos módulos disponíveis
    Quando retorno ao início
    Então o conteúdo de boas-vindas deve ser apresentado novamente
    E o menu de navegação deve permanecer disponível

  @interface
  Cenário: Informar falha de abertura de módulo
    Dado que o atendimento está disponível
    Mas um módulo não pode ser aberto por falta de um recurso da instalação
    Quando tento abrir esse módulo
    Então devo receber uma indicação de falha ao abrir o módulo
    E devo poder retornar ao início

  @caracterizacao
  Cenário: Informar módulo ainda não migrado
    Dado que o módulo solicitado ainda não foi migrado para a nova aplicação
    Quando tento abri-lo
    Então devo receber indicação de que sua implementação está pendente
    E o sistema não deve apresentá-lo como operacional
