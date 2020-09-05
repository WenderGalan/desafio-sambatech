package wendergalan.github.io.desafiosambatech.api.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import wendergalan.github.io.desafiosambatech.model.entity.Media;
import wendergalan.github.io.desafiosambatech.service.MediaService;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medias")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
    @ApiOperation("Cria uma mídia.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity save(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        return ResponseEntity
                .created(MvcUriComponentsBuilder.fromController(getClass()).build().toUri())
                .body(mediaService.save(multipartFile));
    }

    @PutMapping("/{id}")
    @ApiOperation("Altera um mídia.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity update(@PathVariable("id") Integer id,
                                 @RequestParam("file") MultipartFile file) throws Exception {
        Optional<Media> media = mediaService.getById(id);
        if (media.isPresent())
            return ResponseEntity.ok().body(mediaService.update(media.get(), file));
        throw new ResponseStatusException(NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deleta a mídia pelo ID.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity deleteById(@PathVariable Integer id) throws Exception {
        Media media = mediaService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        mediaService.delete(media);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ApiOperation("Busca a mídia por ID.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity buscarPorId(@PathVariable Integer id) {
        Media media = mediaService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        return ResponseEntity.ok(media);
    }

    @GetMapping
    @ApiOperation(value = "Busca todas as mídias.")
    @ApiResponses(@ApiResponse(code = 200, message = "Success", response = Media.class))
    public ResponseEntity<Iterable<Media>> findAllMedias(@RequestParam("allMedias") boolean allMedias, Pageable pageable) {
        return new ResponseEntity<>(mediaService.findAll(allMedias, pageable), OK);
    }
}
