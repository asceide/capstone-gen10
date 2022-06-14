package hiking.repository;

import hiking.models.SpotPhoto;
import hiking.models.TrailPhoto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PhotoRepository {
    List<SpotPhoto> findBySpotId(int spotId);

    List<TrailPhoto> findByTrailId(int trailId);

    @Transactional
    SpotPhoto addPhoto(SpotPhoto photo);

    @Transactional
    TrailPhoto addPhoto(TrailPhoto photo);

    @Transactional
    boolean updatePhoto(SpotPhoto photo);

    @Transactional
    boolean updatePhoto(TrailPhoto photo);

    @Transactional
    boolean deleteById(int photoId);
}
