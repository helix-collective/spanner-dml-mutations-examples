package au.com.helixta.spannerexamples.cats;

import com.google.cloud.spanner.Key;
import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;

import java.util.Optional;

public interface CatRepository extends SpannerRepository<Cat, Key>
{
    Optional<Cat> findByCatName(String catName);
}
