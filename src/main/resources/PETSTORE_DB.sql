CREATE SCHEMA PetStore

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

CREATE TABLE buy_log_entries (
    id SERIAL PRIMARY KEY,
    execution_date TIMESTAMP,
    user_id INT,
    allowed_to_buy BOOLEAN
);

INSERT INTO users (first_name, last_name, email_address, budget)
VALUES
    ('John', 'Doe', 'johndoe@example.com', 1000.00),
    ('Jane', 'Smith', 'janesmith@example.com', 1500.50),
    ('Michael', 'Johnson', 'michael@example.com', 750.25),
    ('Emily', 'Brown', 'emily@example.com', 2000.75),
    ('David', 'Lee', 'david@example.com', 800.00),
    ('Sarah', 'Wilson', 'sarah@example.com', 1200.00),
    ('James', 'Taylor', 'james@example.com', 1600.00),
    ('Olivia', 'Davis', 'olivia@example.com', 900.50),
    ('William', 'Martinez', 'william@example.com', 1300.25),
    ('Sophia', 'Anderson', 'sophia@example.com', 1100.75);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Fluffy', 'CAT', 'A cute and playful kitten', '2020-05-15', 50.99, 4);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Rex', 'DOG', 'Loyal and friendly Labrador', '2019-08-10', 89.99, 5);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Whiskers', 'CAT', 'Elegant and graceful cat', '2018-04-25', 45.50, 4);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Buddy', 'DOG', 'Playful and energetic Beagle', '2020-02-20', 75.25, 4);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Mittens', 'CAT', 'Fluffy and cuddly kitten', '2021-01-12', 55.00, 3);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Rocky', 'DOG', 'Courageous and protective German Shepherd', '2017-11-30', 95.75, 5);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Smokey', 'CAT', 'Mysterious and independent cat', '2019-03-08', 49.99, 3);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Luna', 'DOG', 'Gentle and affectionate Golden Retriever', '2020-06-22', 80.50, 5);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Tofz', 'CAT', 'Mysterious and independent toffii', '2019-03-08', 49.99, 3);

INSERT INTO pets (name, pet_type, description, date_of_birth, price, rating)
VALUES ('Klara', 'DOG', 'Gentle and affectionate Golden Retriever klara', '2020-06-22', 80.50, 5);


SELECT * FROM pets
SELECT * FROM users
SELECT * FROM buy_log_entries

