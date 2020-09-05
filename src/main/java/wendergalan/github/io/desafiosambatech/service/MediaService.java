package wendergalan.github.io.desafiosambatech.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import wendergalan.github.io.desafiosambatech.model.entity.Media;

import java.util.Optional;

public interface MediaService {

    Media save(MultipartFile multipartFile) throws Exception;

    Optional<Media> getById(Integer id);

    void delete(Media media) throws Exception;

    Media update(Media media, MultipartFile multipartFile) throws Exception;

    Page<Media> findAll(Boolean allMedias, Pageable pageable);
}
