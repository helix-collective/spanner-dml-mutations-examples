package au.com.helixta.spannerexamples.music;

import com.google.cloud.spanner.Key;
import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;

public interface AlbumRepository extends SpannerRepository<Album, Key>
{
}
