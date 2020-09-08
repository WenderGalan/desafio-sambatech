package wendergalan.github.io.desafiosambatech.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wendergalan.github.io.desafiosambatech.model.entity.Media;
import wendergalan.github.io.desafiosambatech.model.repository.MediaRepository;
import wendergalan.github.io.desafiosambatech.service.BucketService;
import wendergalan.github.io.desafiosambatech.service.MediaService;
import wendergalan.github.io.desafiosambatech.service.MessageByLocaleService;
import wendergalan.github.io.desafiosambatech.utility.Utility;

import java.io.File;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final BucketService bucketService;
    private final MediaRepository mediaRepository;
    private final MessageByLocaleService message;
    private final Utility utility;

    @Override
    public Media save(MultipartFile multipartFile) throws Exception {
        File file = utility.convertMultiPartToFile(multipartFile);
        Media media = utility.recuperarMediaDoVideo(file.getAbsolutePath());
        String urlAWS = bucketService.uploadFile(file);
        media.setUrl(urlAWS);
        media.setNome(multipartFile.getOriginalFilename());
        // Recuperar a data no UTC 0
        media.setDataUpload(Instant.now().atOffset(ZoneOffset.UTC).toLocalDate());
        media = mediaRepository.save(media);
        // Deleta o arquivo temporário criado
        file.deleteOnExit();
        return media;
    }

    @Override
    public Optional<Media> getById(Integer id) {
        return mediaRepository.findById(id);
    }

    @Override
    public void delete(Media media) throws Exception {
        if (media == null || media.getId() == null)
            throw new Exception(message.getMessage("midia.delete.nao.nula"));
        media.setDeleted(true);
        mediaRepository.save(media);
    }

    @Override
    public Media update(Media media, MultipartFile multipartFile) throws Exception {
        // Converte o multipart
        File file = utility.convertMultiPartToFile(multipartFile);
        Media newMedia = utility.recuperarMediaDoVideo(file.getAbsolutePath());
        // Manda o nome do proprio arquivo para atualizar
        String urlAWS = bucketService.uploadFile(file, media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1));
        newMedia.setUrl(urlAWS);
        newMedia.setDataUpload(Instant.now().atOffset(ZoneOffset.UTC).toLocalDate());
        // Atualiza os atributos da media
        media.update(newMedia);
        mediaRepository.save(media);
        file.deleteOnExit();
        return media;
    }

    @Override
    public Page<Media> findAll(Boolean allMedias, Pageable pageable) {
        return mediaRepository.findAll(allMedias, pageable);
    }
}
