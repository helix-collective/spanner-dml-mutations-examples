package au.com.helixta.spannerexamples.music;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

import java.util.StringJoiner;

@Table(name = "Albums")
public class Album
{
    @PrimaryKey
    @Column(name = "SingerId")
    private Long singerId;

    @PrimaryKey(keyOrder = 2)
    @Column(name = "AlbumId")
    private Long albumId;

    @Column(name = "AlbumTitle")
    private String title;

    public Album()
    {
    }

    public Album(Long singerId, Long albumId, String title)
    {
        this.singerId = singerId;
        this.albumId = albumId;
        this.title = title;
    }

    public Long getSingerId()
    {
        return singerId;
    }

    public void setSingerId(Long singerId)
    {
        this.singerId = singerId;
    }

    public Long getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(Long albumId)
    {
        this.albumId = albumId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", Album.class.getSimpleName() + "[", "]")
                .add("singerId=" + singerId)
                .add("albumId=" + albumId)
                .add("title='" + title + "'")
                .toString();
    }
}

