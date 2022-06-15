package hiking.controller;

import com.fasterxml.jackson.databind.JsonSerializer;
import hiking.models.AppUserInfo;
import hiking.service.AppUserInfoService;
import hiking.service.Result;
import org.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class AppUserInfoController {

    private final AppUserInfoService service;

    public AppUserInfoController(AppUserInfoService service) {
        this.service = service;
    }

    @GetMapping("/{appUserId}")
    public ResponseEntity<Object> findByAppUserId(@PathVariable int appUserId){
        AppUserInfo info = service.findByAppUserId(appUserId);

        if(info==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, String> map = new HashMap<>();
        map.put("firstName", info.getFirstName());
        map.put("lastName", info.getLastName());
        map.put("city", info.getCity());
        map.put("state", info.getState());

        return ResponseEntity.ok(map);
    }

    @GetMapping("/")
    public ResponseEntity<Object> loadByUserName(@RequestBody Map<String, String> user){
        String username = user.get("username");
        AppUserInfo userInfo = service.findByUsername(username);

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/")
    public ResponseEntity<Object> add(@Valid @RequestBody AppUserInfo info, BindingResult bindingResult) {


        if(bindingResult.hasErrors()){
            List<String> messages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }

        Result<AppUserInfo> result = service.add(info);


        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    // WEB API에서는 PUT을 사용하지 않는다.

    // Two ways to approach this, with or without a path variable.
    // But considering that the two objects are obstensively linked, I don't think there is a need
    // To specify the id as a path variable, and will instead already be ingrained in the body.
    @PutMapping("/")
    public ResponseEntity<Object> update(@Valid @RequestBody AppUserInfo info, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            List<String> messages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }

        Result<AppUserInfo> result = service.update(info);

        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{appUserId}")
    public ResponseEntity<Object> delete(@PathVariable int appUserId){
        Result<AppUserInfo> result = service.delete(appUserId);

        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
