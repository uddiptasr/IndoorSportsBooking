package com.booking.app.exception;

import com.booking.app.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;


@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ResponseDto> resourceNotFoundException(Exception resourceNotFoundException, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Validation Failed", resourceNotFoundException.getMessage());

        return new ResponseEntity<>(new ResponseDto(null, exceptionResponse), HttpStatus.OK);
    }
    @Override//request.getDescription(false)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        //return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ResponseDto1(null,error ), HttpStatus.BAD_REQUEST);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<ResponseDto> showCustomMessage(Exception e){

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Failed Validation", "Your input is invalid");

        return new ResponseEntity<>(new ResponseDto(null,exceptionResponse ), HttpStatus.BAD_REQUEST);
    }
   /* @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<ResponseDto> showCustomMessage(Exception e){

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Failed Validation of Request Body", "Your input is invalid");

        return new ResponseEntity<>(new ResponseDto(null,exceptionResponse ), HttpStatus.BAD_REQUEST);
    }
@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Failed Validation", errorMessage);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


   }
@Override
   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       List<String> details = new ArrayList<>();
       for(ObjectError error : ex.getBindingResult().getAllErrors()) {
           details.add(error.getDefaultMessage());
       }
       ExceptionResponse error = new ExceptionResponse(new Date(),"Failed Validation", details.toString());
       return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }

*/
}
