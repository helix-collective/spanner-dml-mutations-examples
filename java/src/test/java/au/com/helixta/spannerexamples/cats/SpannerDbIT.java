package au.com.helixta.spannerexamples.cats;

import com.google.cloud.spanner.Key;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDatabaseConfiguration.class)
class SpannerDbIT
{
    @Autowired
    private CatRepository catRepository;
    
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    void resetDatabase()
    {
        ownerRepository.deleteAll();
        ownerRepository.save(new Owner(1L, "John Galah"));
        ownerRepository.save(new Owner(2L, "Penelope Ibis"));
        catRepository.deleteAll();
        catRepository.save(new Cat(10L, 1L, "Dinah"));
        catRepository.save(new Cat(20L, 2L, "Janvier"));
    }

    @Test
    void canFindOwnersById()
    {
        Owner owner = ownerRepository.findById(Key.of(1L)).get();
        assertThat(owner.getOwnerName()).isEqualTo("John Galah");
    }

    @Test
    void canFindOwnersByName()
    {
        Owner owner = ownerRepository.findByOwnerName("John Galah").get();
        assertThat(owner.getOwnerId()).isEqualTo(1L);
    }
}
