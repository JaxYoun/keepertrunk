package com.troy.keeper.monomer.portal.account.repository;

import com.troy.keeper.monomer.portal.account.AppApp;
import com.troy.keeper.monomer.portal.account.domain.Address;
import com.troy.keeper.monomer.portal.account.domain.Custom;
import com.troy.keeper.monomer.portal.account.domain.projection.CustomProjection;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Harry
 * Date: 17-7-4
 * Time: 上午11:33
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
@Transactional
public class CustomRepositoryTest {

    @Autowired
    CustomRepository customRepository;

    @Test
    //执行完成自动回滚
    public void testSave() {
        Custom custom = createCustom();
        System.out.println("id:"+custom.getCustId());

        Custom one = customRepository.findOne(custom.getCustId());

        assertEquals(custom.getDetail(), one.getDetail());

        System.out.printf("custom id:%s, detail:%s", one.getCustId(), one.getDetail());
        System.out.println();

    }


    @Test
    //执行完成自动回滚
    public void testQuery() {
        Custom custom = createCustom();
        Custom harry = customRepository.queryCustomByDetail("Harry");
        assertEquals(custom.getDetail(), harry.getDetail());
        System.out.printf("custom id:%s, detail:%s", harry.getCustId(), harry.getDetail());
        System.out.println();

    }

    private Custom createCustom() {
        Custom custom = new Custom();
        custom.setDetail("I'm Harry");
        Address address = new Address();
        address.setDetail("where am I?");
        custom.pushAddress(address);
        customRepository.save(custom);
        return custom;
    }


    @Test
    public void testProjection() {

        Custom custom = createCustom();

        Custom one = customRepository.findOne(custom.getCustId());
        CustomProjection customProjection = customRepository.findByCustId(custom.getCustId());
        CustomProjection harry = customRepository.queryCustomByDetail2("Harry");

        System.out.printf("find one: %s", ReflectionToStringBuilder.toString(one));
        System.out.println();
        System.out.printf("findByCustId -- detail:%s, fullDetail:%s", customProjection.getDetail(), customProjection.getAddresses());
        System.out.println();
        System.out.printf("queryCustomByDetail2 -- detail:%s, fullDetail:%s", harry.getDetail(), Arrays.toString(harry.getAddresses().toArray()));
        System.out.println();
    }
}
