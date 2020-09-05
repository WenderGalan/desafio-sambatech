package wendergalan.github.io.desafiosambatech.config.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import wendergalan.github.io.desafiosambatech.config.model.beans.ResponseError;
import wendergalan.github.io.desafiosambatech.service.MessageByLocaleService;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionConfiguration {

    private final MessageByLocaleService message;

    /**
     * @param ex         the ex
     * @param webRequest the web request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleErrorGeneric(Exception ex, WebRequest webRequest) {
        // Do Anything with webRequest
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ResponseError(message.getMessage("api.generic.error")));
    }
}
