package com.troy.keeper.system;

import com.troy.keeper.system.dto.DataDictionaryDTO;
import com.troy.keeper.system.util.PostUtils;
import com.troy.keeper.system.web.rest.CodeTableResource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by yg on 2017/4/18.
 */
@Component
public class MyStartupRunner implements CommandLineRunner {
    public static final Logger log = Logger.getLogger(MyStartupRunner.class);
    @Autowired
    private CodeTableResource codeTableResource;

    @Autowired
    private PostUtils postUtils;

    @Override
    public void run(String... strings) throws Exception {
        DataDictionaryDTO dataDictionaryDTO = new DataDictionaryDTO();
        log.info(">>>>>>>>>>>>>>>load codetable to redis start<<<<<<<<<<<<<");
        codeTableResource.refresh(dataDictionaryDTO);
        log.info(">>>>>>>>>>>>>>>load codetable to redis end<<<<<<<<<<<<<");

        log.info(">>>>>>>>>>>>>>>load orgLimit star<<<<<<<<<<<<<");
        postUtils.LoadOrgLimit();
        log.info(">>>>>>>>>>>>>>>load orgLimit end<<<<<<<<<<<<<");
    }
}
