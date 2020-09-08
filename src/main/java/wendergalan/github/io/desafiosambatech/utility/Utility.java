package wendergalan.github.io.desafiosambatech.utility;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import wendergalan.github.io.desafiosambatech.model.entity.Media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class Utility {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * @param file
     * @return
     * @throws IOException
     */
    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Gera o nome do arquivo
     *
     * @param file
     * @return
     */
    public String generateFileName(File file) {
        return new Date().getTime() + "-" + file.getName().replace(" ", "_");
    }

    /**
     * @param url
     * @return
     * @throws Exception
     */
    public Media recuperarMediaDoVideo(String url) throws Exception {
        // Primeiro crea um containet do xuggler
        IContainer container = IContainer.make();

        int result = container.open(url, IContainer.Type.READ, null);

        // check if the operation was successful
        // Checa se a operação teve sucesso
        if (result < 0)
            return null;

        // Recupera a quantidade stream que foram abertas na chamada
        int numStreams = container.getNumStreams();

        // Recupera a duração
        long duration = container.getDuration();

        // Recupera o tamanho do arquivo
        long fileSize = container.getFileSize();

        // Recupera o bit rate do arquivo
        long bitRate = container.getBitRate();

        int width = 0;
        int height = 0;
        double frameRate = 0;
        for (int i = 0; i < numStreams; i++) {
            // Procura o objeto stream
            IStream stream = container.getStream(i);

            // Obtem o decodificador para decodificar ess stream
            IStreamCoder coder = stream.getStreamCoder();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                width = coder.getWidth();
                height = coder.getHeight();
                frameRate = coder.getFrameRate().getDouble();
            }
        }

        return Media.builder()
                .duracao(duration)
                .fileSize(fileSize)
                .bitRate(bitRate)
                .width(width)
                .height(height)
                .frameRate(frameRate)
                .build();
    }
}
