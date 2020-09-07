package wendergalan.github.io.desafiosambatech.utility;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static wendergalan.github.io.desafiosambatech.utility.VideoUtility.recuperarMediaDoVideo;

@ExtendWith(SpringExtension.class)
public class VideoUtilityTest {

    @Test
    @DisplayName("Lança uma exceção ao tentar recuperar mídia de um caminho inválido.")
    public void recuperaMediaDoVideoExceptionTest() {
        Throwable exception = Assertions.catchThrowable(() -> recuperarMediaDoVideo("CAMINHO_DO_VIDEO/video.mp4"));

        assertThat(exception)
                .isInstanceOf(Exception.class)
                .hasMessage("Falha ao abrir o arquivo.");
    }
}
