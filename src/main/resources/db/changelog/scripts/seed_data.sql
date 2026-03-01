-- 1. Create the Tenant first
INSERT IGNORE INTO tenants (tenant_id, company_name, primary_contact_name, primary_contact_email, active, account_status)
VALUES ('default-tenant', 'Hoosier Logic', 'Admin User', 'admin@hoosierlogic.com', true, 'ACTIVE');

-- 2. Create the User (belongs to Tenant)
INSERT IGNORE INTO users (user_id, first_name, last_name, username, user_role, is_active, tenant_id)
VALUES (1, 'Ringo', 'Dude', 'rdude', 'ADMIN', true, 'default-tenant');

-- 3. Create the Location (belongs to Tenant)
INSERT IGNORE INTO location (id, street_address, city, state, zip_code, tenant_id)
VALUES (1, '123 Emerald City Way', 'Seattle', 'WA', '98101', 'default-tenant');

-- 4. Create the Customer (belongs to Tenant and optional Location)
INSERT IGNORE INTO customer (id, first_name, last_name, email, phone_number, tenant_id, billing_location_id)
VALUES (1, 'Stevie', 'Puppy', 'lia@example.com', '555-0123', 'default-tenant', 1);

-- 5. Create the Ticket (belongs to Tenant, Customer, Location, and User)
INSERT IGNORE INTO ticket (ticket_id, summary, status, created_date, tenant_id, customer_id, location_id, user_id)
VALUES ('TKT-001', 'Leaking Pipe', 'UNASSIGNED', '2026-02-28 10:00:00', 'default-tenant', 1, 1, 1);