package au.com.helixta.spannerexamples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpannerSpringData
{
    private static final Logger log = LoggerFactory.getLogger(SpannerSpringData.class);

    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    public SpannerSpringData(CatRepository catRepository, OwnerRepository ownerRepository)
    {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
    }

    public void run()
    {
        ownerRepository.saveAll(List.of(new Owner(1L, "John Galah"),
                                        new Owner(2L, "Jennifer Cockatoo")));
        catRepository.saveAll(List.of(new Cat(1000L, 1L, "Dinah-Kah"),
                                      new Cat(1001L, 2L, "Janvier")));

        for (Cat cat : catRepository.findAll())
        {
            log.info("Cat: " + cat.getCatName());
        }
    }
}
