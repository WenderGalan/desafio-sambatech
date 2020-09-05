package wendergalan.github.io.desafiosambatech.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utility {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * @param file
     * @return
     * @throws IOException
     */
    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Gera o nome do arquivo
     * @param file
     * @return
     */
    public static String generateFileName(File file) {
        return new Date().getTime() + "-" + file.getName().replace(" ", "_");
    }
}
