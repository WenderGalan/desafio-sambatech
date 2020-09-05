package wendergalan.github.io.desafiosambatech.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wendergalan.github.io.desafiosambatech.model.entity.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
}
