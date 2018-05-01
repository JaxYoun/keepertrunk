package com.troy.keeper.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-17
 * Time: 下午3:03
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ClientHystrixImpl implements ClientHystrix {

    private final Logger LOGGER = LoggerFactory.getLogger(ClientHystrixImpl.class);

    @Override
    public String defaultCommend() {
        LOGGER.error("failed to invoke.");
        return "failed";
    }
}
