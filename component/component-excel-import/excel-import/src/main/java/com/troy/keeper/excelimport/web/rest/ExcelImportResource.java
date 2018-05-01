package com.troy.keeper.excelimport.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.excelimport.service.ExcelImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by SimonChu on 2017/6/29.
 */
@RestController
public class ExcelImportResource {

    @Autowired
    private ExcelImportService excelImportService;

    private final Logger log = LoggerFactory.getLogger(ExcelImportResource.class);

    @RequestMapping(value = "/api/excel/import/test", method = RequestMethod.POST)
    public ResponseDTO<Object> test(@RequestBody File o) throws Exception {
        ResponseDTO<Object> msg = new ResponseDTO<>();
        File file = new File("D:/abc.xls");
        try {
            InputStream excelFile = new FileInputStream(file);
             msg = excelImportService.importExcelNotParamterBy2003(excelFile, excelConfigParamter -> {
                 excelConfigParamter.setExcelHandleList(excelConfigParamter.getExcelList());
                 return excelConfigParamter;
             });
            return msg;
        } catch (FileNotFoundException e) {
            log.error("test接口执行失败",e);
            return msg;
        }
    }
}
