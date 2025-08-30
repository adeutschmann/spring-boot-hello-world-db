-- Flyway Migration V1: Initialize Database
-- Create schema and greetings table

-- Create the helloworld schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS helloworld;

-- Set the search path to use helloworld schema by default
SET
search_path TO helloworld;

-- Create the greetings table
CREATE TABLE helloworld.greetings
(
    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    message    VARCHAR(500) NOT NULL,
    sender     VARCHAR(100),
    recipient  VARCHAR(100),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_greetings_sender ON helloworld.greetings (sender);
CREATE INDEX idx_greetings_recipient ON helloworld.greetings (recipient);
CREATE INDEX idx_greetings_created_at ON helloworld.greetings (created_at);

-- Create a trigger to automatically update the updated_at column
CREATE
OR REPLACE FUNCTION helloworld.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
language 'plpgsql';

CREATE TRIGGER update_greetings_updated_at
    BEFORE UPDATE
    ON helloworld.greetings
    FOR EACH ROW
    EXECUTE FUNCTION helloworld.update_updated_at_column();

-- Insert some sample data for testing
INSERT INTO helloworld.greetings (message, sender, recipient)
VALUES ('Hello World!', 'system', 'everyone'),
       ('Welcome to the Hello World API', 'admin', 'users'),
       ('Good morning!', 'alice', 'bob'),
       ('How are you doing?', 'bob', 'alice'),
       ('Have a great day!', 'system', 'all_users');
