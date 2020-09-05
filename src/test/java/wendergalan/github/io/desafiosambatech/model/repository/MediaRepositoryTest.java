package wendergalan.github.io.desafiosambatech.model.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wendergalan.github.io.desafiosambatech.model.entity.Media;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MediaRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private MediaRepository mediaRepository;

    @Test
    @DisplayName("Deve obter uma mídia por ID.")
    public void findByIdTest() {
        Media media = criarNovaMedia();
        entityManager.persist(media);

        // execucao
        Optional<Media> foundMedia = mediaRepository.findById(media.getId());

        assertThat(foundMedia.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar uma mídia.")
    public void saveMediaTest() {
        Media media = criarNovaMedia();

        Media savedMedia = mediaRepository.save(media);

        assertThat(savedMedia.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um mídia.")
    public void deleteBookTest() {
        Media media = criarNovaMedia();
        entityManager.persist(media);

        Media foundMedia = entityManager.find(Media.class, media.getId());

        mediaRepository.delete(foundMedia);

        Media deletedMedia = entityManager.find(Media.class, media.getId());
        assertThat(deletedMedia).isNull();
    }

    @Test
    @DisplayName("Deve buscar todos as mídias.")
    public void findAllMediaTest() {
        // Salva duas midias
        Media media = criarNovaMedia();
        entityManager.persist(media);

        Page<Media> result = mediaRepository.findAll(true, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(media);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve buscar todos as mídias não deletadas.")
    public void findAllMediaNoDeletedTest() {
        // Salva duas midias
        Media media = criarNovaMedia();
        media.setDeleted(true);
        entityManager.persist(media);

        Page<Media> result = mediaRepository.findAll(false, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    public static Media criarNovaMedia() {
        return Media.builder()
                .nome("video.mp4")
                .duracao(3546456L)
                .frameRate(60D)
                .fileSize(554455L)
                .dataUpload(LocalDate.now())
                .width(2560)
                .height(1080)
                .deleted(false)
                .build();
    }
}
