# Pet Store

Pet Store is a comprehensive Java Spring Boot application designed for managing and facilitating pet-related activities. Whether you're a pet owner, an animal enthusiast, or simply looking to adopt a new furry friend, Pet Store provides an intuitive and user-friendly platform for all your pet needs.

## Features

### Pet Management

- **Create Pets**: Users can add their beloved pets to the store. Pet details including name, type, description, date of birth, price, and rating can be specified.
- **Browse Pets**: Explore a diverse catalog of pets available for adoption or purchase, categorized by type and other attributes.
- **Pet Details**: View detailed information about each pet, including descriptions, specifications, pricing, and user ratings.

### Pet Adoption

- **Purchase Pets**: Users can browse and purchase pets from the store. Purchased pets are transferred to the user's ownership.

### User Accounts

- **User Registration**: Create a user account, enabling features like saving preferences, managing shipping addresses, and securely storing payment methods.
- **Order History**: Track and manage your pet adoption and purchase history, including order status, invoices, and shipment tracking information.

### GraphQL API

- **GraphQL Integration**: Pet Store leverages GraphQL for efficient data retrieval, enabling users to request precisely the information they need.


## Technologies Used

Pet Store is built on a robust technology stack:

- **Java Spring Boot**: Powering the back-end of the application.
- **PostgreSQL**: As the database management system for storing pet and user data.
- **GraphQL**: Serving as the primary API for efficient and flexible data retrieval.
- **Git**: Employed for version control and collaboration among developers.

## Installation and Setup

To get Pet Store up and running on your local environment, follow these steps:

1. Clone the repository: `git clone https://github.com/BojanVrshkovski/PetStore`

2. Configure the database connection string in the `application.yml` file.

3. Start PostgreSQL and create the necessary database schema and tables that are provided in the PETSTORE_DB.sql file.

4. Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).

5. Build the project, ensuring that all dependencies are resolved.

6. Run the application.

7. Access the application in your web browser at `http://localhost:8080/graphiql?path=/graphql`.

## Contributing

Contributions to Pet Store are highly encouraged and welcomed! If you encounter any issues, have suggestions for improvements, or would like to contribute in any way, please submit an issue or a pull request. We value your input and appreciate your efforts in making Pet Store even better.
