--liquibase formatted sql

CREATE TABLE IF NOT EXISTS "config"
(
    key   TEXT NOT NULL,
    value TEXT NOT NULL
);