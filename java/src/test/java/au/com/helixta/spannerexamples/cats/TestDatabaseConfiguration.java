package au.com.helixta.spannerexamples.cats;

import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Instance;
import com.google.cloud.spanner.InstanceAdminClient;
import com.google.cloud.spanner.InstanceConfig;
import com.google.cloud.spanner.InstanceId;
import com.google.cloud.spanner.InstanceInfo;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.data.spanner.core.admin.DatabaseIdProvider;
import org.springframework.cloud.gcp.data.spanner.repository.config.EnableSpannerRepositories;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableSpannerRepositories
public class TestDatabaseConfiguration
{
    /**
     * Normally configured in application.properties, but for testing we'll make this configurable.
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider()
    {
        String projectProperty = getProperty("test.projectId", "test-project");
        String instanceProperty = getProperty("test.instanceId", "test-instance");
        String databaseProperty = getProperty("test.databaseId", "cats");
        String emulatorHost = System.getenv("SPANNER_EMULATOR_HOST");
        if (emulatorHost != null)
        {
            try (Spanner spanner = SpannerOptions.newBuilder().setEmulatorHost(emulatorHost).build().getService())
            {
                //Detect instance config/project to use with the emulator
                InstanceAdminClient instanceAdminClient = spanner.getInstanceAdminClient();
                InstanceConfig instanceConfig = instanceAdminClient.listInstanceConfigs().iterateAll().iterator()
                                                                   .next();
                projectProperty = instanceConfig.getId().getProject();

                //Delete and recreate new instance in emulator
                instanceAdminClient.deleteInstance(instanceProperty);
                Instance instance = instanceAdminClient.createInstance(InstanceInfo.newBuilder(
                        InstanceId.of(instanceConfig.getId().getProject(), instanceProperty))
                                                                                   .setInstanceConfigId(
                                                                                           instanceConfig.getId())
                                                                                   .build()).get();

                //Recreate database
                List<String> dbCreateStatements = List.of(
                        "create table Owners (\n" +
                                "  OwnerId INT64 NOT NULL,\n" +
                                "  OwnerName STRING(MAX) NOT NULL\n" +
                                ") PRIMARY KEY (OwnerId)",

                        "create table Cats (\n" +
                                "  CatId INT64 NOT NULL,\n" +
                                "  OwnerId INT64 NOT NULL,\n" +
                                "  CatName String(MAX) NOT NULL\n" +
                                ") PRIMARY KEY (OwnerId, CatId),\n" +
                                "  INTERLEAVE IN PARENT Owners ON DELETE CASCADE"
                );
                instance.createDatabase(databaseProperty, dbCreateStatements).get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }

        DatabaseId databaseId = DatabaseId.of(projectProperty, instanceProperty, databaseProperty);

        return () -> databaseId;
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
