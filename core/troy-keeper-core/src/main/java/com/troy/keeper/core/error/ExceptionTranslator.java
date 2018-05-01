package com.troy.keeper.core.error;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.enums.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

    private final Logger LOGGER = LoggerFactory.getLogger(ExceptionTranslator.class);

    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseDTO processConcurrencyError(ConcurrencyFailureException ex) {
        ex.printStackTrace();
        return new ResponseDTO(ResponseCode.CODE_409.getCode(),ResponseCode.CODE_409.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO processValidationError(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        return requestFail();
    }

    @ExceptionHandler(KeeperException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO processKeeperException(KeeperException ex) {
        ex.printStackTrace();
        return requestFail(ex);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseDTO processAccessDeniedException(AccessDeniedException e) {
        e.printStackTrace();
        return new ResponseDTO(ResponseCode.CODE_403.getCode(),ResponseCode.CODE_403.getMsg());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseDTO processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        exception.printStackTrace();
        return new ResponseDTO(ResponseCode.CODE_405.getCode(),ResponseCode.CODE_405.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> processRuntimeException(Exception ex) {
        BodyBuilder builder;
        ResponseDTO ResponseDTO;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            ResponseDTO = new ResponseDTO("" + responseStatus.value().value(), responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ResponseDTO = fail();
        }
        ex.printStackTrace();
        return builder.body(ResponseDTO);
    }

    private <K> ResponseDTO<K> fail(){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(ResponseCode.CODE_500.getCode(),ResponseCode.CODE_500.getMsg());
        return responseDTO;
    }

    private <K> ResponseDTO<K> requestFail(){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(ResponseCode.CODE_400.getCode(),ResponseCode.CODE_400.getMsg());
        return responseDTO;
    }

    private <K> ResponseDTO<K>  requestFail(KeeperException e) {
        String code = null;
        String msg = null;
        try {
            ResponseCode responseCode = ResponseCode.valueOf(e.getCode() + "");
            code = responseCode.getCode();
            msg = responseCode.getMsg() + "," + e.getMessage();
        } catch (Exception ex) {
            code = ""+e.getCode();
            msg = e.getMessage();
        }
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(code, msg);
        return responseDTO;
    }



}
