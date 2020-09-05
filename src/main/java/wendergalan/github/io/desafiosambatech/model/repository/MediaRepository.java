package wendergalan.github.io.desafiosambatech.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wendergalan.github.io.desafiosambatech.model.entity.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {

    @Query("SELECT m FROM Media m WHERE (?1 = TRUE OR m.deleted = ?1)")
    Page<Media> findAll(boolean allMedias, Pageable pageable);
}
