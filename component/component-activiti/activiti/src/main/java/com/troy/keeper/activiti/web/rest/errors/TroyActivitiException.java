package com.troy.keeper.activiti.web.rest.errors;

import org.activiti.engine.ActivitiException;

/**
 * discription: 流程异常<br/>
 * Date:     2017/6/6 16:09<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TroyActivitiException extends ActivitiException {

    public TroyActivitiException(String message, Throwable cause) {
        super(message, cause);
    }

    public TroyActivitiException(String message) {
        super(message);
    }
}
