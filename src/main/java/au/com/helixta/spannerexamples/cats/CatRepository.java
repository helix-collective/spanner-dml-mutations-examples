package au.com.helixta.spannerexamples.cats;

import com.google.cloud.spanner.Key;
import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;

public interface CatRepository extends SpannerRepository<Cat, Key>
{
}
