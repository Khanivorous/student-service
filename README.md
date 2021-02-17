# Student Service

This is a simple demo rest api project that saves and returns student data from a mysql db.

This is to demo simple tests against a simple springboot application.

The docker-compose file can be used to create the services locally to run and test out.

## Workflows on github

The branch workflow runs tests on pull requests to master branch

The master workflow runs tests on the master branch and, if successful, pushes the docker image to docker repo 
available to pull from this command `docker pull khanivorous/student-service`

## Testing
There are 3 test classes in this repository

- StudentControllerTests
- StudentApplicationTest
- E2ETests

### StudentControllerTests

These tests simply test the rest controller layer, so the whole application context is not launched. 
These tests are a good waz of isolating controllers to check if they are working. As you will notice in the test class, mocks are required.

### StudentApplicationTest

These tests launch the whole application context, however this this uses mocks in order to test functionality without having to rely on real data.

### E2ETests

These tests check the end-to-end behaviour so no mocks are used in this test class. You will notice that the application.properties class unter ttest resources
uses a h2 in-memory database. this allows for easy testing without having to externally spin up a database to test the e2e functionality.



