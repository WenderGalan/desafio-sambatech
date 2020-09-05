package wendergalan.github.io.desafiosambatech.api.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wendergalan.github.io.desafiosambatech.model.entity.Media;
import wendergalan.github.io.desafiosambatech.utility.Utility;
import wendergalan.github.io.desafiosambatech.utility.VideoUtility;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medias")
public class MediaController {
    // Implements endpoins
    @PostMapping
    public ResponseEntity salvar(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        File file = Utility.convertMultiPartToFile(multipartFile);
        Media media = VideoUtility.recuperarMediaDoVideo(file.getAbsolutePath());
        file.delete();
        return ResponseEntity.ok(media);
    }
}
