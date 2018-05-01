package com.troy.keeper.system.service.impl;

import com.troy.keeper.system.AppApp;
//import com.troy.keeper.system.dto.SmUserDTO;
//import com.troy.keeper.system.service.SmUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Harry on 2017/9/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
@Transactional
//@Import(DatabaseConfiguration.class)
//@AutoConfigureTestDatabase
@ActiveProfiles("dev")
public class SmUserServiceImplTest {
//
//    //@Autowired
//    private SmUserService smUserService;
//
//    @Test
//    public void testCreatedData() throws Exception {
//        SmUserDTO userDTO = new SmUserDTO();
//        smUserService.createdData(userDTO);
//    }
}