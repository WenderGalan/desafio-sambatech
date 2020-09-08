package wendergalan.github.io.desafiosambatech.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wendergalan.github.io.desafiosambatech.model.entity.Media;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class UtilityTest {

    Utility utility;

    @BeforeEach
    public void setUp() {
        utility = new Utility();
    }

    @Test
    @DisplayName("Deve retorna nulo ao tentar recuperar uma mídia de um arquivo inválido.")
    public void recuperaMediaDoVideoNullTest() throws Exception {
        Media media = utility.recuperarMediaDoVideo("CAMINHO_DO_VIDEO/video.mp4");

        assertThat(media)
                .isNull();
    }

    @Test
    @DisplayName("Deve gerar o nome do arquivo.")
    public void generateFileNameTest() {
        String nameOfFile = utility.generateFileName(new File("open-api.json"));

        assertThat(nameOfFile)
                .isNotNull();
    }

    @Test
    @DisplayName("Deve converter um multiparfile para file.")
    public void convertMultiPartToFileTest() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "video.mp4", MediaType.APPLICATION_OCTET_STREAM_VALUE, "File".getBytes());
        File file = utility.convertMultiPartToFile(multipartFile);
        assertThat(file).isNotNull();
    }
}
