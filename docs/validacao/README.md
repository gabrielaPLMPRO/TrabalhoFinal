# Evidências de validação

- `antes/maven-test.log` e XML: 30 testes originais; 24 aprovados, 6 falhas de configuração de banco.
- `antes/regressoes-inicializacao.log`: 6 testes novos de diagnóstico; 5 falhas reproduzidas antes da correção.
- `depois/maven-clean-test.log` e XML: compilação do zero e suíte final com 50 testes; 43 aprovados, 7 dependentes do PostgreSQL de testes ainda não configurado.

Os testes de interface executam JavaFX real e FXML, usando um substituto JDBC vazio para evitar dados e rede. Eles não validam SQL, restrições ou transações do PostgreSQL.

Para executar somente os testes independentes do PostgreSQL:

```powershell
$env:JAVA_HOME = 'C:/Users/guiba/AppData/Local/Programs/Eclipse Adoptium/jdk-25.0.4.7-hotspot'
& ./.tools/apache-maven-3.9.16/bin/mvn.cmd '-Dtest=*ServiceTest,DatabaseConnectionTest,InicializacaoTest,TelaLoaderTest' test
```

Para a suíte completa, preparar o banco exclusivo de testes com o esquema do legado e configurar DANTE_TEST_DB_URL=`jdbc:postgresql://localhost:5432/dante_squash_test`, DANTE_TEST_DB_USER e DANTE_TEST_DB_PASSWORD. Em seguida executar `mvn clean test` (ou o Maven local com JAVA_HOME configurado).

Os testes de repositório inserem/atualizam/excluem registros; nunca devem ser apontados para produção. As verificações originais do nome do banco foram preservadas.
