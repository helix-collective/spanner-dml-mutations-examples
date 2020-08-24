package au.com.helixta.spannerexamples;

import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.DatabaseNotFoundException;
import com.google.cloud.spanner.Instance;
import com.google.cloud.spanner.InstanceAdminClient;
import com.google.cloud.spanner.InstanceConfig;
import com.google.cloud.spanner.InstanceId;
import com.google.cloud.spanner.InstanceInfo;
import com.google.cloud.spanner.InstanceNotFoundException;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Static configuration for tests.
 */
public final class TestConfig
{
    public static final String EMULATOR_URI = System.getProperty("test.spanner.emulator");

    public static final DatabaseId DATABASE_ID;

    static
    {
        String instanceId = getProperty("test.instanceId", "test");
        String databaseId = getProperty("test.databaseId", "test");
        String projectId;
        if (EMULATOR_URI != null)
        {
            try (Spanner spanner = SpannerOptions.newBuilder().setEmulatorHost(EMULATOR_URI).build().getService())
            {
                InstanceAdminClient instanceAdminClient = spanner.getInstanceAdminClient();
                InstanceConfig instanceConfig = instanceAdminClient.listInstanceConfigs().iterateAll().iterator().next();
                projectId = instanceConfig.getId().getProject();
            }
        }
        else
        {
            projectId = getProperty("test.projectId", "helix-sydney");
        }
        DATABASE_ID = DatabaseId.of(projectId, instanceId, databaseId);
    }

    public static final String JDBC_URI;
    static
    {
        if (EMULATOR_URI == null)
            JDBC_URI = "jdbc:cloudspanner:/" + DATABASE_ID.getName();
        else
            JDBC_URI = "jdbc:cloudspanner://" + EMULATOR_URI + "/" + DATABASE_ID.getName() + "?usePlainText=true";
    }

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

    public static void setUpEmulator()
    throws InterruptedException, ExecutionException
    {
        if (EMULATOR_URI != null)
        {
            try (Spanner spanner = SpannerOptions.newBuilder().setEmulatorHost(EMULATOR_URI).build().getService())
            {
                InstanceAdminClient instanceAdminClient = spanner.getInstanceAdminClient();
                InstanceConfig instanceConfig = instanceAdminClient.listInstanceConfigs().iterateAll().iterator().next();

                //Create instance
                Instance instance;
                try
                {
                    instance = instanceAdminClient.getInstance(DATABASE_ID.getInstanceId().getInstance());
                }
                catch (InstanceNotFoundException e)
                {
                    instance = instanceAdminClient.createInstance(
                            InstanceInfo.newBuilder(
                                    InstanceId.of(instanceConfig.getId().getProject(), DATABASE_ID.getInstanceId().getInstance()))
                                        .setNodeCount(1).setDisplayName("Test")
                                        .setInstanceConfigId(instanceConfig.getId())
                                        .build())
                                                  .get();
                }

                //Create database if not already exists
                try
                {
                    instance.getDatabase(DATABASE_ID.getDatabase());
                }
                catch (DatabaseNotFoundException e)
                {
                    instance.createDatabase(DATABASE_ID.getDatabase(), List.of()).get();
                }
            }
        }
    }
}
