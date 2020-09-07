package wendergalan.github.io.desafiosambatech.api.resource;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wendergalan.github.io.desafiosambatech.model.entity.Media;
import wendergalan.github.io.desafiosambatech.model.repository.MediaRepository;
import wendergalan.github.io.desafiosambatech.service.MediaService;
import wendergalan.github.io.desafiosambatech.service.MessageByLocaleService;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wendergalan.github.io.desafiosambatech.model.repository.MediaRepositoryTest.criarNovaMedia;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MediaController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
public class MediaControllerTest {

    static String MEDIA_API = "/medias";

    @MockBean
    MessageByLocaleService message;

    @MockBean
    MediaService mediaService;

    @Autowired
    MockMvc mvc;

    @MockBean
    MessageSource messageSource;

    @MockBean
    MediaRepository repository;

    @MockBean
    AmazonS3 s3Client;

    @Test
    @DisplayName("Deve obter informações de uma mídia.")
    public void getMediaDetailsTest() throws Exception {
        int id = 1;
        Media media = criarNovaMedia();
        media.setId(id);

        BDDMockito.given(mediaService.getById(id)).willReturn(Optional.of(media));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(MEDIA_API.concat("/" + id))
                .accept(APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("nome").value(media.getNome()))
                .andExpect(jsonPath("frameRate").value(media.getFrameRate()))
                .andExpect(jsonPath("duracao").value(media.getDuracao()));
    }

    @Test
    @DisplayName("Deve retornar not found quando a mídia procurada não existir.")
    public void mediaNotFoundTest() throws Exception {
        BDDMockito.given(mediaService.getById(anyInt())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(MEDIA_API.concat("/" + 1))
                .accept(APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve marcar como deletado uma mídia.")
    public void deleteMediaTest() throws Exception {
        Media media = criarNovaMedia();
        media.setId(1);
        BDDMockito.given(mediaService.getById(anyInt())).willReturn(Optional.of(media));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(MEDIA_API.concat("/" + 1))
                .accept(APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve buscar todas as midias.")
    public void findAllMediasTest() throws Exception {
        int id = 1;
        Media media = criarNovaMedia();
        media.setId(id);

        BDDMockito.given(mediaService.findAll(anyBoolean(), any(Pageable.class)))
                .willReturn(new PageImpl<Media>(Collections.singletonList(media), PageRequest.of(0, 100), 1));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(MEDIA_API.concat("?allMedias=true&page=0&size=100"))
                .accept(APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    @Test
    @DisplayName("Deve salvar uma mídia.")
    public void saveMediaTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", MediaType.APPLICATION_OCTET_STREAM_VALUE, "File".getBytes());

        Media media = criarNovaMedia();
        media.setId(1);

        BDDMockito.given(mediaService.save(Mockito.any())).willReturn(media);

        mvc
                .perform(multipart(MEDIA_API).file(file).accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(media.getId()))
                .andExpect(jsonPath("frameRate").value(media.getFrameRate()))
                .andExpect(jsonPath("duracao").value(media.getDuracao()))
                .andExpect(jsonPath("fileSize").value(media.getFileSize()));
    }

    @Test
    @DisplayName("Deve dar erro ao salvar uma mídia que não seja um vídeo.")
    public void saveMediaExceptionTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "video.txt", MediaType.APPLICATION_OCTET_STREAM_VALUE, "File".getBytes());

        Media media = criarNovaMedia();
        media.setId(1);

        BDDMockito.given(mediaService.save(Mockito.any())).willThrow(Exception.class);

        mvc
                .perform(multipart(MEDIA_API).file(file).accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
