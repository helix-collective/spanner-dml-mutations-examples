# Spanner Examples

## Project Setup

- Create a Spanner instance to work with.  The
[Google Cloud Console](https://console.cloud.google.com/) can be used to do this.
- When running tests, set `test.projectId`, `test.instanceId` and
`test.databaseId` system properties to appropriate values for your new database.
- The `GOOGLE_APPLICATION_CREDENTIALS` environment variable can be set
to supply credentials for accessing the Spanner database.  This JSON file
can be downloaded from the cloud console under the IAM & Admin / Service Accounts
section.

## Running tests

```
mvn test
```

or use your IDE.

Supply system properties using appropriate mechanisms.
