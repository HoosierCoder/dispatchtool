
INSERT INTO tenants (tenant_id, company_name, primary_contact_name, primary_contact_email, account_status)
VALUES ('hoosiercoder', 'HoosierCoder Global HQ', 'System Admin', 'admin@hoosiercoder.com', 'PLATFORM');

UPDATE tenants
SET company_name = 'Seattle Dispatch Office'
WHERE tenant_id = 'default-tenant';

UPDATE users SET tenant_id = 'hoosiercoder' WHERE username = 'rdude';
UPDATE users SET tenant_id = 'default-tenant' WHERE username = 'lseattle';

ALTER TABLE users
    ADD CONSTRAINT fk_users_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id);