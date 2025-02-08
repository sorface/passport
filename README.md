# server-oauth2 (Platform SSO)

[![Java CI with Maven](https://github.com/sorface/sso-server/actions/workflows/maven.yml/badge.svg)](https://github.com/sorface/sso-server/actions/workflows/maven.yml)
![jacoco](.github/badges/jacoco.svg)

Single Sign-On (SSO) и Single Logout (SLO) платформы.

## Environment

### Application Metadata

| Environment                  | Описание          | Store    | Значение DEV/PROD       |
|------------------------------|-------------------|----------|-------------------------|
| APPLICATION_METADATA_VERSION | Версия приложения | pipeline | dev / <CURRENT_VERSION> |

## Настройка и запуск

### Конфигурация

Для управления конфигурацией проекта в docker:

- [.docker.passport.sorface.backend.development.env](docker/.docker.passport.sorface.backend.development.env) # конфигурация development (без клиента)
- [.docker.passport.sorface.backend.production.env](docker/.docker.passport.sorface.backend.production.env) # конфигурация production (с клиентом)
- [.docker.redis.env](docker/.docker.redis.env) # конфигурация redis
- [.docker.pgsql.env](docker/.docker.pgsql.env) # конфигурация PostgreSQL

Измените на свои значения:

```properties
# spring envs
#- профиль spring 
SPRING_PROFILES_ACTIVE=docker
# database envs. 
# Указываются такие же значения как и в файле конфигурации docker/.docker.pgsql.env
DATABASE_URL=jdbc:postgresql://postgresql:5432/sso
DATABASE_USERNAME=user
DATABASE_PASSWORD=user
# mail envs.
# mail:
#  username: ${MAIL_NOTIFICATOR_USERNAME}
#  password: ${MAIL_NOTIFICATOR_PASSWORD}
#  port: 465
#  host: smtp.yandex.ru 
#  properties:
#   mail.transport.protocol: smtp
#   mail.smtp.auth: true
#   mail.smtp.starttls.enable: true
#   mail.smtp.ssl.enable: true
# по умолчанию используется smtp.yandex.ru, но вы можете это изменить в (src/main/resources/application.yml)
MAIL_NOTIFICATOR_USERNAME=***************
MAIL_NOTIFICATOR_PASSWORD=***************
# github envs
# ознакомиться и создать токены доступа можно по ссылке https://github.com/settings/developers
OAUTH_CLIENT_GITHUB_ID=***************
OAUTH_CLIENT_GITHUB_SECRET=***************
# google envs. Не поддерживается платформой в России.
OAUTH_CLIENT_GOOGLE_ID=****************
OAUTH_CLIENT_GOOGLE_SECRET=****************
# yandex envs
# ознакомиться с документацией и создать токены доступа можно по ссылке https://yandex.ru/dev/id/doc/ru/register-client
OAUTH_CLIENT_YANDEX_ID=****************
OAUTH_CLIENT_YANDEX_SECRET=****************
OAUTH_CLIENT_YANDEX_REDIRECT_URL=http://localhost:8080/login/oauth2/code/yandex
# twitch envs
# ознакомиться с документацией и создать токены доступа можно по ссылке https://dev.twitch.tv/console/apps
OAUTH_CLIENT_TWITCH_ID=****************
OAUTH_CLIENT_TWITCH_SECRET=****************
OAUTH_CLIENT_TWITCH_REDIRECT_URL=http://localhost:8080/login/oauth2/code/twitch
# oauth2 envs
OAUTH_SERVER_ISSUER_URL=http://localhost:8080
# client frontend url
CLIENT_SERVER_DOMAIN=http://localhost:8080
# redis envs
# Указываются такие же значения как и в файле конфигурации docker/.docker.pgsql.env
REDIS_HOST=redis
REDIS_USERNAME=default
REDIS_PASSWORD=testpassword
```

### Запуск

Перейдите в корневую папки проекта [/docker](docker)

Для запуска БЕЗ клиента:

```shell
docker-compose -f development.yaml up -d
```

Вывод в консоль:

```shell
sh-3.2$ docker-compose -f development.yaml up -d

[+] Running 4/5
 ⠧ Network docker_default            Created                                                                                                                                                                                                                                                              0.7s 
 ✔ Container redis                   Started                                                                                                                                                                                                                                                              0.4s 
 ✔ Container postgres                Started                                                                                                                                                                                                                                                              0.4s 
 ✔ Container passport.sorface        Started 
```

Для запуска с клиентом:

```shell
docker-compose -f production.yaml up -d
```

Вывод в консоль:

```shell
sh-3.2$ docker-compose -f production.yaml up -d
[+] Running 4/5
 ⠧ Network docker_default               Created                                                                                                                                                                                                                                                              0.7s 
 ✔ Container redis                      Started                                                                                                                                                                                                                                                              0.4s 
 ✔ Container postgres                   Started                                                                                                                                                                                                                                                              0.4s 
 ✔ Container passport.sorface           Started                                                                                                                                                                                                                                                              0.5s 
 ✔ Container passport.sorface.frontend  Started   
```

Перейди по ссылке из браузера:

```url
http://localhost:8080/account
```

### Возможные схемы входа OAuth 2.0

- yandex
- github
- twitch
- email

Ознакомиться с конфигурацией можно в файле [application.yml](web%2Fsrc%2Fmain%2Fresources%2Fapplication.yml)

## OAuth2

Запрос аутентификации пользователя

```http request
GET http://localhost:8080/oauth2/authorize?
    client_id=************@sorface.oauth.client
    &scope=scope.read
    &response_type=code
    &redirect_uri=http%3A%2F%2Flocalhost%3A5043%2Foauth2%2Fsorface
    &
    state=CfDJ8Ivqng5udLJLqh1hbKpDiIsX2uiv8TgWsiV12hMtpTAZpHQc8xL0oSq6jErcYOMVQo0-wGR8rZdbc4A4vM5qiQZQcs2nujEZT-UqPl0E7f4nwzUjTDQC-L3x6g7JKR2BhiQKfw2_la6PxGRj4YsLHMYVctdXE7IqQEimF7sBSS7HkYMVleV3jEloVq21Za8IdinwlXii_kQNxcpnglI0wCxgbDxuG7ItIoWtZ0FP2JHe
```

```http request
GET http://localhost:5043/oauth2/sorface?code=XQSuU0_hxT1nomJq5IusEBW2Tv5-BPuMZycz-
```

```http request
POST http://localhost:8080/token?
    grant_type=authorization_code
    &client_id=************@sorface.oauth.client
    &client_secret=************
    &redirect_uri=https://www.oauth.com/playground/authorization-code.html
    &code=lbKAPTprbOBQ2Gfr0Xv6UTymRMBrC_-AONl2nwE0xrQx_Dyt
```

### Запрос данных по токену и принципалу

```http request
POST http://localhost:8080/oauth2/introspect

Authorization: Basic ************************ # clientId:secretId in base64
Content-Type: application/json

{
    "token": "************************" # access token
}
```

```json
{
  "active": true,
  "sub": "developerdevpav",
  "aud": [
    "GDChSngolAhWxgZgjyeBPDwOCKUbfKQKXtCAiNtFprAVFwhhymPrinY@sorface.oauth.client"
  ],
  "nbf": "2024-03-30T00:05:18.089765Z",
  "scopes": [
    "scope.read"
  ],
  "iss": "http://localhost:8080",
  "exp": "2024-03-30T00:35:18.089765Z",
  "iat": "2024-03-30T00:05:18.089765Z",
  "jti": "893112ae-29e6-4983-a05a-3e1b961a8efa",
  "clientId": "GDChSngolAhWxgZgjyeBPDwOCKUbfKQKXtCAiNtFprAVFwhhymPrinY@sorface.oauth.client",
  "tokenType": "Bearer",
  "principal": {
    "id": "50ad8927-0acc-4dcd-860b-2abdbad20394",
    "firstName": null,
    "lastName": null,
    "middleName": null,
    "birthday": null,
    "avatarUrl": "https://avatars.com/****.jpeg",
    "username": "developerdevpav",
    "email": "***************@yandex.ru",
    "authorities": []
  }
}
```

## OAuth2 создание приложений-клиентов пользователя

Пользователю доступно создание приложение-клинтов (доступно только для пользователей с ролью ADMIN)

Для создания клиента необходимо быть авторизированных пользователем.

Выполнить следующий запрос:

```http request
POST http://localhost:8080/api/applications
Cookie: JSESSIONID=************************
Content-Type: application/json

{
	"name": "myapp",
	"redirectionUrls": [
		"http://localhost:8080/callback"
	]
}
```

Ответ:

```json

{
  "id": "2d5db191-1edf-47aa-bb04-777123db0092", // идентификатор приложения
  "clientId": "YexwrISIWiVMJNvGsfHmXbHnpeVBdbqypUoHaVueIcFieUrArURowJk@sorface.oauth.client", // clientId приложения
  "clientSecret": "uwnwqCUzpynvRmPGbOytdDKZNjyOREoUeqjuapqNeCtNhMgfjklArfe", // clientSecret приложения (выдается только на время получения) дальше будет недоступен
  "clientName": "myapp", // название приложения
  "issueTime": "2024-03-31T12:13:27.273255", // дата запроса приложения
  "expiresAt": "2025-01-25T12:13:27.273296", // дата завершения действия clientSecret
  "redirectUrls": [ // OAuth Redirect URL кода будет перенаправлен запрос после аутентификации пользователя
    "http://localhost:8080/callback"
  ]
}
```

# Диаграммы

### Регистрация пользователя через OTP

![alt text](/diagrams/registry_user.png)

## Locale i18

Доступен выбор двух вариантов языков приложения

* Русский
* Английский
