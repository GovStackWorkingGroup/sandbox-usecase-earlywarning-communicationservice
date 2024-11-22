--liquibase formatted sql

CREATE TABLE IF NOT EXISTS "config"
(
    config_key   TEXT NOT NULL,
    config_value TEXT NOT NULL
);