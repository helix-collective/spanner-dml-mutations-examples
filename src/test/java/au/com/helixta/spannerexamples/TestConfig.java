package au.com.helixta.spannerexamples;

import com.google.cloud.spanner.DatabaseId;

/**
 * Static configuration for tests.
 */
public final class TestConfig
{
    public static final DatabaseId DATABASE_ID;

    static
    {
        String projectId = System.getProperty("test.projectId", "helix-sydney");
        String instanceId = System.getProperty("test.instanceId", "prunge-test");
        String databaseId = System.getProperty("test.databaseId", "cats");
        DATABASE_ID = DatabaseId.of(projectId, instanceId, databaseId);
    }

    public static final String JDBC_URI = "jdbc:cloudspanner:/" + DATABASE_ID.getName();

    private TestConfig()
    {
    }
}
