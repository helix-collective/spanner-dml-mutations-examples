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
        String projectId = getProperty("test.projectId", "helix-sydney");
        String instanceId = getProperty("test.instanceId", "test");
        String databaseId = getProperty("test.databaseId", "test");
        DATABASE_ID = DatabaseId.of(projectId, instanceId, databaseId);
    }

    public static final String JDBC_URI = "jdbc:cloudspanner:/" + DATABASE_ID.getName();

    private TestConfig()
    {
    }

    private static String getProperty(String name, String defaultValue)
    {
        String value = System.getProperty(name);
        if (value != null && value.trim().isEmpty())
            value = null;
        if (value == null)
            value = defaultValue;

        return value;
    }
}
