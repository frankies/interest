# Interest Rate API Test Project

A Java project for testing REST APIs using Unirest HTTP client library.

## Project Structure

```
.
├── build.gradle           # Gradle build configuration
├── gradle.properties     # Gradle properties and settings
├── settings.gradle       # Gradle settings and repository configuration
├── src/
│   └── main/
│       └── java/
│           └── RestTest.java  # Main application class
└── crts/                 # Certificate files
    ├── DigiCert Global Root G2.crt
    ├── DigiCert Global Root G2.der
    └── elearning_ylearn_co_id.crt
```
## Generate .crt file

```bash

## Generate file
openssl s_client -connect elearning_ylearn_co_id:443 -showcerts </dev/null | openssl x509 -outform PEM -out elearning_ylearn_co_id.crt

## View file
openssl x509 -in elearning_ylearn_co_id.crt -text -noout

```

## Setup

This project requires:
- Java 21
- Gradle 8.x

## Dependencies

- Unirest Java 3.1.02 - HTTP client library for making REST API calls

## Build & Run

To build the project:
```sh
./gradlew build
```

To run the application:
```sh
./gradlew run
```

## Development

This is a Gradle project that can be imported into any Java IDE. VSCode users are recommended to install:
- Java Extension Pack
- Gradle Extension Pack

## Configuration

The project includes SSL certificates in the `crts/` directory for secure API connections.

## License

This project is licensed under the Apache License 2.0.