-- 1. Create the core Tenant tables
CREATE TABLE IF NOT EXISTS tenants (
                                       tenant_id VARCHAR(50) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    primary_contact_name VARCHAR(255) NOT NULL,
    primary_contact_email VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    account_status VARCHAR(20) DEFAULT 'TRIAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tenants PRIMARY KEY (tenant_id)
    );

-- IMPORTANT: Seed the default tenant so Foreign Keys don't fail
 INSERT IGNORE INTO tenants (tenant_id, company_name, primary_contact_name, primary_contact_email)
 VALUES ('default-tenant', 'Default Company', 'Admin', 'admin@example.com');

-- 2. Update TICKET table
ALTER TABLE ticket ADD COLUMN tenant_id VARCHAR(50);
UPDATE ticket SET tenant_id = 'default-tenant' WHERE tenant_id IS NULL;
ALTER TABLE ticket MODIFY COLUMN tenant_id VARCHAR(50) NOT NULL;
-- CREATE INDEX idx_ticket_tenant_id ON ticket(tenant_id);
-- ALTER TABLE ticket ADD CONSTRAINT fk_ticket_tenant_ref FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id);

-- 3. Update USER table (Using Backticks for reserved word and MODIFY for MySQL)
ALTER TABLE `users` ADD COLUMN tenant_id VARCHAR(50);
UPDATE `users` SET tenant_id = 'default-tenant' WHERE tenant_id IS NULL;
ALTER TABLE users MODIFY COLUMN tenant_id VARCHAR(50) NOT NULL;
CREATE INDEX idx_user_tenant_id ON `users`(tenant_id);
ALTER TABLE `users` ADD CONSTRAINT fk_user_tenant_ref FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id);

-- Repeat the MODIFY pattern for LOCATION and CUSTOMER tables...
-- 4. Update LOCATION table
ALTER TABLE location ADD COLUMN tenant_id VARCHAR(50);
UPDATE location SET tenant_id = 'default-tenant' WHERE tenant_id IS NULL;
ALTER TABLE location MODIFY COLUMN tenant_id VARCHAR(50) NOT NULL;
CREATE INDEX idx_location_tenant_id ON location(tenant_id);
ALTER TABLE location ADD CONSTRAINT fk_location_tenant_ref
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id);

-- 5. Update CUSTOMER table
ALTER TABLE customer ADD COLUMN tenant_id VARCHAR(50);
UPDATE customer SET tenant_id = 'default-tenant' WHERE tenant_id IS NULL;
ALTER TABLE customer MODIFY COLUMN tenant_id VARCHAR(50) NOT NULL;
CREATE INDEX idx_customer_tenant_id ON customer(tenant_id);
ALTER TABLE customer ADD CONSTRAINT fk_customer_tenant_ref
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id);