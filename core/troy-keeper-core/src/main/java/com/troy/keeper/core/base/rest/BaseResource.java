package com.troy.keeper.core.base.rest;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.enums.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-3-29
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseResource<T extends BaseDTO> {

    protected final Logger LOGGER= LoggerFactory.getLogger(getClass());

    public <K> ResponseDTO<K> getResponseDTO(String code, String msg, K obj){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(code, msg);
        responseDTO.setData(obj);
        return responseDTO;
    }
    public <K> ResponseDTO<K> success(){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(ResponseCode.CODE_200.getCode(), ResponseCode.CODE_200.getMsg());
        return responseDTO;
    }
    public <K> ResponseDTO<K> success(K obj){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(ResponseCode.CODE_200.getCode(), ResponseCode.CODE_200.getMsg());
        responseDTO.setData(obj);
        return responseDTO;
    }
    public <K> ResponseDTO<K> success(K obj,String msg){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(ResponseCode.CODE_200.getCode(), msg);
        responseDTO.setData(obj);
        return responseDTO;
    }
    public <K> ResponseDTO<K> fail(){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(ResponseCode.CODE_400.getCode(), ResponseCode.CODE_400.getMsg());
        return responseDTO;
    }
    public <K> ResponseDTO<K> fail(String msg){
        ResponseDTO<K> responseDTO = new ResponseDTO<K>(ResponseCode.CODE_400.getCode(),msg);
        return responseDTO;
    }

}
