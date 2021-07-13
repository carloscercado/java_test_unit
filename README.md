## **Configurar el ambiente de pruebas**

Para configurar el ambiente de pruebas, es necesario crear una base de datos en la maquina donde desea ejecutar los casos de prueba, dicha base de datos debe contener las siguientes caracteristicas:

**DB NAME:** macarena_test

**DB PORT:** 5432

**DB USERNAME:** tuten_user

**DB PASSWORD:** holatuten123.

Una vez creada la base de datos, restaurar el backup que esta en _/sql/dump_test_create.sql_, para hacerlo se puede ejecutar la siguiente instrucción:

```bash
psql -U tuten_user -d macarena_test -f sql/dump_test_create.sql	
```

Adicionalmente, es necesario actualizar la base de datos con los ultimos cambios a nivel de estructura, para esto ejecutar una actualizacion con **liquibase**.

```bash
liquibase --url="jdbc:postgresql://localhost:5432/macarena_test" --contexts=dev,test update
```

Finalmente, ejecutar las pruebas.

```bash
mvn test
```