package au.com.helixta.spannerexamples.music;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

import java.util.StringJoiner;

@Table(name = "Singers")
public class Singer
{
    @PrimaryKey
    @Column(name = "SingerId")
    private Long singerId;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    public Singer()
    {
    }

    public Singer(Long singerId, String firstName, String lastName)
    {
        this.singerId = singerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getSingerId()
    {
        return singerId;
    }

    public void setSingerId(Long singerId)
    {
        this.singerId = singerId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", Singer.class.getSimpleName() + "[", "]")
                .add("singerId=" + singerId)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .toString();
    }
}
