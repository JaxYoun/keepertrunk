package com.troy.keeper.monomer.portal.account.web.rest;


import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.monomer.portal.account.domain.Person;
import com.troy.keeper.monomer.portal.account.domain.Student;
import com.troy.keeper.monomer.portal.account.service.StudentService;
import com.troy.keeper.monomer.portal.account.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
            commonService.save(json, "com.troy.keeper.monomer.portal.account.domain.Person");
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
}
