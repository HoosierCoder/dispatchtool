-- SQL script to create the 'user' table in MySQL
CREATE TABLE user (
    -- userId: Primary key, auto-incremented BIGINT.
    -- The AUTO_INCREMENT starts from 1 as per the 'initial_value' parameter in the Java entity.
    userId BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- firstName: Stores the first name as a variable-length string.
    -- Assuming a reasonable maximum length of 255 characters.
    firstName VARCHAR(255) NOT NULL,

    -- lastName: Stores the last name as a variable-length string.
    -- Assuming a reasonable maximum length of 255 characters.
    lastName VARCHAR(255) NOT NULL,

    -- userRole: Stores the UserRole enum as a string.
    -- Assuming a reasonable maximum length for enum names, e.g., 50 characters.
    -- This corresponds to @Enumerated(EnumType.STRING) in Java.
    userRole VARCHAR(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
