package hiking.controller;

import hiking.service.Result;
import hiking.service.ResultType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {

    public static <T> ResponseEntity<Object> build(Result<T> result) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(result.getType() == null || result.getType() == ResultType.INVALID){
            status = HttpStatus.BAD_REQUEST;
        } else if (result.getType() == ResultType.NOT_FOUND){
            status = HttpStatus.NOT_FOUND;
        } else if (result.getType() == ResultType.CONFLICT){
            status = HttpStatus.CONFLICT;
        }

        return new ResponseEntity<>(result.getMessages(), status);
    }
}
