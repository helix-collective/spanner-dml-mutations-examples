package au.com.helixta.spannerexamples;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.data.spanner.repository.config.EnableSpannerRepositories;

@SpringBootApplication
@EnableSpannerRepositories
public class AppConfiguration
{
}
