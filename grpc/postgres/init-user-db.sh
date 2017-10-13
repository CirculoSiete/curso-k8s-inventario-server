
#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER storer PASSWORD 'foryoureyes';
    CREATE DATABASE warehouse OWNER storer;
    GRANT ALL PRIVILEGES ON DATABASE warehouse TO storer;
EOSQL