package com.troy.keeper.monomer.portal.account.domain.projection;

import com.troy.keeper.monomer.portal.account.domain.Address;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Harry
 * Date: 17-7-6
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public interface CustomProjection {
    String getDetail();


    //等效
    @Value("#{target.addresses}")
    List<Address> getAddresses();
}
