package hiking.service;

import hiking.repository.SpotRepository;
import org.springframework.stereotype.Service;

@Service
public class SpotService {

    private final SpotRepository repository;

    public SpotService(SpotRepository repository) {this.repository = repository;}

    
}
