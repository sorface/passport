# IdP Sorface

Поставщик удостоверений (IdP). Это служба, которая создаёт, управляет и проверяет личность пользователей в сети. Она позволяет пользователям получать доступ к
нескольким приложениям с помощью одного набора учётных данных. IdP аутентифицирует пользователей и выдаёт токены аутентификации для проверки их личности при
доступе к различным системам.

## Запуск

По умолчанию сервис запускается с frontend, то есть сервис отдает static.

Для отключения frontend, для использования сервиса IdP как API сервис выполните следующие шаги:

1. В SPRING_PROFILES_ACTIVE файла [.docker.idp.development.env](sandbox/.docker.idp.development.env) установите:
    ```properties
    # ...
    SPRING_PROFILES_ACTIVE=sandbox
    # ...
    ```
2. В ACTIVE_PROFILE файла [development.yaml](sandbox/development.yaml)
   ```properties
   # ...
   ACTIVE_PROFILE=sandbox
   # ...
    ```

Установите или измените properties env файла IdP сервиса. 

Файл конфигурации сервиса IdP [.docker.idp.development.env](sandbox/.docker.idp.development.env)

Перейти в папку `sandbox`

```shell
cd ./sandbox
```

Выполнить запуск `docker-compose`

```shell
docker-compose -f development.yaml up -d
```
