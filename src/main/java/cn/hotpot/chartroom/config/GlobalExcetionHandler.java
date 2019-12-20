package cn.hotpot.chartroom.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
@ControllerAdvice
@Slf4j
public class GlobalExcetionHandler {

    @ExceptionHandler(value =Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e){
        log.info(e.getMessage());

        if (e instanceof RuntimeException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
    }
}
