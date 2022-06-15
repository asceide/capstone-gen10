package hiking.controller;

import hiking.models.Spot;
import hiking.models.Trail;
import hiking.service.Result;
import hiking.service.SpotService;
import hiking.service.TrailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/trail")
public class TrailController {

    private final TrailService service;

    public TrailController(TrailService service) {this.service = service;}

    @GetMapping
    public List<Trail> getTrails() {return service.findAll();}

    @GetMapping("/{trailId}")
    public ResponseEntity<Trail> getSpot(@PathVariable int trailId) {
        Trail trail = service.findById(trailId);
        if (trail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trail);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Trail trail) {
        Result<Trail> result = service.add(trail);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{trailId}")
    public ResponseEntity<Object> update(@PathVariable int trailId, @RequestBody Trail trail) {
        if (trailId != trail.getTrailId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Trail> result = service.update(trail);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{trailId}")
    public ResponseEntity<Void> deleteById(@PathVariable int trailId) {
        if (service.deleteById(trailId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
