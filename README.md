# Introduction 
This project aims to create a crossword puzzle generator web application using Java with Spring Boot and integrating various AI APIs. The motivation behind this project is to provide users with an interactive and dynamic crossword puzzle experience, where puzzles are generated with the help of AI that makes crossword puzzle based on criteria that user provides.

## Docker Hub
https://hub.docker.com/r/michalaugustyniak/aicrc-ai-service \
https://hub.docker.com/r/michalaugustyniak/aicrc-api-gateway \
https://hub.docker.com/r/michalaugustyniak/aicrc-auth-service \
https://hub.docker.com/r/michalaugustyniak/aicrc-config-management-service \
https://hub.docker.com/r/michalaugustyniak/aicrc-config-server \
https://hub.docker.com/r/michalaugustyniak/aicrc-crossword-service \
https://hub.docker.com/r/michalaugustyniak/aicrc-eureka \
https://hub.docker.com/r/michalaugustyniak/aicrc-user-service

# Tech stack
- Java
- Spring Boot
- Keycloak
- RabbitMQ
- MySQL
- Redis

# System architecture
![ai-based-crossword-creator-system-design-diagram](https://github.com/user-attachments/assets/52a18d90-11ba-4ebe-935c-9c1dcf3569cb)

---


## Endpoints

## `ai-service`

### 1. 🔹 Generate prompt response

**POST** `/api/ai/`

Sends prompt to the selected provider and model and returns a response.

### Example

#### Request
```json
{
  "message": "Request message.",
  "provider": "openai",
  "model": "gpt-5-nano"
}
```

#### Response `201 CREATED`
```json
{
  "message": "Response message.",
  "provider": "openai",
  "model": "gpt-5-nano"
}
```

## `auth-service`

### 1. 🔹 Access Token

**POST** `/api/auth/token`

Generates Access Token.

### Example

#### Request
```json
{
  "username": "user",
  "password": "password"
}
```

#### Response `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAi",
  "expiresIn": 299,
  "refreshExpiresIn": 1800,
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUI",
  "tokenType": "Bearer",
  "idToken": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia",
  "notBeforePolicy": 0,
  "sessionState": "890cd12d-7c2e-4b06-93aa-976cca981dc5",
  "scope": "openid email profile"
}
```

## `user-service`

### 1. 🔹 User info by provided access token

**GET** `/api/users/me`

Retrieves information from Keycloak about user using access token.

### Example

#### Response `200 OK`
```json
{
  "id": "e707c36c-020d-4f6f-8fb7-edd5d5b0d1a4",
  "username": "admin",
  "roles": [
    "admin"
  ]
}
```

### 2. 🔹 User info by uuid

**GET** `/api/users/{uuid}`

Retrieves information from Keycloak about user using uuid.

### Example

#### Response `200 OK`
```json
{
  "id": "e707c36c-020d-4f6f-8fb7-edd5d5b0d1a4",
  "username": "admin",
  "roles": [
    "admin"
  ]
}
```

### 3. 🔹 Create a new user

**POST** `/api/users`

Creates a new user.

### Example

#### Request
```json
{
  "username": "user",
  "password": "password"
}
```

#### Response `201 CREATED`

## `crossword-service`

### 1. 🔹 Generate a crossword

**POST** `/api/crosswords?action=generate&type={crossword-type}`

Generates a crossword.

### Example

#### Request
```json
{
  "theme": "informatyka",
  "language": "polish",
  "type": "V2",
  "height": 10,
  "width": 10,
  "provider": "openai",
  "model": "gpt-5-nano"
}
```

#### Response `201 CREATED`
```json
{
  "uuid": "147fa27b-1563-4a3d-9999-5433a30bac0d",
  "theme": "informatyka",
  "height": 10,
  "width": 10,
  "language": "polish",
  "creator": "e707c36c-020d-4f6f-8fb7-edd5d5b0d1a4",
  "body": [
    [null, "p", "r", "o", "c", "e", "s", "o", "r", null], 
    [null, "y", null, null, null, null, null, null, null, null],
    [null, "t", null, null, null, "i", null, null, null, null],
    [null, "h", null, null, null, "n", null, null, null, null],
    ["k", "o", "m", "p", "u", "t", "e", "r", null, null],
    [null, "n", null, "a", null, "e", null, null, null, null],
    [null, null, null, "m", null, "r", null, null, null, null],
    [null, null, null, "i", null, "n", null, null, null, null],
    [null, null, null, "ę", null, "e", null, null, null, null],
    [null, null, null, "ć", null, "t", null, null, null, null]
  ],
  "words": [
    {
      "text": "komputer",
      "identifier": "1",
      "clue": "komputer",
      "orientation": "h",
      "coordinates": {
        "hindex": 4,
        "windex": 0
      }
    },
    {
      "text": "internet",
      "identifier": "3",
      "clue": "internet",
      "orientation": "v",
      "coordinates": {
        "hindex": 2,
        "windex": 5
      }
    },
    {
      "text": "procesor",
      "identifier": "5",
      "clue": "procesor",
      "orientation": "h",
      "coordinates": {
        "hindex": 0,
        "windex": 1
      }
    },
    {
      "text": "pamięć",
      "identifier": "4",
      "clue": "pamięć",
      "orientation": "v",
      "coordinates": {
        "hindex": 4,
        "windex": 3
      }
    },
    {
      "text": "python",
      "identifier": "2",
      "clue": "python",
      "orientation": "v",
      "coordinates": {
        "hindex": 0,
        "windex": 1
      }
    }
  ]
}
```

### 2. 🔹 Retrieve a crossword

**POST** `/api/crosswords/{uuid}`

Retrieves a crossword.

### Example

#### Response `200 OK`
```json
{
  "uuid": "147fa27b-1563-4a3d-9999-5433a30bac0d",
  "theme": "informatyka",
  "height": 10,
  "width": 10,
  "language": "polish",
  "creator": "e707c36c-020d-4f6f-8fb7-edd5d5b0d1a4",
  "body": [
    [null, "p", "r", "o", "c", "e", "s", "o", "r", null], 
    [null, "y", null, null, null, null, null, null, null, null],
    [null, "t", null, null, null, "i", null, null, null, null],
    [null, "h", null, null, null, "n", null, null, null, null],
    ["k", "o", "m", "p", "u", "t", "e", "r", null, null],
    [null, "n", null, "a", null, "e", null, null, null, null],
    [null, null, null, "m", null, "r", null, null, null, null],
    [null, null, null, "i", null, "n", null, null, null, null],
    [null, null, null, "ę", null, "e", null, null, null, null],
    [null, null, null, "ć", null, "t", null, null, null, null]
  ],
  "words": [
    {
      "text": "komputer",
      "identifier": "1",
      "clue": "komputer",
      "orientation": "h",
      "coordinates": {
        "hindex": 4,
        "windex": 0
      }
    },
    {
      "text": "internet",
      "identifier": "3",
      "clue": "internet",
      "orientation": "v",
      "coordinates": {
        "hindex": 2,
        "windex": 5
      }
    },
    {
      "text": "procesor",
      "identifier": "5",
      "clue": "procesor",
      "orientation": "h",
      "coordinates": {
        "hindex": 0,
        "windex": 1
      }
    },
    {
      "text": "pamięć",
      "identifier": "4",
      "clue": "pamięć",
      "orientation": "v",
      "coordinates": {
        "hindex": 4,
        "windex": 3
      }
    },
    {
      "text": "python",
      "identifier": "2",
      "clue": "python",
      "orientation": "v",
      "coordinates": {
        "hindex": 0,
        "windex": 1
      }
    }
  ]
}
```

### 3. 🔹 Retrieve crosswords

**POST** `/api/crosswords`

Retrieves crossword crosswords using provided parameters.

#### Request parameters
| Name       | Type   | Descrirption                | Example                                 |
|------------|--------|-----------------------------|-----------------------------------------|
| `creator`  | string | Filters by creator's uuid   | `"e707c36c-020d-4f6f-8fb7-edd5d5b0d1a4` |
| `hight`    | number | Filters by crossword height | `10`                                    |
| `width`    | number | Filters by crossword height | `10`                                    |
| `provider` | string | Filters by ai provider      | `"openai"`                              |
| `model`    | string | Filters by ai model         | `"gpt-5-nano"`                          |
| `language` | string | Filters by language         | `"english"`                             |
| `page`     | number | Page number (pagination)    | `0`                                     |
| `size`     | number | Page size (pagination)      | `10`                                    |
| `type`     | string | Filters by crossword type   | `"standard-crossword"`                  |

### Example

#### Response `200 OK`
```json
{
  "totalPages": 1,
  "numberOfElements": 1,
  "totalElements": 1,
  "crosswords": [
    {
      "uuid": "147fa27b-1563-4a3d-9999-5433a30bac0d",
      "theme": "informatyka",
      "height": 10,
      "width": 10,
      "language": "polish",
      "creator": "e707c36c-020d-4f6f-8fb7-edd5d5b0d1a4",
      "body": [
        [null, "p", "r", "o", "c", "e", "s", "o", "r", null],
        [null, "y", null, null, null, null, null, null, null, null],
        [null, "t", null, null, null, "i", null, null, null, null],
        [null, "h", null, null, null, "n", null, null, null, null],
        ["k", "o", "m", "p", "u", "t", "e", "r", null, null],
        [null, "n", null, "a", null, "e", null, null, null, null],
        [null, null, null, "m", null, "r", null, null, null, null],
        [null, null, null, "i", null, "n", null, null, null, null],
        [null, null, null, "ę", null, "e", null, null, null, null],
        [null, null, null, "ć", null, "t", null, null, null, null]
      ],
      "words": [
        {
          "text": "komputer",
          "identifier": "1",
          "clue": "komputer",
          "orientation": "h",
          "coordinates": {
            "hindex": 4,
            "windex": 0
          }
        },
        {
          "text": "internet",
          "identifier": "3",
          "clue": "internet",
          "orientation": "v",
          "coordinates": {
            "hindex": 2,
            "windex": 5
          }
        },
        {
          "text": "procesor",
          "identifier": "5",
          "clue": "procesor",
          "orientation": "h",
          "coordinates": {
            "hindex": 0,
            "windex": 1
          }
        },
        {
          "text": "pamięć",
          "identifier": "4",
          "clue": "pamięć",
          "orientation": "v",
          "coordinates": {
            "hindex": 4,
            "windex": 3
          }
        },
        {
          "text": "python",
          "identifier": "2",
          "clue": "python",
          "orientation": "v",
          "coordinates": {
            "hindex": 0,
            "windex": 1
          }
        }
      ]
    }
  ]
}
```

### 4. 🔹 Save words with clues

**POST** `/api/words?action=save-words-with-clues`

Saves words with clues.

### Example

#### Request
```json
{
    "theme": "informatyka",
    "language": "polish",
    "wordsAndClues": [
        {
            "word": "komputer",
            "clue": "komputer"
        },
        {
            "word": "internet",
            "clue": "internet"
        },
        {
            "word": "procesor",
            "clue": "procesor"
        },
        {
            "word": "pamięć",
            "clue": "pamięć"
        },
        {
            "word": "python",
            "clue": "python"
        }
    ]
}
```

#### Response `201 CREATED`

### 5. 🔹 Save words and generate clues

**POST** `/api/words?action=save-words-generate-clues`

Saves words and clues using selected provider and model.

### Example

#### Request
```json
{
    "theme": "informatyka",
    "language": "polish",
    "provider": "openai",
    "model": "gpt-5-nano",
    "words": ["komputer", "internet", "procesor", "pamięć", "python"]
}
```

#### Response `201 CREATED`

### 6. 🔹 Generate words and clues

**POST** `/api/words?action=generate-words`

Generates words and clues using selected provider and model.

### Example

#### Request
```json
{
    "provider": "openai",
    "model": "gpt-5-nano",
    "details": {
      "theme": "informatyka",
      "language": "polish",
      "quantity": 3,
      "maxLength": 10
    }
}
```

#### Response `201 CREATED`
```json
{
  "words": [
    {
      "word": "First",
      "clue": "First clue"
    },
    {
      "word": "Second",
      "clue": "Second clue"
    },
    {
      "word": "Third",
      "clue": "Third clue"
    }
  ]
}
```

## `config-management-service`

### 1. 🔹 Retrieve configuration property

**GET** `/api/config-management/properties/{name}`

Returns a configuration property by its name.

#### Request parameters

| Name                  | Type   | Descrirption                                        | Example        |
|-----------------------|--------|-----------------------------------------------------|----------------|
| `application-name`    | string | Search property for selected application            | `"ai-service"` |
| `application-profile` | string | Search property for application of selected profile | `"dev"`        |

### Example

#### Response `200 OK`

```json
{
  "propertyName": "ai-providers.openai.api-key",
  "propertyValue": "key",
  "applicationName": "ai-service",
  "applicationProfile": "dev"
}
```

### 2. 🔹 Set configuration property

**POST** `/api/config-management/properties`

Sets a configuration property.

#### Request parameters

| Name                  | Type   | Descrirption                                      | Example        |
|-----------------------|--------|---------------------------------------------------|----------------|
| `application-name`    | string | Sets property for selected application            | `"ai-service"` |
| `application-profile` | string | Sets property for application of selected profile | `"dev"`        |

### Example

#### Request
```json
{
  "propertyName": "ai-providers.openai.api-key",
  "propertyValue": "key",
  "applicationName": "ai-service",
  "applicationProfile": "dev"
}
```

#### Response `200 OK`

---

# Configuration

The system has **two levels of configuration**:
1. Configuration provided by the **configuration server**
2. Configuration provided by **environment variables**

---

## Configuration Server

Configuration shared by all application instances is stored in **Redis**.  
Each microservice has its own configuration stored in a separate **hash set**.

- **Hash set key**:
  ```
  [application-name]-[profile]
  ```

- **Values**:  
  Each configuration property is stored as a key–value pair:
  ```
  key → property name  
  value → property value
  ```

---

## Per-Service Configuration in Redis

Each microservice should define a dedicated hash set in Redis under the key `[application-name]-[profile]`.  
Below are the recommended properties for each service.

### Example: `user-service-[profile]`
| Property Key                   | Description | Example Value                                  |
|--------------------------------|-------------|------------------------------------------------|
| `keycloak.realm.client.id`     |             | `user-service`                                 |
| `keycloak.realm.client.secret` |             | `secret`                                       |
| `keycloak.realm.name`          |             | `app`                                          |
| `keycloak.admin.username`      |             | `admin`                                        |   
| `keycloak.admin.password`      |             | `password`                                     |
| `cors.allowed-origins`         |             | `http://localhost:5173,http://localhost:5174`  |

### Example: `auth-service-[profile]`
| Property Key                   | Description | Example Value                                 |
|--------------------------------|-------------|-----------------------------------------------|
| `keycloak.realm.client.id`     |             | `auth-service`                                |
| `keycloak.realm.client.secret` |             | `secret`                                      |
| `keycloak.realm.name`          |             | `app`                                         |
| `keycloak.admin.username`      |             | `admin`                                       |   
| `keycloak.admin.password`      |             | `password`                                    |
| `cors.allowed-origins`         |             | `http://localhost:5173,http://localhost:5174` |

### Example: `crossword-service-[profile]`
| Property Key                       | Description                                                                                             | Example Value                                 |
|------------------------------------|---------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| `keycloak.realm.client.id`         |                                                                                                         | `crossword-service`                           |
| `keycloak.realm.client.secret`     |                                                                                                         | `secret`                                      |
| `keycloak.realm.name`              |                                                                                                         | `app`                                         |
| `keycloak.admin.username`          |                                                                                                         | `admin`                                       |   
| `keycloak.admin.password`          |                                                                                                         | `password`                                    |
| `cors.allowed-origins`             |                                                                                                         | `http://localhost:5173,http://localhost:5174` |
| `database.username`                |                                                                                                         | `root`                                        |
| `database.password`                |                                                                                                         | `password`                                    |
| `crossword-service.iterations`     | The generator will create `n` crosswords and pick the best one.                                         | `30`                                          |
| `crossword-service.attempts-limit` | Words and clues generation request attempts limit (if not generated enough) while creating a crossword. | `10`                                          |

### Example: `ai-service-[profile]`
| Property Key                         | Description                             | Example Value                                 |
|--------------------------------------|-----------------------------------------|-----------------------------------------------|
| `keycloak.realm.client.id`           |                                         | `ai-service`                                  |
| `keycloak.realm.client.secret`       |                                         | `secret`                                      |
| `keycloak.realm.name`                |                                         | `app`                                         |
| `keycloak.admin.username`            |                                         | `admin`                                       |   
| `keycloak.admin.password`            |                                         | `password`                                    |
| `cors.allowed-origins`               |                                         | `http://localhost:5173,http://localhost:5174` |
| `ai-providers.openai.enabled-models` |                                         | `gpt-5-nano,gpt-4o,gpt-5-pro`                 |
| `ai-providers.openai.api-key`        |                                         | `my-secret-key`                               |

### Example: `config-management-service-[profile]`
| Property Key                   | Description                             | Example Value                                 |
|--------------------------------|-----------------------------------------|-----------------------------------------------|
| `keycloak.realm.client.id`     |                                         | `config-management-service`                   |
| `keycloak.realm.client.secret` |                                         | `secret`                                      |
| `keycloak.realm.name`          |                                         | `app`                                         |
| `keycloak.admin.username`      |                                         | `admin`                                       |   
| `keycloak.admin.password`      |                                         | `password`                                    |
| `cors.allowed-origins`         |                                         | `http://localhost:5173,http://localhost:5174` |

---

## Environment Variables

Each application must define specific environment variables that override or complement Redis configuration.

### Example: `user-service-[profile]`, `auth-service-[profile]`, `ai-service-[profile]`
| Variable Name         | Description                                 | Example Value                   |
|-----------------------|---------------------------------------------|---------------------------------|
| `EUREKA_URL`          |                                             | `http://localhost:8761/eureka/` |
| `CONFIG_SERVER_URL`   |                                             | `http://localhost:8012`         |
| `ACTIVE_PROFILE`      | Active profile (e.g., dev, prod)            | `dev`                           |
| `RABBITMQ_HOST`       |                                             | `localhost`                     |
| `RABBITMQ_PORT`       |                                             | `5672`                          |
| `KEYCLOAK_SERVER_URL` |                                             | `http://localhost:8080`         |

### Example: `crossword-service-[profile]`
| Variable Name         | Description                                 | Example Value                   |
|-----------------------|---------------------------------------------|---------------------------------|
| `EUREKA_URL`          |                                             | `http://localhost:8761/eureka/` |
| `CONFIG_SERVER_URL`   |                                             | `http://localhost:8012`         |
| `ACTIVE_PROFILE`      | Active profile (e.g., dev, prod)            | `dev`                           |
| `RABBITMQ_HOST`       |                                             | `localhost`                     |
| `RABBITMQ_PORT`       |                                             | `5672`                          |
| `KEYCLOAK_SERVER_URL` |                                             | `http://localhost:8080`         |
| `DATABASE_ADDRESS`    |                                             | `localhost:3306`                | 

### Example: `config-management-service-[profile]`
| Variable Name         | Description                                 | Example Value                   |
|-----------------------|---------------------------------------------|---------------------------------|
| `EUREKA_URL`          |                                             | `http://localhost:8761/eureka/` |
| `CONFIG_SERVER_URL`   |                                             | `http://localhost:8012`         |
| `ACTIVE_PROFILE`      | Active profile (e.g., dev, prod)            | `dev`                           |
| `RABBITMQ_HOST`       |                                             | `localhost`                     |
| `RABBITMQ_PORT`       |                                             | `5672`                          |
| `KEYCLOAK_SERVER_URL` |                                             | `http://localhost:8080`         |
| `REDIS_HOST`          |                                             | `localhost`                     |
| `REDIS_PORT`          |                                             | `6379`                          |  

### Example: `eureka`
| Variable Name        | Description                                 | Example Value |
|----------------------|---------------------------------------------|---------------|
| `EUREKA_SERVER_PORT` |                                             | `7861`        |

### Example: `api-gateway`
| Variable Name             | Description                                 | Example Value                   |
|---------------------------|---------------------------------------------|---------------------------------|
| `API_GATEWAY_SERVER_PORT` |                                             | `8081`                          |
| `EUREKA_URL`              |                                             | `http://localhost:8761/eureka/` |

### Example: `config-server`
| Variable Name           | Description | Example Value                   |
|-------------------------|-------------|---------------------------------|
| `CONFIG_SERVER_PORT`    |             | `8012`                          |
| `EUREKA_URL`            |             | `http://localhost:8761/eureka/` |
| `REDIS_HOST`            |             | `localhost`                     |
| `REDIS_PORT`            |             | `6379`                          |
| `RABBITMQ_HOST`         |             | `localhost`                     |
| `RABBITMQ_PORT`         |             | `5672`                          |

> **Note:**  
> Environment variables take precedence over configuration server values.

---

## Configuration Resolution Priority

1. Environment variables
2. Configuration server (Redis)
3. Default application properties (optional)

---

## Authentication and authorization
Protected endpoints need an access token sent in `Authorization` request header, which can be obtained via `auth-service`.
The system uses `Keycloak` as an IAM (identity and access management) service. 

---

## Keycloak Configuration
All system applications use the same Keycloak realm. It's ok to create a separate realm client for each microservice but not 
required. Access is gained based on **client role**, which is either `admin` or `member`.

> **Import file**:
> [realm-export.json](keycloak-config/realm-export.json)
