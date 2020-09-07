package wendergalan.github.io.desafiosambatech.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wendergalan.github.io.desafiosambatech.model.entity.Media;
import wendergalan.github.io.desafiosambatech.model.repository.MediaRepository;
import wendergalan.github.io.desafiosambatech.service.impl.MediaServiceImpl;
import wendergalan.github.io.desafiosambatech.service.impl.MessageByLocaleServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wendergalan.github.io.desafiosambatech.model.repository.MediaRepositoryTest.criarNovaMedia;

@ExtendWith(SpringExtension.class)
public class MediaServiceTest {

    MessageByLocaleService message;
    MediaService mediaService;

    @MockBean
    MediaRepository repository;

    @MockBean
    MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        this.message = new MessageByLocaleServiceImpl(messageSource);
        this.mediaService = new MediaServiceImpl(null, repository, message);
    }

    @Test
    @DisplayName("Deve obter uma mídia por ID.")
    public void getByIdTest() {
        int id = 11;
        Media media = criarNovaMedia();
        media.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(media));

        Optional<Media> foundMedia = mediaService.getById(id);

        assertThat(foundMedia.isPresent()).isTrue();
        assertThat(foundMedia.get().getId()).isEqualTo(media.getId());
        assertThat(foundMedia.get().getNome()).isEqualTo(media.getNome());
        assertThat(foundMedia.get().getDataUpload()).isEqualTo(media.getDataUpload());
        assertThat(foundMedia.get().getFileSize()).isEqualTo(media.getFileSize());
        assertThat(foundMedia.get().getFrameRate()).isEqualTo(media.getFrameRate());
    }

    @Test
    @DisplayName("Deve dar erro ao enviar nulo ao método.")
    public void deleteByIdExceptionTest() {
        Throwable exception = Assertions.catchThrowable(() -> mediaService.delete(null));

        assertThat(exception)
                .isInstanceOf(Exception.class)
                .hasMessage(message.getMessage("midia.delete.nao.nula"));
    }

    @Test
    @DisplayName("Deve deletar marcar como deletado a mídia.")
    public void deleteByIdTest() {
        Media media = Media.builder().id(1).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mediaService.delete(media));

        Mockito.verify(repository, Mockito.times(1)).save(media);
    }

    @Test
    @DisplayName("Deve buscar todas as mídias.")
    public void findAllMediaTest() {
        Media media = criarNovaMedia();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Media> lista = Collections.singletonList(media);
        Page<Media> page = new PageImpl<Media>(lista, pageRequest, 1);

        when(repository.findAll(any(Boolean.class), any(PageRequest.class))).thenReturn(page);

        Page<Media> result = mediaService.findAll(true, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve buscar todas as mídias não deletadas.")
    public void findAllMediaNoDeletedTest() {
        Media media = criarNovaMedia();
        media.setDeleted(false);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Media> lista = Collections.singletonList(media);
        Page<Media> page = new PageImpl<Media>(lista, pageRequest, 1);

        when(repository.findAll(any(Boolean.class), any(PageRequest.class))).thenReturn(page);

        Page<Media> result = mediaService.findAll(false, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
