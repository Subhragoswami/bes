CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS PUBLIC.AUDITABLE_ENTITY
(
    id UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    date_created TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR,
    date_updated TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR,
    CONSTRAINT AUDITABLE_ENTITY_ID_PK PRIMARY KEY (ID)
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS charging_station_details (
   vendor_id UUID,
   name VARCHAR,
   gun_id NUMERIC,
   station_name VARCHAR,
   latitude DOUBLE PRECISION,
   longitude DOUBLE PRECISION,
   connectors VARCHAR,
   ocpp_Id VARCHAR,
   max_charging_capacity NUMERIC,
   address VARCHAR,
   status VARCHAR,
   onboarded_date VARCHAR,
   gun_details VARCHAR,
   CONSTRAINT CHARGING_STATION_DETAILS_ID_PK PRIMARY KEY (ID)
)INHERITS (AUDITABLE_ENTITY);

CREATE TABLE IF NOT EXISTS customer_transaction_details (
    customer_id UUID NOT NULL,
    vendor_id UUID NOT NULL,
    station_id UUID NOT NULL,
    start_date_time TIMESTAMP,
    end_date_time TIMESTAMP,
    kwh_used VARCHAR,
    cdr_token VARCHAR,
    authorization_reference VARCHAR,
    auth_method VARCHAR,
    evse_uid VARCHAR,
    connector_id NUMERIC,
    currency VARCHAR,
    charging_periods NUMERIC,
    vehicle_details VARCHAR,
    CONSTRAINT CUSTOMER_TRANSACTION_DETAILS_ID_PK PRIMARY KEY (ID)
)INHERITS (AUDITABLE_ENTITY);