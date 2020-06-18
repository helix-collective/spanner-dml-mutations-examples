package au.com.helixta.spannerexamples.cats;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

import java.util.StringJoiner;

@Table(name = "owners")
public class Owner
{
    @PrimaryKey
    @Column(name = "OwnerId")
    private Long ownerId;

    @Column(name = "OwnerName")
    private String ownerName;

    public Owner()
    {
    }

    public Owner(Long ownerId, String ownerName)
    {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public Long getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(Long ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", Owner.class.getSimpleName() + "[", "]")
                .add("ownerId=" + ownerId)
                .add("ownerName='" + ownerName + "'")
                .toString();
    }
}
