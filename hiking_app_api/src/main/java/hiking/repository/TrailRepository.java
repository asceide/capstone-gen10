package hiking.repository;

import hiking.models.Trail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrailRepository {
    List<Trail> findAll();

    Trail findById(int trailId);

    Trail add(Trail trail);

    boolean update(Trail trail);

    @Transactional
    boolean deleteById(int trailId);
}
