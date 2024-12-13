# Gatling yaml wrapper

[![Build](https://github.com/nsriram/gatling-yaml-wrapper/actions/workflows/maven-make.yml/badge.svg)](https://github.com/nsriram/gatling-yaml-wrapper/actions/workflows/maven-make.yml)

Gatling wrapper for scripting performance tests in Yaml. This tool provides the flexibility to declare performance tests
in Yaml format

## Quick Sample

- Following is a sample Yaml that demonstrates generating load tests using a simple Yaml file for a GET API,
  with ramp up and response verification.
- Simulation yaml files are placed in `src/test/resources` folder.

```
baseUrl:
  'dev': 'https://computer-database.gatling.io'
acceptHeader: 'application/html'
contentTypeHeader: 'application/html'
maxDuration: 5
scenarios:
  - name: 'computer database'
    atOnceUsers: 1
    rampConfig:
      users: 2
      duration : 1
    chains:
      - name: 'fetch all computers'
        url: '/computers/'
        method: 'GET'
        check:
          status: 200
          responseTimeInMillis: 2500
```

### Simulation Yaml

The Yaml keys are defined closer to the way Gatling scripts are programmed. The Yaml hierarchy stays close to the
gatling hierarchy of **simulation->scenario->chain**

### 1. Simulation

| Field               | Description                                                                                        | Sample Value                             |
|---------------------|----------------------------------------------------------------------------------------------------|------------------------------------------|
| `baseUrl`           | Base URL of the system being performance tested (simulation). <br/> Supports multiple environments | `https://computer-database.gatling.io`   |
| `acceptHeader`      | HTTP accept header                                                                                 | `application/html` or `application/json` |
| `contentTypeHeader` | HTTP content type header                                                                           | `application/html` or `application/json` |
| `maxDuration`       | Maximum duration for the simulation (in seconds)                                                   | Integer value e.g., 30                   |
| ``scenarios``       | List of scenarios                                                                                  |                                          |

#### 1.1 Simulation (more) : Environment properties

Simulation supports passing environment properties for multiple environments.

- Following example shows the simulation configured with different env properties for SIT & UAT.

```
baseUrl:
  'unit-test': 'https://reqres.in'
  'sit': 'https://any-other-reqres.in'
propertiesFile:
  'unit-test': 'src/test/resources/reqres/reqres.properties'
  'sit': 'src/test/resources/reqres/reqres-sit.properties'
acceptHeader: 'application/html'
contentTypeHeader: 'application/html'
maxDuration: 5
scenarios:
```

| Field                      | Description                                                                                                                              | Sample Value |
|----------------------------|------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| `propertiesFile`           | List of simulation properties for multiple environments                                                                                  |              |
| `propertiesFile.unit-test` | Location of properties file corresponding to the env. This is placed in `src/test/resources` or <br/>folders inside `src/test/resources` |              |

A sample reqres-sit.properties could be as follows
**Note** :

- Key-Values inside the properties file are made available to the gatling test session.
- There could be multiple env files

```
sit-key1=sit-val1
sit-key2=sit-val2
sit-key3=sit-val3
```

### 2. Scenario

| Field                 | Description                             | Sample Value               |
|-----------------------|-----------------------------------------|----------------------------|
| `name`                | Scenario name                           | All computer database info |
| `atOnceUsers`         | Users at once (as in gatling domain)    | Integer value              |
| `rampConfig.users`    | Users to ramp-up (as in gatling domain) | Integer value              |
| `rampConfig.duration` | Duration of rampup (in seconds)         | Integer value              |
| `chains`              | URLs to be invoked. Array of chains     |                            |

#### 2.1 Scenario (more) : example with feedFile

Following example shows how the test data file in a feed can be passed.

```
scenarios:
  - name: 'User CRUD'
    feedFile:
      'dev': 'reqres/data.csv'
    atOnceUsers: 1
    rampConfig:
      users: 2
      duration: 1
```

| Field                | Description                                                                                                                                                    | Sample Value                                                                                                                           |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| `scenarios.feedFile` | Test data feed file (CSV) that supports multiple environments.<br/> Test data field & values defined in the CSV are made available to the gatling test session | Reference to CSV feed file. <br/> Feed file should be in simulation file directory or its sub-directories <br/>E.g., `reqres/data.csv` |

##### 3. Chain

| Field                        | Description                                                                 | Sample Value              |
|------------------------------|-----------------------------------------------------------------------------|---------------------------|
| `name`                       | name of the chain                                                           | e.g., fetch all computers |
| `url`                        | The URL to be load / perf tested. References to base URL                    | e.g., `'/api/users'`      |
| `method`                     | Supports Http GET or POST                                                   | POST or GET               |
| `check.status`               | Response checks. Checks the status value in response JSON                   | HTTP Status code          |
| `check.responseTimeInMillis` | Response checks. Checks the response time in milliseconds, in response JSON | Integer value e.g., 2500  |

##### 3.1 Chain (more) : example using POST

Following is an advanced chain example that uses POST.

```
chains:
  - name: 'create cartoon'
    url: '/api/users'
    method: 'POST'
    headers:
      'Content-Type': 'application/json'
    body:
      templateFile: 'src/test/resources/reqres/create-cartoon.json'
      keys:
        - 'name'
        - 'job'
    check:
      status: 201
      responseTimeInMillis: 2500
      jmesPathChecks:
        'name': '#{name}'
        'job': '#{job}'
    saveResponseFields:
      'name': 'createdName'
      'job': 'createdJob'
```

| Field                  | Description                                               | Sample Value                                                           |
|------------------------|-----------------------------------------------------------|------------------------------------------------------------------------|
| `headers`              | Request headers to be sent as a part of the HTTP request. | Any key value pair. E.g., 'Content-Type': 'application/json'           |
| `check.jmesPathChecks` | jmes check is done on the response.                       | Any key value pair expected in the response. E.g., `'name': '#{name}'` |

##### 3.2 POST request body

POST requests require a request body to be passed to the chain URL. The following explains how this is supported

| Field               | Description                                                                              | Sample Value                                                                                        |
|---------------------|------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| `body.templateFile` | JSON template file containing the request body with `%s` placeholders.                   | JSON file referenced from test/resources<br/> e.g., `src/test/resources/reqres/create-cartoon.json` |
| `body.keys.name`    | Key corresponding to the placeholder position in [request body JSON](#request-body-jSON) |                                                                                                     |
| `body.keys.job`     | Key corresponding to the placeholder position in [request body JSON](#request-body-jSON) |                                                                                                     |

###### Request body JSON

`create-cartoon.json` request body JSON contains the contents below.

```
{
  "name": "%s",
  "job": "%s"
}
```

**Note :**

- The key to the reference `name`, `job` in simulation.yml is picked from session and passed as request.
- `%s` placeholders are replaced using the corresponding values
- The value of `name`, `job` could be defined in (1) properties or in (2) feedFile or (3) saved from a previous response

###### UUID in requests

- Request body keys also support `uuid`. If a Key is defined as `uuid`, the tool generates a UUID and passes it
  in the request body. It is not needed as a part of the feed data / properties / from prev response

| Field            | Description                                     |
|------------------|-------------------------------------------------|
| `body.keys.uuid` | Request body placeholder to be replaced by UUID |

##### 3.3 Saving response in gatling session

In gatling tests response values from one call are used for subsequent requests as headers or values or for processing.
`saveResponseFields` can be used for this.

| Field                | Description                                                                          | Sample Value                                                                                                  |
|----------------------|--------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| `saveResponseFields` | Saves the fields defined in the gatling session with value string as the session key | Any key value pair. E.g., `'name': 'job'` <br/> Here, `name` saved as `createdName` and `job` as `createdJob` |

**Note :** The values saved are stored in session (similar to how the properties, feedFile are stored in gatling
session)

### Invocation & FrameworkSimulation class

Following are sample commands to invoke the `simple/simulation.yml` & `reqres/simulation.yml`

#### Simple simulation

```
mvn gatling:test -Denv=dev -DlogLevel=DEBUG \
    -Dsimulation=simple/simulation.yml \
    -Dgatling.simulationClass=tool.gatling.wrapper.core.FrameworkSimulation
```

#### Reqres simulation

```
mvn gatling:test -Denv=dev -DlogLevel=DEBUG \
    -Dsimulation=reqres/simulation.yml \
    -Dgatling.simulationClass=tool.gatling.wrapper.core.FrameworkSimulation
```

**Note :**

- Invocation of the gatling tool is similar to any simulation class.
- `tool.gatling.wrapper.core.FrameworkSimulation` is the only class to be used
- This class is passed the simulation yaml file

#### Log levels

- The log level is also supported between `INFO` and `DEBUG`. `DEBUG` provides more information

## Scripting your first simulation using Gatling yaml wrapper

1. Clone the repo & build (`mvn verify`)
2. Write your simulation Yaml file [Ref: Simulation Yaml](#simulation-yaml)
3. Place it in the src/test/resources folder (inside a new folder preferred)
4. If you need properties, feedFile, requestBody JSON place them in the folder where your simulation file and reference
   them as in samples
5. Run the maven command [Ref: Invocation section](#invocation). Enabling DEBUG helps in debugging better with gatling
   logs.

### References

- [GET Simulation yaml](src/test/resources/simple/simulation.yml)
- [POST Simulation yaml](src/test/resources/reqres/simulation.yml)

