CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email_address VARCHAR(100) UNIQUE NOT NULL,
                       budget DECIMAL(10, 2) NOT NULL
);

CREATE TABLE pets (
                      pet_id SERIAL PRIMARY KEY,
                      owner_id INT REFERENCES users(user_id),
                      name VARCHAR(50) NOT NULL,
                      pet_type VARCHAR(10) NOT NULL,
                      description TEXT,
                      date_of_birth DATE NOT NULL,
                      price DECIMAL(10, 2) NOT NULL,
                      rating INT,
                      FOREIGN KEY (owner_id) REFERENCES users(user_id)
);