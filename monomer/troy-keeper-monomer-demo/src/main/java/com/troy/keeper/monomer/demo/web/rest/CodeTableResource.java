package com.troy.keeper.monomer.demo.web.rest;

import com.troy.keeper.core.utils.MapperParam;
import com.troy.keeper.core.utils.MapperUtils;
import com.troy.keeper.monomer.api.dto.DataDictionaryDTO;
import com.troy.keeper.monomer.demo.domain.*;
import com.troy.keeper.monomer.demo.repository.DataDictionaryRepository;
import com.troy.keeper.monomer.demo.repository.DateDictionaryChildRepository;
import com.troy.keeper.monomer.demo.service.DataDictionaryService;
import com.troy.keeper.monomer.demo.utils.StringUtil;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revisions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.*;

/**
 * Created by yg on 2017/4/17.
 */
@RestController
public class CodeTableResource extends BaseResource<DataDictionaryDTO> {
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MapperUtils mapperUtils;

    @Autowired
    private DateDictionaryChildRepository dateDictionaryChildRepository;

    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    @RequestMapping(value = "/api/codetable/upload", method = RequestMethod.POST)
    @Transactional
    public ResponseDTO upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
        try {
//            Revisions<Integer, DataDictionary> revisions = testRepository.findRevisions(1l);
//            dataDictionaryRepository.test();
            System.out.println("sss");
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
//        try{
//            System.out.println("开始");
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String fileName = file.getOriginalFilename();
//            path = "G:\\GouWoGames"+"\\"+fileName.substring(0,fileName.lastIndexOf("."));
//            System.out.println(path);
//            File targetFile = null;
//            targetFile = new File(path);
//            if(!targetFile.exists()){
//                targetFile.mkdirs();
//            }
//            //保存
//            try {
//                targetFile = new File(path, fileName);
//                file.transferTo(targetFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path,file.getOriginalFilename())));
////            out.write(file.getBytes());
////            out.flush();
////            out.close();
//            return getResponseDTO("200", "成功", null);
//        }catch (Exception e){
//            e.printStackTrace();
//            return getResponseDTO("201", "系统内部错误", null);
//        }
    }

    @RequestMapping(value = "/api/codetable/save", method = RequestMethod.POST)
    public ResponseDTO save(@RequestBody DataDictionaryDTO dataDictionaryDTO){
        try{
            DataDictionary dataDictionary = new DataDictionary();

            BeanUtils.copyProperties(dataDictionaryDTO,dataDictionary);

            Date date = new Date();
//            dataDictionary.setDatelong(date);
//            dataDictionary.setInstantLongTest(Instant.now());
            dataDictionary.setDicCode("01");
            dataDictionary.setDicValue("test");
            dataDictionary = dataDictionaryService.save(dataDictionary);
            DateDictionaryChild dateDictionaryChild = new DateDictionaryChild();
            dateDictionaryChild.setDataDictionary(dataDictionary);
            dateDictionaryChildRepository.save(dateDictionaryChild);
            return getResponseDTO("200", "成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/updateTest", method = RequestMethod.POST)
   public ResponseDTO updateTest(@RequestBody DataDictionaryDTO dataDictionaryDTO){
       try{
           if (dataDictionaryDTO.getId() == null){
               return getResponseDTO("202", "参数错误", null);
           }
           DataDictionary dataDictionary = dataDictionaryRepository.findOne(dataDictionaryDTO.getId());
           BeanUtils.copyProperties(dataDictionaryDTO,dataDictionary);
           dataDictionaryService.save(dataDictionary);
           return getResponseDTO("200", "成功", null);
       }catch (Exception e){
           e.printStackTrace();
           return getResponseDTO("201", "系统内部错误", null);
       }

   }


    @RequestMapping(value = "/api/codetable/queryTest", method = RequestMethod.POST)
    public ResponseDTO queryTest(){
        try{
            List<String> emial = new ArrayList<>();
            emial.add("test1");
            emial.add("test2");
//            List<DataDictionary> dataDictionaryList = (List<DataDictionary>) dataDictionaryRepository.queryTest(emial);
            return getResponseDTO("200", "成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/add", method = RequestMethod.POST)
    public ResponseDTO<String> add(String dicCode, String dicValue, String orderCode, String memo){
        try {
            if(dicCode == null || "".equals(dicCode)){
                return getResponseDTO("202", "请求参数dicCode错误", null);
            }
            else{
                if(StringUtil.stringNumbers(dicCode, ".", 0) != 3){
                    return getResponseDTO("202", "请求参数dicCode错误", null);
                }
            }
            if(dicValue == null || "".equals(dicValue)){
                return getResponseDTO("202", "请求参数dicValue错误", null);
            }
            Integer orderCodeInt = null;
            if(orderCode == null || "".equals(orderCode)){
                return getResponseDTO("202", "请求参数orderCode错误", null);
            }
            else{
                try {
                    orderCodeInt = Integer.parseInt(orderCode);
                }catch (Exception e){
                    e.printStackTrace();
                    return getResponseDTO("202", "请求参数orderCode错误", null);
                }
            }
            if(memo == null || "".equals(memo)){
                return getResponseDTO("202", "请求参数memo错误", null);
            }
            dataDictionaryService.addDataDictionary(dicCode, dicValue, orderCodeInt, memo);
            //刷新redis
            refresh(dicCode);
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/edit", method = RequestMethod.POST)
    public ResponseDTO<String> edit(String dicCode, String dicValue, String orderCode, String memo){
        try {
            if(dicCode == null || "".equals(dicCode)){
                return getResponseDTO("202", "请求参数dicCode错误", null);
            }
            else{
                if(StringUtil.stringNumbers(dicCode, ".", 0) != 3){
                    return getResponseDTO("202", "请求参数dicCode错误", null);
                }
            }
            if(dicValue == null || "".equals(dicValue)){
                dicValue = null;
            }
            Integer orderCodeInt = null;
            if(orderCode == null || "".equals(orderCode)){
                orderCodeInt = null;
            }
            else{
                try {
                    orderCodeInt = Integer.parseInt(orderCode);
                }catch (Exception e){
                    e.printStackTrace();
                    return getResponseDTO("202", "请求参数orderCode错误", null);
                }
            }
            if(memo == null || "".equals(memo)){
                memo = null;
            }
            if(dicValue == null && orderCodeInt == null && memo == null){
                return getResponseDTO("202", "请求参数dicValue、orderCode、memo错误", null);
            }
            dataDictionaryService.editDataDictionary(dicCode, dicValue, orderCodeInt, memo);
            //刷新redis
            refresh(dicCode);
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/delete", method = RequestMethod.POST)
    public ResponseDTO<String> delete(String dicCode){
        try {
            if(dicCode == null || "".equals(dicCode)){
                return getResponseDTO("202", "请求参数dicCode错误", null);
            }
            else{
                if(StringUtil.stringNumbers(dicCode, ".", 0) != 3){
                    return getResponseDTO("202", "请求参数dicCode错误", null);
                }
            }
            dataDictionaryService.deleteDataDictionary(dicCode);
            //刷新redis
            refresh(dicCode);
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }
    @RequestMapping(value = "/api/get", method = RequestMethod.GET)
    public String get2(String key){

        return "hello2";
    }

    @RequestMapping(value = "/api/codetable/get", method = RequestMethod.POST)
    public ResponseDTO<String> get(String key){
        try {
            if(key == null || "".equals(key)){
                return getResponseDTO("202", "请求参数key错误", null);
            }
            String value = redisService.get(key);
            return getResponseDTO("200", "操作成功", value);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/getList", method = RequestMethod.POST)
    public ResponseDTO<List<String>> getList(String key){
        try {
            if(key == null || "".equals(key)){
                return getResponseDTO("202", "请求参数key错误", null);
            }
            List<String> list = redisService.getList(key);
            return getResponseDTO("200", "操作成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/getLists", method = RequestMethod.POST)
    public ResponseDTO<Map<String, List<String>>> getLists(String[] key){
        try {
            if(key == null || key.length == 0){
                return getResponseDTO("202", "请求参数key错误", null);
            }
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            for (int i = 0; i < key.length; i++) {
                List<String> list = redisService.getList(key[i]);
                map.put(key[i], list);
            }
            return getResponseDTO("200", "操作成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/getHashMap", method = RequestMethod.POST)
    public ResponseDTO<Map<String, String>> getHashMap(String key){
        try {
            if(key == null || "".equals(key)){
                return getResponseDTO("202", "请求参数key错误", null);
            }
            Map<String, String> map = redisService.hMGet(key);
            return getResponseDTO("200", "操作成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/getHashMaps", method = RequestMethod.POST)
    public ResponseDTO<Map<String, Map<String, String>>> getHashMaps(String [] key){
        try {
            if(key == null || key.length == 0){
                return getResponseDTO("202", "请求参数key错误", null);
            }
            Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
            for (int i = 0; i < key.length; i++) {
                Map<String, String> map = redisService.hMGet(key[i]);
                resultMap.put(key[i], map);
            }
            return getResponseDTO("200", "操作成功", resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/refresh", method = RequestMethod.POST)
    public ResponseDTO<String> refresh(String dicCode){
        try {
            if(dicCode != null){
                if(StringUtil.stringNumbers(dicCode, ".", 0) != 3){
                    return getResponseDTO("202", "请求参数dicCode错误", null);
                }
                //清除
                redisService.removeByKey(dicCode);
                String listKey = dicCode.substring(0, dicCode.lastIndexOf("."));
                redisService.removeByKey(listKey);

                //查询新值
                DataDictionaryDTO dto = dataDictionaryService.queryDataDictionaryByDicCode(dicCode);
                if(dto != null){
                    String dicValue = dto.getDicValue();
                    redisService.set(dicCode, dicValue);
                }
                List<DataDictionaryDTO> dtoList = dataDictionaryService.queryDataDictionariesByKey(listKey);
                if(dtoList != null && !dtoList.isEmpty()){
                    Map<String, String> map = new LinkedHashMap<String, String>();
                    for (int i = 0; i < dtoList.size(); i++) {
                        DataDictionaryDTO dataDictionaryDTO = dtoList.get(i);
                        String code = dataDictionaryDTO.getDicCode();
                        String mapKey = code.substring(code.lastIndexOf(".") + 1);
                        String dicValue = dataDictionaryDTO.getDicValue();
                        map.put(mapKey, dicValue);
                    }
                    redisService.hMSet(listKey, map);
                }
            }
            else{
                List<DataDictionaryDTO> list = dataDictionaryService.queryDataDictionaries();
                if(list != null && !list.isEmpty()){
                    Iterator<DataDictionaryDTO> it = list.iterator();
                    Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
                    List<String> keyList = new ArrayList<String>();
                    for (;it.hasNext();){
                        DataDictionaryDTO dataDictionaryDTO = it.next();
                        String code = dataDictionaryDTO.getDicCode();
                        String listKey = code.substring(0, code.lastIndexOf("."));
                        String mapKey = code.substring(code.lastIndexOf(".") + 1);
                        String value = dataDictionaryDTO.getDicValue();
                        redisService.removeByKey(code);
                        redisService.set(code, value);
                        if(!map.containsKey(listKey)){
                            redisService.removeByKey(listKey);
                            Map<String, String> listMap = new LinkedHashMap<String, String>();
                            listMap.put(mapKey, value);
                            map.put(listKey, listMap);
                        }
                        else{
                            Map<String, String> listMap = map.get(listKey);
                            listMap.put(mapKey, value);
                        }
                    }
                    if(map != null && map.size() > 0){
                        Iterator<String> iterator = map.keySet().iterator();
                        for (;iterator.hasNext();){
                            String listKey = iterator.next();
                            Map<String, String> listMap = map.get(listKey);
                            redisService.hMSet(listKey, listMap);
                        }
                    }
                }
            }
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/maptest", method = RequestMethod.POST)
    public ResponseDTO<Map<String, Object>> maptest(){
        try {
            Test test = new Test();
//            test.setId(1);
//            test.setName("zhangsan");
//            test.setUserId(1);
//            test.setOperId(2);
//            test.setCode1("0");
//            test.setCode2("1");
//            test.setIgnore1("ignore1");
//            test.setIgnore2("ignore2");

            List<String> ignoreColumnList = new ArrayList<String>();
            ignoreColumnList.add("ignore1");
            ignoreColumnList.add("ignore2");

            List<String> keyList = new ArrayList<String>();
            keyList.add("tenant.tenant.state");
            keyList.add("uaa.user.user_type");

            List<String> sourceColumnList = new ArrayList<String>();
            sourceColumnList.add("code1");
            sourceColumnList.add("code2");

            List<String> userIdColumnList = new ArrayList<String>();
            userIdColumnList.add("userId");
            userIdColumnList.add("operId");

            MapperParam mapperParam = new MapperParam();
            mapperParam.setObj(test);
            mapperParam.setIgnoreColumnList(ignoreColumnList);
            mapperParam.setKeyList(keyList);
            mapperParam.setSourceColumnList(sourceColumnList);
            mapperParam.setUserIdColumnList(userIdColumnList);

            Map<String, Object> map = mapperUtils.convert(mapperParam);
            return getResponseDTO("200", "操作成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    @RequestMapping(value = "/api/codetable/listtest", method = RequestMethod.POST)
    public ResponseDTO<List<Map<String, Object>>> listtest(){
//        try {
//            Test test = new Test();
//            test.setId(1);
//            test.setName("zhangsan");
//            test.setUserId(1);
//            test.setOperId(2);
//            test.setCode1("0");
//            test.setCode2("1");
//            test.setIgnore1("ignore1");
//            test.setIgnore2("ignore2");
//
//            Test test2 = new Test();
//            test2.setId(2);
//            test2.setName("zhangsan2");
//            test2.setUserId(1);
//            test2.setOperId(2);
//            test2.setCode1("0");
//            test2.setCode2("1");
//            test2.setIgnore1("ignore1");
//            test2.setIgnore2("ignore2");
//
//            Test test3 = new Test();
//            test3.setId(3);
//            test3.setName("zhangsan3");
//            test3.setUserId(1);
//            test3.setOperId(2);
//            test3.setCode1("0");
//            test3.setCode2("1");
//            test3.setIgnore1("ignore1");
//            test3.setIgnore2("ignore2");
//
//            List<Object> list = new ArrayList<Object>();
//            list.add(test);
//            list.add(test2);
//            list.add(test3);
//
//            List<String> ignoreColumnList = new ArrayList<String>();
//            ignoreColumnList.add("ignore1");
//            ignoreColumnList.add("ignore2");
//
//            List<String> keyList = new ArrayList<String>();
//            keyList.add("tenant.tenant.state");
//            keyList.add("uaa.user.user_type");
//
//            List<String> sourceColumnList = new ArrayList<String>();
//            sourceColumnList.add("code1");
//            sourceColumnList.add("code2");
//
//            List<String> userIdColumnList = new ArrayList<String>();
//            userIdColumnList.add("userId");
//            userIdColumnList.add("operId");
//
//            MapperParam mapperParam = new MapperParam();
//            mapperParam.setList(list);
//            mapperParam.setIgnoreColumnList(ignoreColumnList);
//            mapperParam.setKeyList(keyList);
//            mapperParam.setSourceColumnList(sourceColumnList);
//            mapperParam.setUserIdColumnList(userIdColumnList);
//
//            List<Map<String, Object>> result = mapperUtils.convertList(mapperParam);
//            return getResponseDTO("200", "操作成功", result);
//        }catch (Exception e){
//            e.printStackTrace();
//            return getResponseDTO("201", "系统内部错误", null);
//        }
        return null;
    }



}
