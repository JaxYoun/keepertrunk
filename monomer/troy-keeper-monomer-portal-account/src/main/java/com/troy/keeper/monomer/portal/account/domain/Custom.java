package com.troy.keeper.monomer.portal.account.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Harry
 * Date: 17-7-4
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Custom {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(Custom.class);

    private Long custId;

    private String detail;

    private List<Address> addresses = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getCustId() {
        return custId;
    }
    public String getDetail() {
        return detail;
    }
    /*
     * 三种配置方式：
     * 1. @OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY ,mappedBy="custom")
     * mappedBy属性用于双向关联实体时使用，
     * mappedBy属性：用在双向关联中，把关系的维护权反转 ； 跟hibernate XML映射中的property-ref一样。
     *
     * JPA执行步骤：一、插入数据到CUSTOM表，
     * 二、执行getAddresses()获取需要持久化的ADDRESS（ADDRESS必须代码设置外键CUSTID），
     * 三、插入数据到ADDRESS表
     *
     * 2.@OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY )
     * 自动生成中间表
     * JPA执行步骤: 一、插入数据到CUSTOM表，
     * 二、执行getAddresses()获取需要持久化的ADDRESS（ADDRESS代码不需要设置外键CUSTID），ADDRESS和CUSTOM关系保存在关联表中；
     * 三、插入数据到ADDRESS表
     * 四、插入关联信息到CUSTOM_ADDRESS 表中
     * 另外关联表的字段对应关系也可以手工设置，
     * @JoinTable(name="ref_customer_address",
     * joinColumns={@JoinColumn(name="customer_id",referencedColumnName="custId")},
     * inverseJoinColumns={@JoinColumn(name="address_id",referencedColumnName="addrId")}
     *
     * 3.@OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY )
     * @JoinColumn(name="customer_id") 对应的是表中的字段，会在最后一步进行该字段的更新
     * 该设置属于单向关联， 该设置需要执行三条SQL操作，不推荐；JPA推荐第一种和第二种做法；
     * JPA执行步骤如下：一、插入数据到CUSTOM表，
     * 二、执行getAddresses()获取需要持久化的ADDRESS（ADDRESS必须代码设置外键CUSTID或者将外键属性设置为可以为空），
     * 三、插入数据到ADDRESS表
     * 四、 然后再UPDATE ADDRESS 设置外键关系customer_id
     *
     * 4.另外多端需要初始化一个空数组
     * private Collection<Address> addresses = new ArrayList<Address>();
     *
     * */
    //级联操作， 通常情况，都是用这种方式
    @OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY ,mappedBy="custom" )
    // 双向关联,mappedBy="customId" 不可共存 @JoinColumn(name="customer_id") ,此处有mapBy, 就没有JoinColumn
    // 2. 自定义中间表方式 @JoinTable(name="ref_customer_address",
    // joinColumns={@JoinColumn(name="customer_id",referencedColumnName="custId")},
    // inverseJoinColumns={@JoinColumn(name="address_id",referencedColumnName="addrId")}
    // )
    public Collection<Address> getAddresses() {
        return addresses;
    }

    public void pushAddress(Address add){
        addresses.add(add);
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}