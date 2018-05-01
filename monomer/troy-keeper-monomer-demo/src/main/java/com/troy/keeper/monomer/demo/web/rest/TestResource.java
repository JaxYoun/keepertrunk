package com.troy.keeper.monomer.demo.web.rest;


import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.core.base.service.MailService;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.monomer.demo.domain.Person;
import com.troy.keeper.monomer.demo.domain.Student;
import com.troy.keeper.monomer.demo.service.StudentService;
import com.troy.keeper.monomer.demo.service.TestService;
import com.troy.keeper.monomer.demo.utils.ExcelOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by yg on 2017/6/8.
 */
@RestController
public class TestResource extends BaseResource<BaseDTO> {
    @Autowired
    private StudentService studentService;
    @Autowired
    private TestService commonService;

    @Autowired
    private MailService mailService;

    @RequestMapping(value = "/api/test/find", method = RequestMethod.POST)
    public ResponseDTO find(long id){
        try{
            Student student = studentService.find(id);
            return getResponseDTO("200", "操作成功", student);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getResponseDTO("200", "操作成功", null);
    }

    @RequestMapping(value = "/api/test/delete", method = RequestMethod.POST)
    public ResponseDTO delete(){
        try{
            studentService.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
        return getResponseDTO("200", "操作成功", null);
    }

    @RequestMapping(value = "/api/test/update", method = RequestMethod.POST)
    public ResponseDTO update(long id){
        try{
            studentService.update(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getResponseDTO("200", "操作成功", null);
    }

    @RequestMapping(value = "/api/test/findAll", method = RequestMethod.POST)
    public ResponseDTO findAll(){
        List<Student> list = studentService.findAll();
        return getResponseDTO("200", "操作成功", list);
    }

    @RequestMapping(value = "/api/test/testJson", method = RequestMethod.POST)
    public ResponseDTO testJson(){
        String json = "{\"name\":\"zhangsan\"}";;
        try{
            commonService.save(json, "com.troy.keeper.monomer.demo.domain.Person");
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }

    @RequestMapping(value = "/api/test/findOneByHql", method = RequestMethod.POST)
    public ResponseDTO findOneByHql(){
        Person person = studentService.findOneByHql();
        return getResponseDTO("200", "操作成功", person);
    }

    @RequestMapping(value = "/api/test/findListByHql", method = RequestMethod.POST)
    public ResponseDTO findListByHql(){
        List<Person> list = studentService.findListByHql();
        return getResponseDTO("200", "操作成功", list);
    }

    @RequestMapping(value = "/api/test/findListBySql", method = RequestMethod.POST)
    public ResponseDTO findListBySql(){
        List<Person> list = studentService.findListBySql();
        return getResponseDTO("200", "操作成功", list);
    }

    @RequestMapping(value = "/api/test/findOneBySql", method = RequestMethod.POST)
    public ResponseDTO findOneBySql(){
        Person person = (Person)studentService.findOneBySql();
        return getResponseDTO("200", "操作成功", person);
    }

    @RequestMapping(value = "/api/test/findAllBySpec", method = RequestMethod.POST)
    public ResponseDTO findAllBySpec(){
        List<Person> list = studentService.findAllBySpec();
        return getResponseDTO("200", "操作成功", list);
    }

    @RequestMapping(value = "/api/test/findAllBySpecAndSort", method = RequestMethod.POST)
    public ResponseDTO findAllBySpecAndSort(){
        List<Person> list = studentService.findAllBySpecAndSort();
        return getResponseDTO("200", "操作成功", list);
    }

    @RequestMapping(value = "/api/test/findAllPage", method = RequestMethod.POST)
    public ResponseDTO findAllPage(){
        Page<Person> page = studentService.findAllPage();
        return getResponseDTO("200", "操作成功", page);
    }

    @RequestMapping(value = "/api/test/findAllPageBySpec", method = RequestMethod.POST)
    public ResponseDTO findAllPageBySpec(){
        Page<Person> page = studentService.findAllPageBySpec();
        return getResponseDTO("200", "操作成功", page);
    }

    @RequestMapping(value = "/api/test/findMaxId", method = RequestMethod.POST)
    public ResponseDTO findMaxId(){
        Long id = studentService.findMaxId();
        return getResponseDTO("200", "操作成功", id);
    }

    @RequestMapping(value = "/api/test/importExcel", method = RequestMethod.POST)
    public ResponseDTO importExcel(@RequestParam(value="filename") MultipartFile file) throws Exception {

        //判断文件是否为空
        if(file==null){
            return getResponseDTO("201", "文件为空", null);
        }

        //获取文件名
        String fileName=file.getOriginalFilename();

        String[][] datas = ExcelOperate.getData(file, 0, false, 0);

        return getResponseDTO("200", fileName, null);
    }

    @RequestMapping(value = "/api/test/resttest")
    public ResponseDTO<String> restTest(){
        LOGGER.info("harry nice");
        return new ResponseDTO<>("200","nice");
    }

    @RequestMapping(value = "/api/test/resttest1")
    public ResponseDTO<String> restTest1(){
        LOGGER.info("harry nice1");
        return new ResponseDTO<>("200","nice");
    }

    @RequestMapping(value = "/api/test/resttest2")
    public ResponseDTO<String> restTest2(){
        LOGGER.info("harry nice1");
        return new ResponseDTO<>("200","nice");
    }

    @RequestMapping(value = "/api/test/sendmail")
    public ResponseDTO<String> restSendMail(){
        LOGGER.info("harry send Mail");

        BaseAuditingEntity entity = new BaseAuditingEntity() {

        };
        entity.setId(22L);
        entity.setStatus("init");
        mailService.sendEmailFromTemplate(entity, "xuhai@sc-troy.com", "mailDemo2", "mail test2");

        Mail mail = new Mail();
        mail.setFirstName("Harry");
        mail.setLastName("Xu");
        mail.setNationality("China");
        mailService.sendEmailFromTemplate(mail,"xuhai@sc-troy.com","mailDemo","mail test");
        return new ResponseDTO<>("200","nice");
    }

    public static class Mail {
        String firstName;
        String lastName;
        String nationality;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }
    }

    @RequestMapping(value = "/api/test/testTx")
    public ResponseDTO testTx(){
        commonService.testTx();
        return success();
    }
}
