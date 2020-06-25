package au.com.helixta.spannerexamples.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpannerSpringData
{
    private static final Logger log = LoggerFactory.getLogger(SpannerSpringData.class);

    private final AlbumRepository albumRepository;
    private final SingerRepository singerRepository;

    public SpannerSpringData(AlbumRepository albumRepository,
                             SingerRepository singerRepository)
    {
        this.albumRepository = albumRepository;
        this.singerRepository = singerRepository;
    }

    public void run()
    {
        singerRepository.saveAll(List.of(new Singer(10L, "Floor", "Jansen"),
                                         new Singer(20L, "Charlotte", "Wessels")));
        albumRepository.saveAll(List.of(new Album(10L, 1000L, "After Forever"),
                                        new Album(20L, 1001L, "Lucidity")));

        for (Singer singer : singerRepository.findAll())
        {
            log.info("Singer: " + singer.getFirstName() + " " + singer.getLastName());
        }
    }
}
