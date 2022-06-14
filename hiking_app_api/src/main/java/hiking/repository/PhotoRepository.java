package hiking.repository;

import hiking.models.SpotPhoto;
import hiking.models.TrailPhoto;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface PhotoRepository {
    List<SpotPhoto> findBySpotId(int spotId);

    List<TrailPhoto> findByTrailId(int trailId);

    @Transactional
    SpotPhoto addPhoto(SpotPhoto photo) throws Exception;

    @Transactional
    TrailPhoto addPhoto(TrailPhoto photo) throws Exception;

    @Transactional
    boolean updatePhoto(SpotPhoto photo) throws Exception;

    @Transactional
    boolean updatePhoto(TrailPhoto photo) throws Exception;

    @Transactional
    boolean deleteById(int photoId);
}
