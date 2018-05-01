package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.DataDictionary;
import com.troy.keeper.system.dto.DataDictionaryDTO;
import com.troy.keeper.system.service.DataDictionaryService;
import com.troy.keeper.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by yg on 2017/4/17.
 */
@RestController
public class CodeTableResource extends BaseResource<BaseDTO> {
    public static final Logger log = Logger.getLogger(CodeTableResource.class);
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private RedisService redisService;

    /**
     * 前端接口：获取列表
     *
     * @param dataDictionaryDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/system/codeTable/getList", method = RequestMethod.POST)
    public ResponseDTO<List<Map<String, Object>>> getList(@RequestBody DataDictionaryDTO dataDictionaryDTO) throws Exception {
        return success(dataDictionaryService.getList(dataDictionaryDTO));
    }

    /**
     * 前端接口：获取列表（分页）
     *
     * @param dataDictionaryDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/system/codeTable/getPage", method = RequestMethod.POST)
    public ResponseDTO<Page<Map<String, Object>>> getPage(@RequestBody DataDictionaryDTO dataDictionaryDTO) throws Exception {
        if (dataDictionaryDTO.getPage() == null) {
            return fail(Const.MSG_COMMON_PAGE_ERROR);
        }
        if (dataDictionaryDTO.getSize() == null || dataDictionaryDTO.getSize() == 0) {
            dataDictionaryDTO.setSize(Const.DEFAULT_PAGE_SIZE_NUM);
        }
        return success(dataDictionaryService.getPage(dataDictionaryDTO));
    }

    /**
     * 前端接口：新增码表
     *
     * @param dataDictionaryDTO
     * @return
     */
    @RequestMapping(value = "/api/system/codeTable/add", method = RequestMethod.POST)
    public ResponseDTO<String> add(@RequestBody DataDictionaryDTO dataDictionaryDTO) {
        String dicCode = dataDictionaryDTO.getDicCode();
        String dicValue = dataDictionaryDTO.getDicValue();
        Integer orderCode = dataDictionaryDTO.getOrderCode();
        String memo = dataDictionaryDTO.getMemo();
        if (dicCode == null || "".equals(dicCode)) {
            return fail(Const.MSG_CODE_400_CODETABLE_DICCODE);
        } else {
            if (StringUtils.stringNumbers(dicCode, ".", 0) != 3) {
                return fail(Const.MSG_CODE_400_CODETABLE_DICCODE);
            }
        }
        if (dicValue == null || "".equals(dicValue)) {
            return fail(Const.MSG_CODE_400_CODETABLE_DICVALUE);
        }
        if (orderCode == null) {
            return fail(Const.MSG_CODE_400_CODETABLE_ORDERCODE);
        }
        if (memo == null || "".equals(memo)) {
            return fail(Const.MSG_CODE_400_CODETABLE_MEMO);
        }
        if (!dataDictionaryService.checkReport(dataDictionaryDTO)) {
            return fail(Const.MSG_CODE_400_CODETABLE_REPORT_ERROR);
        }
        dataDictionaryService.addDataDictionary(dicCode, dicValue, orderCode, memo);
        //刷新redis
        refresh(dataDictionaryDTO);
        return success();
    }

    @RequestMapping(value = "/api/system/codeTable/edit", method = RequestMethod.POST)
    public ResponseDTO<String> edit(@RequestBody DataDictionaryDTO dataDictionaryDTO) {
        String dicCode = dataDictionaryDTO.getDicCode();
        String dicValue = dataDictionaryDTO.getDicValue();
        Integer orderCode = dataDictionaryDTO.getOrderCode();
        String memo = dataDictionaryDTO.getMemo();
        if (dicCode == null || "".equals(dicCode)) {
            return fail(Const.MSG_CODE_400_CODETABLE_DICCODE);
        } else {
            if (StringUtils.stringNumbers(dicCode, ".", 0) != 3) {
                return fail(Const.MSG_CODE_400_CODETABLE_DICCODE);
            }
        }
        if (dicValue == null || "".equals(dicValue)) {
            dicValue = null;
        }
        if (memo == null || "".equals(memo)) {
            memo = null;
        }
        if (dicValue == null && orderCode == null && memo == null) {
            return fail(Const.MSG_CODE_400_CODETABLE_DICCODE_OR_ORDERCODE_OR_MEMO);
        }
        dataDictionaryService.editDataDictionary(dicCode, dicValue, orderCode, memo);
        //刷新redis
        refresh(dataDictionaryDTO);
        return success();
    }

    /**
     * 前端接口：删除接口
     *
     * @param dataDictionaryDTO
     * @return
     */
    @RequestMapping(value = "/api/system/codeTable/delete", method = RequestMethod.POST)
    public ResponseDTO<String> delete(@RequestBody DataDictionaryDTO dataDictionaryDTO) {
        String dicCode = dataDictionaryDTO.getDicCode();
        if (dicCode == null || "".equals(dicCode)) {
            return fail(Const.MSG_CODE_400_CODETABLE_DICCODE);
        } else {
            if (StringUtils.stringNumbers(dicCode, ".", 0) != 3) {
                return fail(Const.MSG_CODE_400_CODETABLE_DICCODE);
            }
        }
        dataDictionaryService.deleteDataDictionary(dicCode);
        //刷新redis
        refresh(dataDictionaryDTO);
        return success();
    }

    /**
     * 前端接口：刷新接口
     *
     * @param dataDictionaryDTO
     * @return
     */
    @RequestMapping(value = "/api/system/codeTable/refresh", method = RequestMethod.POST)
    public ResponseDTO<String> refresh(@RequestBody DataDictionaryDTO dataDictionaryDTO) {
        String dicCode = dataDictionaryDTO.getDicCode();
        if (dicCode != null) {
            return refreshWithDicCode(dicCode);
        } else {
            return refreshWithoutDicCode();
        }
    }

    private ResponseDTO<String> refreshWithoutDicCode() {
        List<DataDictionary> list = dataDictionaryService.queryDataDictionaries();
        if (list != null && !list.isEmpty()) {
            Iterator<DataDictionary> it = list.iterator();
            Map<String, Map<String, String>> map = new HashMap<>();
            while (it.hasNext()) {
                DataDictionary dataDictionary = it.next();
                String code = dataDictionary.getDicCode();
                String listKey = code.substring(0, code.lastIndexOf('.'));
                String mapKey = code.substring(code.lastIndexOf('.') + 1);
                String value = dataDictionary.getDicValue();
                redisService.removeByKey(code);
                redisService.set(code, value);
                if (!map.containsKey(listKey)) {
                    redisService.removeByKey(listKey);
                    Map<String, String> listMap = new LinkedHashMap<>();
                    listMap.put(mapKey, value);
                    map.put(listKey, listMap);
                } else {
                    Map<String, String> listMap = map.get(listKey);
                    listMap.put(mapKey, value);
                }
            }
            if (map.size() > 0) {
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String listKey = iterator.next();
                    Map<String, String> listMap = map.get(listKey);
                    redisService.hMSet(listKey, listMap);
                }
            }
        }
        return success();
    }

    private ResponseDTO<String> refreshWithDicCode(String dicCode) {
        if (StringUtils.stringNumbers(dicCode, ".", 0) != 3) {
            return fail(Const.MSG_CODE_400_CODETABLE_DICCODE);
        }
        //清除
        redisService.removeByKey(dicCode);
        String listKey = dicCode.substring(0, dicCode.lastIndexOf('.'));
        redisService.removeByKey(listKey);

        //查询新值
        DataDictionary dd = dataDictionaryService.queryDataDictionaryByDicCode(dicCode);
        if (dd != null) {
            String dicValue = dd.getDicValue();
            redisService.set(dicCode, dicValue);
        }
        List<DataDictionary> ddList = dataDictionaryService.queryDataDictionariesByKey(listKey);
        if (ddList != null && !ddList.isEmpty()) {
            Map<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < ddList.size(); i++) {
                DataDictionary dataDictionary = ddList.get(i);
                String code = dataDictionary.getDicCode();
                String mapKey = code.substring(code.lastIndexOf('.') + 1);
                String dicValue = dataDictionary.getDicValue();
                map.put(mapKey, dicValue);
            }
            redisService.hMSet(listKey, map);
        }
        return success();
    }
}
