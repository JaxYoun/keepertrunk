---
title: OneToMany+ManyToOne（ManyToMany）
tag: 后端开发
publishDate: 2018-03-13
---


## 包的说明

**domain:** domain 包下是对实体的定义
**service:** 服务的接口定义和实现
**repository:** repository 包下是对数据接入层接口的定义
**web.rest:** REST API 实现
**dto:** DTO 起到业务数据的传递作用
**mapper:** 实体和 DTO 之间互相转换

# ManyToMany 例子说明

---

设有 3 个实体，分别是用户订单，银行卡记录，两者中间实体，订单和刷卡消费是多对多关系。这里采用中间表的形式维护关系。

---

## 实现步骤

### 1 建表 我们用 sql 语句建立者两个表，也可以使用 hibernate 自己创建。

---

-- 创建 bank_card 表 --
DROP TABLE IF EXISTS `bank_card`;
CREATE TABLE `bank_card` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`created_by` bigint(20) NOT NULL,
`created_date` bigint(20) NOT NULL,
`last_modified_by` bigint(20) DEFAULT NULL,
`last_modified_date` bigint(20) DEFAULT NULL,
`data_status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
`bank_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
`card_num` varchar(255) COLLATE utf8_bin DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 创建 card_order_middle 表 --
DROP TABLE IF EXISTS `card_order_middle`;
CREATE TABLE `card_order_middle` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`created_by` bigint(20) NOT NULL,
`created_date` bigint(20) NOT NULL,
`last_modified_by` bigint(20) DEFAULT NULL,
`last_modified_date` bigint(20) DEFAULT NULL,
`data_status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
`bank_card_id` bigint(20) DEFAULT NULL,
`consumer_order_id` bigint(20) DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `FK4mngcgqlgrg3plvnlcd96d9c6` (`bank_card_id`),
KEY `FKopd39gh9k6l2bl6232qsmtfmh` (`consumer_order_id`),
CONSTRAINT `FK4mngcgqlgrg3plvnlcd96d9c6` FOREIGN KEY (`bank_card_id`) REFERENCES `bank_card` (`id`),
CONSTRAINT `FKopd39gh9k6l2bl6232qsmtfmh` FOREIGN KEY (`consumer_order_id`) REFERENCES `consumer_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- 创建 consumer_order 表 --
DROP TABLE IF EXISTS `consumer_order`;
CREATE TABLE `consumer_order` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`created_by` bigint(20) NOT NULL,
`created_date` bigint(20) NOT NULL,
`last_modified_by` bigint(20) DEFAULT NULL,
`last_modified_date` bigint(20) DEFAULT NULL,
`data_status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
`goods_num` int(11) DEFAULT NULL,
`store_num` int(11) DEFAULT NULL,
`total_money` double DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

### 2 在 domain 包下建实体类 BankCard，ConsumerOrder 和 CardOrderMiddle

---

银行卡类：

    @Entity
    @Table(name = "bank_card")
    public class BankCard extends BaseAuditingEntity {

    //卡号
    @Column(name = "card_num")
    private String cardNum;

    //银行名
    @Column(name = "bank_name")
    private String bankName;

    //关联中间表
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "bankCard")
    private List<CardOrderMiddle> cardOrderMiddleList;
    //------此处省略getter和setter的代码----------
    }

中间表类：
@Entity
//指定数据库表名
@Table(name = "card_order_middle")
public class CardOrderMiddle extends BaseAuditingEntity {

    /**
     * 银行卡信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_card_id")
    private  BankCard bankCard;

    /**
     * 用户订单信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_order_id")
    private ConsumerOrder consumerOrder;

    //------此处省略getter和setter的代码----------
    }

用户订单类：
@Entity
@Table(name = "consumer_order")
public class ConsumerOrder extends BaseAuditingEntity {

    //门店号码
    @Column(name = "store_num")
    private Integer storeNum;

    //商品数量
    @Column(name = "goods_num")
    private Integer goodsNum;

    //消费总金额
    @Column(name = "total_money")
    private Double totalMoney;

    //中间信息
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "consumerOrder")
    private List<CardOrderMiddle> cardOrderMiddleList;
    //------此处省略getter和setter的代码----------
    }

### 3 在 repository 下建 接口 BankCardRepository ，CardOrderMiddleRepository ，ConsumerOrderRepository

---

```
public interface BankCardRepository extends BaseRepository<BankCard,Long> {

  /**
   * 此处省略了hql语句，具体用法参看 spring-data-jpa 文档
   * @param cardNum
   * @return
   */
  BankCard findByCardNum(String cardNum);
}

public interface CardOrderMiddleRepository extends BaseRepository<CardOrderMiddle,Long> {

  @Modifying
  @Query("delete from CardOrderMiddle m where m.consumerOrder.id in (?1)")
  public void deleteByOrderIds(List<Long> ids);
}

public interface ConsumerOrderRepository extends BaseRepository<ConsumerOrder,Long> {

  /**
   * 在query注解里面写hql语句
   *
   * 更新删除需要在方法上加上@Modifying注解
   * @param ids
   */
  @Modifying
  @Query("delete from ConsumerOrder c where c.id in (?1)")
  public void deleteByIds(List<Long> ids);
}
```

### 4 在 dto 包下建 EmployeeDTO 类

---

```
  public class BankCardDTO extends BaseDTO {

    //卡号
    private String cardNum;

    //银行名
    private String bankName;
    //------此处省略getter和setter的代码----------
    }

  public class ConsumerOrderDTO extends BaseDTO{

    private Long id;

    //门店号码
    private Integer storeNum;

    //商品数量
    private Integer goodsNum;


    //消费总金额
    private Double totalMoney;

    //银行卡信息
    private List<BankCardDTO> bankCardDTOList;

    //订单id集合
    private List<Long> ids;
    //------此处省略getter和setter的代码----------
    }
```

### 5 可以在 service>mapper 包下使用 mapstruct 创建类 mapper,此处实体和 dto 的转换使用 spring 的 BeanUtils。

---

```
	例如：BeanUtils.copyProperties(consumerOrderDTO,consumerOrder);
	该方法的第一个参数是source，第一个参数是target。达到将第一个类里面的属性拷贝到第二个类的目的。
```

### 6 在 service 包下 ConsumerOrderService 接口，在 service>impl 包下建 ConsumerOrderServiceImpl 实现类

---

```
public interface ConsumerOrderService {

    /**
     * 添加
     */
    public void createOrUpdateConsumerOrder(ConsumerOrderDTO consumerOrderDTO);

    /**
     * 删除
     * @param ids
     */
    public void deleteOrder(List<Long> ids);


    /**
     * 根据条件分页查询
     * @param pageable
     * @param consumerOrderDTO
     * @return
     */
    Page<ConsumerOrderDTO> findOrderByPage(Pageable pageable,ConsumerOrderDTO consumerOrderDTO);

}


//将实现类创建为spring bean
@Service
//添加事物
@Transactional
public class ConsumerOrderServiceImpl implements ConsumerOrderService {

    //注入bean
    @Autowired
    private BankCardRepository bankCardRepository;


    @Autowired
    private ConsumerOrderRepository consumerOrderRepository;

    @Autowired
    private CardOrderMiddleRepository cardOrderMiddleRepository;

    /**
     * 添加
     */
    @Override
    public void createOrUpdateConsumerOrder(ConsumerOrderDTO consumerOrderDTO) {
        ConsumerOrder consumerOrder = null;
        if (consumerOrderDTO.getId() != null){//是修改时
            consumerOrder = consumerOrderRepository.findOne(consumerOrderDTO.getId());
            if (consumerOrder != null){
                //删除中间表信息
                List<CardOrderMiddle> cardOrderMiddleList = consumerOrder.getCardOrderMiddleList();
                if (cardOrderMiddleList != null && !cardOrderMiddleList.isEmpty()){
                    cardOrderMiddleRepository.delete(cardOrderMiddleList);
                }
            }
        }

        //保存银行卡信息
        List<BankCard> bankCardList = null;
        if (consumerOrderDTO.getBankCardDTOList() != null && !consumerOrderDTO.getBankCardDTOList().isEmpty()){
           bankCardList = saveBankCard(consumerOrderDTO.getBankCardDTOList());
        }

        //保存用户订单信息
        consumerOrder = saveConsumerOrder(consumerOrder ==  null?new ConsumerOrder():consumerOrder,consumerOrderDTO);

        //保存中间信息
        saveMiddle(bankCardList,consumerOrder);
    }

    /***
     * 删除
     * @param ids
     */
    @Override
    public void deleteOrder(List<Long> ids) {
        //先删除中间表信息
        cardOrderMiddleRepository.deleteByOrderIds(ids);
        consumerOrderRepository.deleteByIds(ids);
    }


    /**
     * 根据条件分页查询
     * @param pageable
     * @param consumerOrderDTO
     * @return
     */
    @Override
    public Page<ConsumerOrderDTO> findOrderByPage(Pageable pageable, ConsumerOrderDTO consumerOrderDTO) {

        /**
         *  分页查询，使用spring data jpa 自带的findAll方法，并且实现其内部类
         *
         */
        Page<ConsumerOrder> page = consumerOrderRepository.findAll(new Specification<ConsumerOrder>() {
            @Override
            public Predicate toPredicate(Root<ConsumerOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //查询条件集合
                List<Predicate> predicateList = new ArrayList<>();
                //添加查询条件
                if (consumerOrderDTO.getStoreNum() != null){
                    predicateList.add(criteriaBuilder.equal(root.get("storeNum"),consumerOrderDTO.getStoreNum()));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        },pageable);//分页参数，包含排序等

        //将实体转换成dto
        return page.map(new Converter<ConsumerOrder, ConsumerOrderDTO>() {
            @Override
            public ConsumerOrderDTO convert(ConsumerOrder consumerOrder) {
                ConsumerOrderDTO dto = new ConsumerOrderDTO();
                BeanUtils.copyProperties(consumerOrder,dto);
                return dto;
            }
        });
    }


    /**
     * 保存银行卡信息
     * @param bankCardDTOList
     * @return
     */
    private List<BankCard> saveBankCard(List<BankCardDTO> bankCardDTOList){
        List<BankCard> bankCardList = new ArrayList<>(bankCardDTOList.size());
        for (BankCardDTO bankCardDTO : bankCardDTOList) {
            BankCard bankCard = bankCardRepository.findByCardNum(bankCardDTO.getCardNum());
            if (bankCard == null) {
                bankCard = new BankCard();
                //使用spring beanutils拷贝数据
                BeanUtils.copyProperties(bankCardDTO,bankCard);
                bankCardList.add(bankCardRepository.save(bankCard));
            }else{
                bankCardList.add(bankCard);
            }
        }
        return bankCardList;
    }

    /**
     * 保存订单
     * @param consumerOrder
     * @param consumerOrderDTO
     * @return
     */
    private ConsumerOrder saveConsumerOrder(ConsumerOrder consumerOrder,ConsumerOrderDTO consumerOrderDTO){
        BeanUtils.copyProperties(consumerOrderDTO,consumerOrder);
        return consumerOrderRepository.save(consumerOrder);
    }

    /**
     * 保存订单-银行卡中间表
     * @param bankCardList
     */
    private void saveMiddle(List<BankCard> bankCardList,ConsumerOrder consumerOrder){
        if (bankCardList != null){
            for (BankCard bankCard : bankCardList) {
                CardOrderMiddle cardOrderMiddle = new CardOrderMiddle();
                cardOrderMiddle.setBankCard(bankCard);
                cardOrderMiddle.setConsumerOrder(consumerOrder);
                cardOrderMiddleRepository.save(cardOrderMiddle);
            }
        }
    }
}
```

### 7 在 web.rest 包下建 ConsumerOrderResource 类，实现 rest api 接口

---

```
//将类创建为spring bean,该注解包含了@Controller
//@ResponseBody两个注解的功能。ResponseBody用于将返回的对了转换成json
@RestController
//添加访问路径
@RequestMapping("/api/order")
public class ConsumerOrderResource extends BaseResource {

    @Autowired
    private ConsumerOrderService consumerOrderService;


    /**
     * 添加
     * @param consumerOrderDTO
     */
    @RequestMapping("/createConsumerOrder")
    public ResponseDTO createConsumerOrder(@RequestBody ConsumerOrderDTO consumerOrderDTO){
        consumerOrderService.createOrUpdateConsumerOrder(consumerOrderDTO);
        return getResponseDTO("200", "操作成功", null);
    }

    /**
     * 修改
     * @param consumerOrderDTO
     */
    @RequestMapping("/updateConsumerOrder")
    public ResponseDTO updateConsumerOrder(@RequestBody ConsumerOrderDTO consumerOrderDTO){
        if (consumerOrderDTO.getId() == null){
            return getResponseDTO("202","参数错误",null);
        }
        consumerOrderService.createOrUpdateConsumerOrder(consumerOrderDTO);
        return getResponseDTO("200", "操作成功", null);
    }

    /**
     * 删除
     * @param consumerOrderDTO
     */
    @RequestMapping("/deleteOrder")
    public ResponseDTO deleteOrder(@RequestBody ConsumerOrderDTO consumerOrderDTO){
        if (consumerOrderDTO.getIds() == null || consumerOrderDTO.getIds().isEmpty()){
            return getResponseDTO("202","参数错误",null);
        }
        consumerOrderService.deleteOrder(consumerOrderDTO.getIds());
        return getResponseDTO("200", "操作成功", null);
    }

    /**
     * 根据条件分页查询
     * @param pageable
     * @param consumerOrderDTO
     * @return
     */
    @RequestMapping("/findOrderByPage")
    public ResponseDTO findOrderByPage(@PageableDefault Pageable pageable, @RequestBody ConsumerOrderDTO consumerOrderDTO){
        return getResponseDTO("200", "操作成功",
                consumerOrderService.findOrderByPage(pageable,consumerOrderDTO));
    }

}
```

接口代码完成，用 postman 进行接口的测试
