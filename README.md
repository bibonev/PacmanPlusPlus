# PacmanPlusPlus

## Developers
- Thomas Galvin
- Rose Kirtley
- Boyan Bonev
- Lyubomir Pashev
- Simeon Kostadinov
- Andrei-Marius Longhin

## Contributions

We welcome contributions from external developers. To do so please fork the
repository, work on the game following the guidelines outlined in the `Dev Info`
section and create pull requests for your changes.

## Dev Info

### Eclipse setup
To work on the project in Eclipse please follow the steps below:

1) Create a new project called `PacmanPlusPlus` (the name needs to be exactly that)

2) In Build Path, add the folder `resources` to the sources.

3) In Build Path, add the following libraries:
- JUnit 4
- Mockito ALL 1.9.5. (available [here](https://mvnrepository.com/artifact/org.mockito/mockito-all/1.9.5))

The game can now be run from the main class `GameUI`.

### Automated run of unit tests
In a Terminal/CMD `cd` into the `PacmanPlusPlus` directory.

When inside the directory, you can run all the unit tests using the command:

`./gradlew test` for Unix-based OS's or

`gradlew test` for Windows

NOTE: This command runs ALL tests for ALL services.

### Code coverage
From inside `PacmanPlusPlus` run using:

`./gradlew jacocoTestReport` for Unix

`gradlew jacocoTestReport` for Windows

Then you can find the generated HTML report inside
`java-team-project/build/reports/jacoco/test/html`.

### Note
For any gradle task to work, `gradle` needs to be installed first. See instructions on how to do that [here](https://docs.gradle.org/current/userguide/installation.html).

You might also want to run the `gradle wrapper` task first to avoid any wrapper-related errors.
