# FNVW - Finanzverwaltung

## Description

This project lets you manage your finances per month. You can add entries, group them in categories (entry types) like food, abonnements or cosmetics, and group these in groups like fixed costs, variable costs or income.

## About the project

This project is a simple Spring Boot Application, delivering a Webapp, that uses the REST API of the Spring Backend. The webapp is an Angular project that uses Axios to request the backend. The backend uses JPA to access the PostgreSQL database.

## Build the project

To build the project you have to navigate into the _scripts_ directory and execute the _build.sh_ script.

```bash
cd scripts/
./build.sh
```

## Run the project

To run the project you have to navigate into the _scripts_ directory and execute the _run.sh_ script.

```bash
cd scripts/
./run.sh
```

## Stop the project

### Running in detached mode

To stop the project when running in detached mode, you can run the _stop.sh_ script. Therefore you have to be in the _scripts_ directory.

```bash
cd scripts/
./stop.sh
```

### Not running in detached mode

When not running in detached mode, you can just press _control + C_.

## Common Issues

### Permission denied while trying to connect to the Docker daemon socket

The solution is to update the permissions of the Docker socket:

```bash
sudo chmod 666 /var/run/docker.sock
```
