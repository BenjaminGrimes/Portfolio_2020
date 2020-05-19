# CS4227

## Introduction

## Installation & Setup

### Prerequisites

- Have python & [pip](https://www.liquidweb.com/kb/install-pip-windows/) installed.

### Virtual Environments

Create a virtual environment by following the commands in [this](https://programwithus.com/learn-to-code/Pip-and-virtualenv-on-Windows/) article.

- `pip install virtualenv`
- `virtualenv env`

CD to the CS4227 directory and run the following:

- `env\Scripts\activate`

### Python Packages

With the virtual environment is activated, you can install requirements by running the following command:

```shell
pip install -r requirements.txt
```

`requirements.txt` was generated using [pip-tools](https://github.com/jazzband/pip-tools). Specific versions are not specified here, simply the package names.

## Running

To run the application, in the virtual env with installed packages, run the command:

```shell
python run.py
```

## Automated Setup

Similarily, you can invoke `setup-run.bat` from `CMD` to activate a virtual environment, install necessary packages and run the application.

## Admin

To add entries to the site database, simply go to `localhost:5000/admin`. This will bring you to an admin UI, where models can be added out of the box.

An admin created in the database with the correct admin role is as follows:

```
email: admin@admin.ie

password: admin
```
