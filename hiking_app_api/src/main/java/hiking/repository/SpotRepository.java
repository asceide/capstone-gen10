package hiking.repository;

import hiking.models.Spot;

import java.util.List;

public interface SpotRepository {

    List<Spot> findAll();
    List<Spot> findByTrail(int trailId);
    Spot findById(int spotId);
    Spot add(Spot spot);
    boolean update(Spot spot);
    boolean deleteById(int spotId);
}
