-- liquibase formatted sql
-- changeset HoosierCoder:add-ticket-status-column

-- Update tickets column to add status field
ALTER TABLE ticket
ADD COLUMN status VARCHAR(25) NOT NULL DEFAULT 'UNASSIGNED';
