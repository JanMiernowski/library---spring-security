package pl.jmiernowski;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.UUID;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(value = IncorrectResultSizeDataAccessException.class)
    public String handleAnyRuntimeException(IncorrectResultSizeDataAccessException ex){
        String errorCode = UUID.randomUUID().toString();
        System.out.println("Error code " + errorCode);
        ex.printStackTrace();

        return "redirect:/register";
    }



    @Getter
    @AllArgsConstructor
    static class Error{
        private final String message;
        private final LocalDateTime errorTime;
        private final String errorCode;
    }
}
