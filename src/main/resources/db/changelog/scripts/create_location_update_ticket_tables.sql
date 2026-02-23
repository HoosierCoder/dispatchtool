-- Creating the location table to be represented by
-- the location entity, and included in the ticket
-- table and entity.

CREATE TABLE location (
                          id BIGINT AUTO_INCREMENT NOT NULL,
                          street_address VARCHAR(255) NOT NULL,
                          apartment_number VARCHAR(50),
                          city VARCHAR(100) NOT NULL,
                          state VARCHAR(50) NOT NULL,
                          zip_code VARCHAR(20) NOT NULL,
                          CONSTRAINT pk_location PRIMARY KEY (id)
);

ALTER TABLE ticket ADD COLUMN location_id BIGINT;

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_location
        FOREIGN KEY (location_id) REFERENCES location (id);