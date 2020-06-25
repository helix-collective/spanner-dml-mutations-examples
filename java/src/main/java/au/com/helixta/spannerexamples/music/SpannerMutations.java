package au.com.helixta.spannerexamples.music;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.Statement;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpannerMutations
{
    private static final Logger log = LoggerFactory.getLogger(SpannerMutations.class);

    private final Spanner spanner;
    private final DatabaseId databaseId;

    public SpannerMutations(Spanner spanner, DatabaseId databaseId)
    {
        this.spanner = spanner;
        this.databaseId = databaseId;
    }

    public void run()
    {
        DatabaseClient dbClient = spanner.getDatabaseClient(databaseId);

        dbClient.write(ImmutableList.of(
                Mutation.newInsertBuilder("Singers").set("SingerId").to(10L)
                                                    .set("FirstName").to("Floor")
                                                    .set("LastName").to("Jansen")
                                                    .build(),
                Mutation.newInsertBuilder("Singers").set("SingerId").to(20L)
                                                    .set("FirstName").to("Charlotte")
                                                    .set("LastName").to("Wessels")
                                                    .build(),
                Mutation.newInsertBuilder("Albums").set("SingerId").to(10L)
                                                   .set("AlbumId").to(1000L)
                                                   .set("AlbumTitle").to("After Forever")
                                                   .build(),
                Mutation.newInsertBuilder("Albums").set("SingerId").to(20L)
                                                   .set("AlbumId").to(2000L)
                                                   .set("AlbumTitle").to("Lucidity")
                                                   .build()
                ));

        try (ResultSet rs = dbClient.singleUseReadOnlyTransaction().executeQuery(Statement.newBuilder("select FirstName, LastName from Singers").build()))
        {
            while (rs.next())
            {
                log.info("Singer: " + rs.getString("FirstName") + " " + rs.getString("LastName"));
            }
        }
    }
}
