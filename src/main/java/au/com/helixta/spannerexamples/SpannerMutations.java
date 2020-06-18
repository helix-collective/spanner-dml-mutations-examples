package au.com.helixta.spannerexamples;

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
                Mutation.newInsertBuilder("Owners").set("OwnerId").to(1L)
                                                   .set("OwnerName").to("John Galah")
                                                   .build(),
                Mutation.newInsertBuilder("Owners").set("OwnerId").to(2L)
                                                   .set("OwnerName").to("Jennifer Cockatoo")
                                                   .build(),
                Mutation.newInsertBuilder("Cats").set("CatId").to(1000L)
                                                 .set("OwnerId").to(1L)
                                                 .set("CatName").to("Dinah-Kah")
                                                 .build(),
                Mutation.newInsertBuilder("Cats").set("CatId").to(1001L)
                                                 .set("OwnerId").to(2L)
                                                 .set("CatName").to("Janvier")
                                                 .build()
                ));

        try (ResultSet rs = dbClient.singleUseReadOnlyTransaction().executeQuery(Statement.newBuilder("select CatName from cats").build()))
        {
            while (rs.next())
            {
                log.info("Cat: " + rs.getString("CatName"));
            }
        }
    }
}
