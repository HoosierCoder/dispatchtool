-- Drop the table if it already exists to start fresh.
DROP TABLE IF EXISTS ticket;

-- Create the ticket table
CREATE TABLE ticket (
    -- Primary key for the Ticket entity.
    ticket_id VARCHAR(255) NOT NULL,

    summary VARCHAR(255),

    description TEXT,

    is_dispatched TINYINT(1) NOT NULL,

    -- A foreign key to the 'user' table, as defined by the @ManyToOne relationship.
    -- The name 'user_id' is explicitly set in the @JoinColumn annotation.
    -- The column is nullable, as specified in the @Column annotation.
    -- Ensure that the data type matches the primary key of the 'user' table.
    -- Assuming the user_id is a VARCHAR.
    user_id BIGINT,

    date_dispatched DATETIME(6),

    created_date DATETIME(6) NOT NULL,

    modified_date DATETIME(6) NOT NULL,

    -- Define the primary key constraint
    PRIMARY KEY (ticket_id),

    -- Define the foreign key constraint to the 'user' table.
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);