package hiking.controller;

import hiking.models.AppUserRole;
import hiking.service.AppUserRoleService;
import hiking.service.Result;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/authorities")
public class AppUserRoleController {

    private final AppUserRoleService service;

    public AppUserRoleController(AppUserRoleService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppUserRole> getUsers(){
        return service.findAll();
    }

    @PutMapping
    public ResponseEntity<Object> update(@Valid @RequestBody AppUserRole user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> messages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }

        Result<AppUserRole> result = service.update(user);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }
}
