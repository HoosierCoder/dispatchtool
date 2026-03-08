ALTER TABLE tenants
ADD COLUMN secondary_contact_name VARCHAR(255),
ADD COLUMN secondary_contact_email VARCHAR(255),
ADD COLUMN phone_number VARCHAR(255),
ADD COLUMN billing_street VARCHAR(255),
ADD COLUMN billing_city VARCHAR(255),
ADD COLUMN billing_state VARCHAR(255),
ADD COLUMN billing_zip VARCHAR(255);
