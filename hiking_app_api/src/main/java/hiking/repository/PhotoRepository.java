package hiking.repository;

import hiking.models.SpotPhoto;
import hiking.models.TrailPhoto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PhotoRepository {
    List<SpotPhoto> findBySpotId(int spotId);

    List<TrailPhoto> findByTrailId(int trailId);

    @Transactional
    SpotPhoto addPhoto(SpotPhoto photo) throws DataIntegrityViolationException;

    @Transactional
    TrailPhoto addPhoto(TrailPhoto photo) throws DataIntegrityViolationException;

    @Transactional
    boolean updatePhoto(SpotPhoto photo) throws DataIntegrityViolationException;

    @Transactional
    boolean updatePhoto(TrailPhoto photo) throws DataIntegrityViolationException;

    @Transactional
    boolean deleteById(int photoId);
}
