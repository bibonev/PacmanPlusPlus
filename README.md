# PacmanPlusPlus

## Dev Info

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
