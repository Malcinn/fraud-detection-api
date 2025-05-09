# fraud-detection-api

## Development environment setup
1. Install MySql
2. Log into mysql console and create database and user or use this script as an init script with docker container
````
CREATE USER fraud-db-user WITH ENCRYPTED PASSWORD 'fraud-db-password';
CREATE DATABASE fraud-transaction-db;
GRANT ALL PRIVILEGES ON DATABASE fraud-transaction-db TO fraud-db-user;
````
3. You can now start up development environment using ``./mvnw quarkus:dev``


# Side notes
### In order to work with Mastercard Bin Lookup API I had to:
1. Create developer account
2. Create project with Bin Lookup API access
3. Download the key store and use it with Oauth1.0 Authorization API to sign(add Authorization header) the outgoing requests.

In order to work with API using e.g postman you have to:
1. extract the private key from the key store
2. remove the passphrase 

Extracting private key from PKCS#12 (Public Key Cryptography Standards) keystore
1. openssl pkcs12 -in fraud-transaction-detection-api-sandbox-signing.p12 -nocerts -out private.key
2. type passphrase

Remove passphrase form key in order to use it with postman
1. openssl rsa -in private.key -out private_key_without_passphrase.key
2. type passphrase


This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/fraud-detection-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.
