package au.com.helixta.spannerexamples;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

import java.util.StringJoiner;

@Table(name = "cats")
public class Cat
{
    @PrimaryKey(keyOrder = 2)
    @Column(name = "CatId")
    private Long catId;

    @PrimaryKey
    @Column(name = "OwnerId")
    private Long ownerId;

    @Column(name = "CatName")
    private String catName;

    public Cat()
    {
    }

    public Cat(Long catId, Long ownerId, String catName)
    {
        this.catId = catId;
        this.ownerId = ownerId;
        this.catName = catName;
    }

    public Long getCatId()
    {
        return catId;
    }

    public void setCatId(Long catId)
    {
        this.catId = catId;
    }

    public Long getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(Long ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getCatName()
    {
        return catName;
    }

    public void setCatName(String catName)
    {
        this.catName = catName;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", Cat.class.getSimpleName() + "[", "]")
                .add("catId=" + catId)
                .add("ownerId=" + ownerId)
                .add("catName='" + catName + "'")
                .toString();
    }
}
