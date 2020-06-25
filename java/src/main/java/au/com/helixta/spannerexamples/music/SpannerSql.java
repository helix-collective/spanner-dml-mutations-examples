package au.com.helixta.spannerexamples.music;

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
            if (tableExists(con, "Singers"))
            {
                //Delete existing data
                try (PreparedStatement stat = con.prepareStatement("delete from Singers where SingerId > 0"))
                {
                    stat.executeUpdate();
                }

                return;
            }

            try (PreparedStatement stat = con.prepareStatement(
                    "CREATE TABLE Singers ("
                    + "  SingerId   INT64 NOT NULL,"
                    + "  FirstName  STRING(1024),"
                    + "  LastName   STRING(1024),"
                    + "  SingerInfo BYTES(MAX)"
                    + ") PRIMARY KEY (SingerId)"))
            {
                stat.execute();
            }
            try (PreparedStatement stat = con.prepareStatement(
                    "CREATE TABLE Albums ("
                    + "  SingerId     INT64 NOT NULL,"
                    + "  AlbumId      INT64 NOT NULL,"
                    + "  AlbumTitle   STRING(MAX)"
                    + ") PRIMARY KEY (SingerId, AlbumId),"
                    + "  INTERLEAVE IN PARENT Singers ON DELETE CASCADE"))
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
            try (PreparedStatement stat = con.prepareStatement("insert into Singers (SingerId, FirstName, LastName)  values (?, ?, ?)"
            ))
            {
                stat.setLong(1, 10L);
                stat.setString(2, "Floor");
                stat.setString(3, "Jansen");
                stat.executeUpdate();

                stat.setLong(1, 20L);
                stat.setString(2, "Charlotte");
                stat.setString(3, "Wessels");
                stat.executeUpdate();
            }
            try (PreparedStatement stat = con.prepareStatement("insert into Albums (SingerId, AlbumId, AlbumTitle) values (?, ?, ?)"
            ))
            {
                stat.setLong(1, 10L);
                stat.setLong(2, 1000L);
                stat.setString(3, "After Forever");
                stat.executeUpdate();

                stat.setLong(1, 20L);
                stat.setLong(2, 2000L);
                stat.setString(3, "Lucidity");
                stat.executeUpdate();
            }

            try (PreparedStatement stat = con.prepareStatement("select FirstName, LastName from Singers");
                 ResultSet rs = stat.executeQuery())
            {
                while (rs.next())
                {
                    log.info("Singer: " + rs.getString("FirstName") + " " + rs.getString("LastName"));
                }
            }
        }
    }
}
