package hiking.controller;

import hiking.models.Spot;
import hiking.service.Result;
import hiking.service.ResultType;
import hiking.service.SpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spot")
public class SpotController {

    private final SpotService service;

    public SpotController(SpotService service) {this.service = service;}

    @GetMapping
    public List<Spot> getSpots(@RequestParam(value = "trail-id", required = false, defaultValue = "0") int trailId) {
        if (trailId != 0) {
            return service.findByTrail(trailId);
        }

        return service.findAll();
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<Spot> getSpot(@PathVariable int spotId) {
        Spot spot = service.findById(spotId);
        if (spot == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spot);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody Spot spot, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> messages = bindingResult.getAllErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }

        Result<Spot> result = service.add(spot);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{spotId}")
    public ResponseEntity<Object> update(@PathVariable int spotId,
                                         @Valid @RequestBody Spot spot, BindingResult bindingResult) {
        if (spot == null) {
            return new ResponseEntity<>(List.of("Spot cannot be null"), HttpStatus.BAD_REQUEST);
        }
        if (spot.getSpotId() != spotId) {
            return new ResponseEntity<>(List.of("Spot id must match id in path"), HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasErrors()) {
            List<String> messages = bindingResult.getAllErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }

        Result<Spot> result = service.update(spot);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{spotId}/rate")
    public ResponseEntity<Object> rate(@PathVariable int spotId, @RequestParam int rating) {
        if (spotId <= 0) {
            return new ResponseEntity<>(List.of("Spot id must be greater than 0"), HttpStatus.BAD_REQUEST);
        }

        if(rating <= 0 || rating > 5) {
            return new ResponseEntity<>(List.of("rating must be between 1-5"), HttpStatus.BAD_REQUEST);
        }

        Result<Spot> result = service.rate(spotId, rating);

        if(result.getType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
        }

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }

        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{spotId}")
    public ResponseEntity<Object> delete(@PathVariable int spotId) {
        Result<Spot> result = service.deleteById(spotId);

        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
    }
}
