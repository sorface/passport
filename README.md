# server-oauth2 (Platform SSO)

[![Java CI with Maven](https://github.com/sorface/sso-server/actions/workflows/maven.yml/badge.svg)](https://github.com/sorface/sso-server/actions/workflows/maven.yml)
![jacoco](.github/badges/jacoco.svg)

## Infrastructure

* Postgresql
* Redis
* Kotlin

##  Stack

* Kotlin (v 1.9.23) (основной язык)
  * coroutines (многопоточность)
* Spring (framework)
  * web (взаимодействие по HTTP)
  * data-jpa (postgresql driver)
  * data-redis (redis)
  * security (базовая защита приложения)
  * oauth2-authorization-server (сервер авторизации)
  * oauth2-client (клиент авторизации)
  * oauth2-resource-server (потребитель авторизации)
  * session-data-redis (управление сессиями в Redis)
  * mail (отправка почтовых писем)
  * thymeleaf (шаблонизатор писем)
  * sleuth (управление метаданными исполнения)
  * actuator (мониторинг приложения)
* Liquibase (миграция баз данных: схем и данных)

## Environment

### Application Metadata

| Environment                  | Описание          | Store     | Значение DEV ~ PROD                 | Тип данных |
|------------------------------|-------------------|-----------|-------------------------------------|------------|
| APPLICATION_METADATA_VERSION | Версия приложения | pipeline  | `1.0.0` ~ `<BUILD_CURRENT_VERSION>` | string     |
| APPLICATION_TARGET_PORT      | Порт запуска      | ConfigMap | `9000`' ~ `...`                     | integer    |

### Spring

| Environment            | Описание           | Store     | Значение DEV/PROD            | Тип данных |
|------------------------|--------------------|-----------|------------------------------|------------|
| SPRING_PROFILES_ACTIVE | Профиль приложения | ConfigMap | `development` ~ `production` | string     |

### Cors

| Environment                   | Описание              | Store     | Значение DEV/PROD                                                                                                                                         | Тип данных |
|-------------------------------|-----------------------|-----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| PASSPORT_CORS_ALLOWED-ORIGINS | Разрешенные источники | ConfigMap | `http://localhost:9020;http://localhost:9030;http://localhost:8080` ~ `https://id.sorface.com,https://interview.sorface.com,https://passport.sorface.com` | string     |

### Redis

| Environment    | Описание               | Store | Значение DEV ~ PROD    | Тип данных |
|----------------|------------------------|-------|------------------------|------------|
| REDIS_HOST     | Хост Redis             | Vault | `localhost` ~ `...`    | string     |
| REDIS_USERNAME | Имя пользователя Redis | Vault | `default` ~ `...`      | string     |  
| REDIS_PASSWORD | Пароль Redis           | Vault | `testpassword` ~ `...` | string     |    

### Database

| Environment                | Описание                  | Store | Значение DEV ~ PROD                                 | Тип данных |
|----------------------------|---------------------------|-------|-----------------------------------------------------|------------|
| DATABASE_URL               | URL database              | Vault | `jdbc:postgresql://localhost:5432/passport` ~ `...` | string     |
| DATABASE_USERNAME          | Имя пользователя database | Vault | `user` ~ ...                                        | string     |
| DATABASE_PASSWORD          | Пароль database           | Vault | `user` ~ ...                                        | string     |
| DATABASE_DRIVER_CLASS_NAME | Драйвер класс database    | Vault | `org.postgresql.Driver` ~ `org.postgresql.Driver`   | string     |

### Mail

| Environment               | Описание            | Store | Значение DEV ~ PROD | Тип данных |
|---------------------------|---------------------|-------|---------------------|------------|
| MAIL_NOTIFICATOR_USERNAME | Имя mail-клиент     | Vault | `...` ~ `...`       | string     |
| MAIL_NOTIFICATOR_PASSWORD | Пароль mail-клиента | Vault | `...` ~ `...`       | string     |

### OAuth2

| Environment                | Описание                          | Store | Значение DEV ~ PROD | Тип данных |
|----------------------------|-----------------------------------|-------|---------------------|------------|
| OAUTH_CLIENT_GITHUB_ID     | Идентификатор приложения в GitHub | Vault | `...` ~ `...`       | string     |
| OAUTH_CLIENT_GITHUB_SECRET | Секрет приложения в GitHub        | Vault | `...` ~ `...`       | string     |
| OAUTH_CLIENT_YANDEX_ID     | Идентификатор приложения в Yandex | Vault | `...` ~ `...`       | string     |
| OAUTH_CLIENT_YANDEX_SECRET | Секрет приложения в Yandex        | Vault | `...` ~ `...`       | string     |
| OAUTH_CLIENT_TWITCH_ID     | Идентификатор приложения в Twitch | Vault | `...` ~ `...`       | string     |
| OAUTH_CLIENT_TWITCH_SECRET | Секрет приложения в Twitch        | Vault | `...` ~ `...`       | string     |

| Environment                      | Описание                      | Store     | Значение DEV ~ PROD                                                                          | Тип данных |
|----------------------------------|-------------------------------|-----------|----------------------------------------------------------------------------------------------|------------|
| OAUTH_CLIENT_YANDEX_REDIRECT_URL | URL webhook приложения Yandex | ConfigMap | `http://localhost:8080/login/oauth2/code/yandex` ~ `http://*******/login/oauth2/code/yandex` | string     |
| OAUTH_CLIENT_TWITCH_REDIRECT_URL | URL webhook приложения Twitch | ConfigMap | `http://localhost:8080/login/oauth2/code/twitch` ~ `http://*******/login/oauth2/code/twitch` | string     |

| Environment                    | Описание                            | Store     | Значение DEV ~ PROD                     | Тип данных |
|--------------------------------|-------------------------------------|-----------|-----------------------------------------|------------|
| SPRING_SESSION_REDIS_NAMESPACE | Название namespace сессий для Redis | ConfigMap | `passport:session` ~ `passport:session` | string     |
| SPRING_SESSION_TIMEOUT         | Время жизни сессии в Redis          | ConfigMap | `5d` ~ `5d`                             | string     |

| Environment                                  | Описание                                          | Store     | Значение DEV ~ PROD   | Тип данных |
|----------------------------------------------|---------------------------------------------------|-----------|-----------------------|------------|
| PASSPORT_OAUTH_TOKEN_ACCESS_CRON             | CRON для настройки access_token                   | ConfigMap | `minutes` ~ `minutes` | string     |
| PASSPORT_OAUTH_TOKEN_ACCESS_TTL              | Значение для access_token относительно CRON       | ConfigMap | `5` ~ `5`             | integer    |
| PASSPORT_OAUTH_TOKEN_REFRESH_CRON            | CRON для настройки refresh_token                  | ConfigMap | `days` ~ `days`       | string     |
| PASSPORT_OAUTH_TOKEN_REFRESH_TTL             | Значение для refresh_token относительно CRON      | ConfigMap | `5` ~ `5`             | integer    |
| PASSPORT_OAUTH_TOKEN_AUTHORIZATION_CODE_CRON | CRON для настройки authorization_code             | ConfigMap | `minutes` ~ `minutes` | string     |
| PASSPORT_OAUTH_TOKEN_AUTHORIZATION_CODE_TTL  | Значение для authorization_code относительно CRON | ConfigMap | `5` ~ `5`             | integer    |

### Cookie

| Environment                                | Описание                                  | Store     | Значение DEV ~ PROD             | Тип данных |
|--------------------------------------------|-------------------------------------------|-----------|---------------------------------|------------|
| PASSPORT_ACCOUNT_REGISTRY_COOKIE_DOMAIN    | Domain для cookie аккаунта регистрации    | ConfigMap | localhost ~ sorface.com         | string     |
| PASSPORT_ACCOUNT_REGISTRY_COOKIE_NAME      | Название cookie аккаунта регистрации      | ConfigMap | account_tmp_id ~ account_tmp_id | string     |
| PASSPORT_ACCOUNT_REGISTRY_COOKIE_PATH      | Путь для cookie аккаунта регистрации      | ConfigMap | `/` ~ `/`                       | string     |
| PASSPORT_ACCOUNT_REGISTRY_COOKIE_HTTP_ONLY | Cookie только в HTTP                      | ConfigMap | `true` ~ `true`                 | boolean    |
| PASSPORT_ACCOUNT_REGISTRY_COOKIE_SECURE    | Отправка cookie с чувствительными данными | ConfigMap | `true` ~ `true`                 | boolean    |

| Environment                    | Описание                              | Store     | Значение DEV ~ PROD             | Тип данных |
|--------------------------------|---------------------------------------|-----------|---------------------------------|------------|
| PASSPORT_CSRF_COOKIE_DOMAIN    | Domain для cookie CSRF                | ConfigMap | localhost ~ sorface.com         | string     |
| PASSPORT_CSRF_COOKIE_NAME      | Название cookie CSRF                  | ConfigMap | account_tmp_id ~ account_tmp_id | string     |
| PASSPORT_CSRF_COOKIE_PATH      | Путь для cookie CSRF                  | ConfigMap | `/` ~ `/`                       | string     |
| PASSPORT_CSRF_COOKIE_HTTP_ONLY | Cookie CSRF только в HTTP             | ConfigMap | `true` ~ `true`                 | boolean    |
| PASSPORT_CSRF_COOKIE_SECURE    | Cookie CSRF с чувствительными данными | ConfigMap | `true` ~ `true`                 | boolean    |

| Environment                              | Описание                              | Store     | Значение DEV ~ PROD                             | Тип данных |
|------------------------------------------|---------------------------------------|-----------|-------------------------------------------------|------------|
| PASSPORT_SESSION_COOKIE_DOMAIN_PATTERN   | Domain для cookie session             | ConfigMap | `^.+?\.(\w+\.[a-z]+)$` ~ `^.+?\.(\w+\.[a-z]+)$` | string     |
| PASSPORT_SESSION_COOKIE_DOMAIN_NAME      | Название cookie session               | ConfigMap | `pspt_sid` ~ `pspt_sid`                         | string     |
| PASSPORT_SESSION_COOKIE_DOMAIN_PATH      | Путь для cookie session               | ConfigMap | `/` ~ `/`                                       | string     |
| PASSPORT_SESSION_COOKIE_DOMAIN_HTTP_ONLY | Cookie CSRF только в HTTP             | ConfigMap | `true` ~ `true`                                 | boolean    |
| PASSPORT_SESSION_COOKIE_DOMAIN_SAME_SITE | Cookie CSRF с чувствительными данными | ConfigMap | `lax` ~ `lax`                                   | string     |

### Business

| Environment              | Описание                          | Store     | Значение DEV ~ PROD             | Тип данных |
|--------------------------|-----------------------------------|-----------|---------------------------------|------------|
| PASSPORT_FRONTEND_DOMAIN | URL на приложение клиент паспорта | ConfigMap | `http://localhost:9020` ~ `...` | string     |
| PROFILE_FRONTEND_URL     | URL на приложение клиент профиля  | ConfigMap | `http://localhost:9020` ~ `...` | string     |

| Environment                                         | Описание                                   | Store     | Значение DEV ~ PROD | Тип данных |
|-----------------------------------------------------|--------------------------------------------|-----------|---------------------|------------|
| PASSPORT_ACCOUNT_REGISTRY_OTP_LIVE_TO_CACHE_SECONDS | Время жизни регистрационного OTP в Redis   | ConfigMap | `1m` ~ `1m`         | string     |
| PASSPORT_ACCOUNT_REGISTRY_LIVE_TO_CACHE_SECONDS     | Время жизни регистрационных данных в Redis | ConfigMap | `10m` ~ `10m`       | string     |


## Locale i18

Доступен выбор двух вариантов языков приложения

* Русский (ru-RU)
* Английский (en-EN)
