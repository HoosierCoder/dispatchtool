-- Create the Customer Table
CREATE TABLE customer (
                          id BIGINT AUTO_INCREMENT NOT NULL,
                          first_name VARCHAR(50),
                          last_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100),
                          phone_number VARCHAR(20),
                          billing_location_id BIGINT,
                          CONSTRAINT pk_customer PRIMARY KEY (id)
);

-- Add Foreign Key for Customer Billing Address
ALTER TABLE customer
    ADD CONSTRAINT fk_customer_billing_location
        FOREIGN KEY (billing_location_id) REFERENCES location (id);

-- Update Ticket Table to include Customer reference
ALTER TABLE ticket
    ADD COLUMN customer_id BIGINT;

-- Add Foreign Key for Ticket's Customer
ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_customer
        FOREIGN KEY (customer_id) REFERENCES customer (id);