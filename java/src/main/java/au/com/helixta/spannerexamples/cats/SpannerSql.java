package au.com.helixta.spannerexamples.cats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpannerSql
{
    private static final Logger log = LoggerFactory.getLogger(SpannerSql.class);

    private final String jdbcUri;

    public SpannerSql(String jdbcUri)
    {
        this.jdbcUri = jdbcUri;
    }

    private boolean tableExists(Connection con, String tableName)
    throws SQLException
    {
        try (ResultSet rs = con.getMetaData().getTables(null, null, tableName, null))
        {
            return rs.next();
        }
    }

    public void setUpTables()
    throws SQLException
    {
        try (Connection con = DriverManager.getConnection(jdbcUri))
        {
            if (tableExists(con, "Owners"))
            {
                //Delete existing data
                try (PreparedStatement stat = con.prepareStatement("delete from Owners where OwnerId > 0"))
                {
                    stat.executeUpdate();
                }

                return;
            }

            try (PreparedStatement stat = con.prepareStatement(
                    "create table Owners (\n" +
                    "  OwnerId INT64 NOT NULL,\n" +
                    "  OwnerName STRING(MAX) NOT NULL\n" +
                    ") PRIMARY KEY (OwnerId)"))
            {
                stat.execute();
            }
            try (PreparedStatement stat = con.prepareStatement(
                    "create table Cats (\n" +
                    "  CatId INT64 NOT NULL,\n" +
                    "  OwnerId INT64 NOT NULL,\n" +
                    "  CatName String(MAX) NOT NULL\n" +
                    ") PRIMARY KEY (OwnerId, CatId),\n" +
                    "  INTERLEAVE IN PARENT Owners ON DELETE CASCADE"))
            {
                stat.execute();
            }
        }
    }

    public void run()
    throws SQLException
    {
        try (Connection con = DriverManager.getConnection(jdbcUri))
        {
            //Insert some data
            try (PreparedStatement stat = con.prepareStatement(
                    "insert into owners (OwnerId, OwnerName) values (?, ?)"
            ))
            {
                stat.setLong(1, 1L);
                stat.setString(2, "John Galah");
                stat.executeUpdate();

                stat.setLong(1, 2L);
                stat.setString(2, "Jennifer Cockatoo");
                stat.executeUpdate();
            }
            try (PreparedStatement stat = con.prepareStatement(
                    "insert into cats (CatId, OwnerId, CatName) values (?, ?, ?)"
            ))
            {
                stat.setLong(1, 1000L);
                stat.setLong(2, 1L);
                stat.setString(3, "Dinah-Kah");
                stat.executeUpdate();

                stat.setLong(1, 1001L);
                stat.setLong(2, 2L);
                stat.setString(3, "Janvier");
                stat.executeUpdate();
            }

            try (PreparedStatement stat = con.prepareStatement("select CatName from cats");
                 ResultSet rs = stat.executeQuery())
            {
                while (rs.next())
                {
                    log.info("Cat: " + rs.getString("CatName"));
                }
            }
        }
    }
}
