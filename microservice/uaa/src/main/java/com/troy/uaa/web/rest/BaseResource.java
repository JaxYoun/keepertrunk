package com.troy.uaa.web.rest;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;

import java.util.Collection;

/**
 * Created by yjm on 2017/4/12.
 */
public class BaseResource <T extends BaseDTO>{
    public BaseResource() {
    }

    public <K> ResponseDTO<K> getResponseDTO(String code, String msg, K obj) {
        if(null != obj && obj instanceof Collection) {
            ;
        }

        ResponseDTO responseDTO = new ResponseDTO(code, msg);
        responseDTO.setData(obj);
        return responseDTO;
    }
}
