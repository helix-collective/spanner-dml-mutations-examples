package au.com.helixta.spannerexamples;

import com.google.cloud.spanner.Key;
import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;

public interface OwnerRepository extends SpannerRepository<Owner, Key>
{
}
