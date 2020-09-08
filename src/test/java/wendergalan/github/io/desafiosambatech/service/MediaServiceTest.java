package wendergalan.github.io.desafiosambatech.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wendergalan.github.io.desafiosambatech.model.entity.Media;
import wendergalan.github.io.desafiosambatech.model.repository.MediaRepository;
import wendergalan.github.io.desafiosambatech.service.impl.MediaServiceImpl;
import wendergalan.github.io.desafiosambatech.service.impl.MessageByLocaleServiceImpl;
import wendergalan.github.io.desafiosambatech.utility.Utility;

import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static wendergalan.github.io.desafiosambatech.model.repository.MediaRepositoryTest.criarNovaMedia;

@ExtendWith(SpringExtension.class)
public class MediaServiceTest {

    MessageByLocaleService message;
    MediaService mediaService;

    @MockBean
    BucketService bucketService;

    @MockBean
    MediaRepository repository;

    @MockBean
    Utility utility;

    @MockBean
    MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        this.message = new MessageByLocaleServiceImpl(messageSource);
        this.mediaService = new MediaServiceImpl(bucketService, repository, message, utility);
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

    @Test
    @DisplayName("Deve salvar uma mídia.")
    public void saveMediaTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "video.mp4", MediaType.APPLICATION_OCTET_STREAM_VALUE, "File".getBytes());

        Media media = criarNovaMedia();
        given(utility.recuperarMediaDoVideo(anyString())).willReturn(media);
        given(utility.convertMultiPartToFile(any())).willReturn(new File("video.mp4"));
        given(utility.generateFileName(any())).willReturn("video.mp4");
        given(bucketService.uploadFile(any(), anyString())).willReturn("http://url/video.mp4");
        given(repository.save(media))
                .willReturn(
                        Media.builder()
                                .id(1)
                                .dataUpload(LocalDate.now())
                                .build());

        Media saveMedia = mediaService.save(multipartFile);

        assertThat(saveMedia.getId()).isNotNull();
        assertThat(saveMedia.getDataUpload()).isNotNull();
    }

    @Test
    @DisplayName("Deve atualizar uma mídia.")
    public void updateMediaTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "video.mp4", MediaType.APPLICATION_OCTET_STREAM_VALUE, "File".getBytes());

        Media media = criarNovaMedia();
        media.setUrl("https://url/video.mp4");
        media.setId(1);

        given(utility.recuperarMediaDoVideo(anyString())).willReturn(media);
        given(utility.convertMultiPartToFile(any())).willReturn(new File("video.mp4"));
        given(utility.generateFileName(any())).willReturn("video.mp4");
        given(bucketService.uploadFile(any(), anyString())).willReturn("http://url/video.mp4");
        given(repository.save(media))
                .willReturn(
                        Media.builder()
                                .id(1)
                                .url("https://url/video.mp4")
                                .dataUpload(LocalDate.now())
                                .build());

        Media updateMedia = mediaService.update(media, multipartFile);

        assertThat(updateMedia.getId()).isEqualTo(media.getId());
        assertThat(updateMedia.getDataUpload()).isEqualTo(media.getDataUpload());
        assertThat(updateMedia.getUrl()).isEqualTo(media.getUrl());
    }
}
