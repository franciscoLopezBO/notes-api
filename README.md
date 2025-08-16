# Spring Boot Project

This project is a REST API developed with Spring Boot.

## Restore Database
You can restore the database using the backup included in the project.  
Make sure your database server is properly configured before restoring the backup.

## API Documentation
The API endpoints documentation is available through **Swagger UI** at the following URL:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Running the Project
1. Clone this repository.
2. Create the NotesDB in postgresql
2. Configure your database credentials in `application.properties`.
3. Restore the provided backup or run the InitialScript in the folder `db/` on your local database.
4. Run the project using:
   ```bash
   mvn spring-boot:run
