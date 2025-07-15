# EcoEdu JavaFX Project

This is a JavaFX application managed with Maven.

## Project Structure

```
ecoedu/
├── pom.xml                # Maven build file
├── src/
│   ├── main/
│   │   ├── java/          # Java source code
│   │   │   └── com/ecoedu/
│   │   │       └── Main.java
│   │   └── resources/     # Application resources (FXML, images, etc.)
│   └── test/
│       └── java/          # Test source code
└── target/                # Build output (ignored by git)
```

## Requirements
- Java 11 or higher
- Maven 3.6+

## Build and Run

To build the project:
```sh
mvn clean install
```

To run the application:
```sh
mvn javafx:run
```

## Notes
- All build output is in the `target/` directory.
- Place FXML, images, and other resources in `src/main/resources/`.
- Place your main application code in `src/main/java/com/ecoedu/`. 