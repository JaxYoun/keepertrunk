---
title: OneToOne
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

# one to one 例子说明

设有 2 个实体，雇员和停车位 。一个雇员只能拥有一个停车位，一个停车位只能属于一个雇员，因此它们是 1 对 1 的关系。在 1 对 1 关系中，我们需要区分主导者和从属者，所谓的主导者就是拥有外键的实体。本例中我们将雇员设置为主导者。目标是实现功能雇员的增删查改，停车位级联增删查改。

---

## 实现步骤

### 1 建表 我们用 sql 语句建立者两个表

-- 创建 EMPLOYEE 表 --

    CREATE TABLE EMPLOYEE(
    ID INTEGER NOT NULL AUTO_INCREMENT ,
    PSPACE_ID INTEGER NOT NULL ,
    NAME VARCHAR (20) NOT NULL ,
    SALARY INTEGER NOT NULL ,
    LAST_UPDATED_TIME TIMESTAMP NOT NULL ,
    PRIMARY KEY (ID),
    FOREIGN KEY (PSPACE_ID) REFERENCES PARKING_SPACE (ID)
    );

-- 创建 PARKING_SPACE 表 --
CREATE TABLE PARKING_SPACE(
ID INTEGER NOT NULL AUTO_INCREMENT ,
LOT INTEGER NOT NULL ,
LOCATION VARCHAR (100) NOT NULL ,
LAST_UPDATED_TIME TIMESTAMP NOT NULL ,
PRIMARY KEY (ID)
);

### 2 在 domain 包下建实体类 Employee 和 ParkingSpace

-雇员类：

    @Table(name ="employee")
    @Entity
    public class Employee implements Serializable {
    /*主键*/
    @Id
    @Column(name="ID", nullable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /*姓名*/
    @Column(name="NAME", nullable=false)
    private String name;
    /*工资*/
    @Column(name="SALARY", nullable=false)
    private Integer salary;
    /*停车位，级联保存更新和删除*/
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="PSPACE_ID")
    private ParkingSpace pSpace;
    /*更新时间*/
    @Version
    @Column(name="LAST_UPDATED_TIME", nullable=false)
    private Date employeeUpdateTime;
    //------此处省略getter和setter的代码-
    }

-停车位类：
@Table(name="parking*space")
@Entity
public class ParkingSpace implements Serializable {
/*主键*/
@Id
@Column(name="ID", nullable=false)
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;
/*大小*/
@Column(name="LOT", nullable=false)
private Integer lot;
/*位置*/
@Column(name="LOCATION", nullable=false)
private String location;
/*更新时间\_/
@Version
@Column(name="LAST_UPDATED_TIME", nullable=false)
private Date parkingSpaceUpdateTime;

    @OneToOne(mappedBy="pSpace")
    private Employee employee;

    //------此处省略getter和setter的代码-
    }

### 3 在 repository 下建 接口 EmployeeRepository 和 ParkingSpaceRepository

```
public interface EmployeeRepository extends BaseRepository<Employee,Serializable>{

}

public interface ParkingSpaceRepository extends BaseRepository<ParkingSpace,Serializable> {

}
```

### 4 在 dto 包下建 EmployeeDTO 类

```
public class EmployeeDTO {
    /*主键*/
    private Long id;
    /*姓名*/
    private String name;
    /*工资*/
    private Integer salary;
    /*雇员更新时间*/
    private String employeeUpdateTime;
    /*停车位主键id*/
    private Long parkingSpaceId;
    /*停车位大小*/
    private Integer lot;
    /*停车位位置*/
    private String location;
    /*停车位更新时间*/
    private String parkingSpaceUpdateTime;
    //------此处省略getter和setter的代码-
    }
```

### 5 在 service>mapper 包下建 EmployeeMapper

---

```
@Mapper
public interface EmployeeMapper {
    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);
   @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "salary",target = "salary"),
           @Mapping(source = "employeeUpdateTime",target = "employeeUpdateTime",dateFormat = "yyyy-MM-dd HH:mm:ss"),
           @Mapping(source = "pSpace.id",target = "parkingSpaceId"),
           @Mapping(source = "pSpace.lot",target = "lot"),
           @Mapping(source = "pSpace.location",target = "location"),
           @Mapping(source = "pSpace.parkingSpaceUpdateTime",target = "parkingSpaceUpdateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
         })
   EmployeeDTO employeeToEmployeeDTO(Employee employee);
}
```

mapper 使用 mapstruct，需要先在 pom 中添加依赖，如下所示：

        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-jdk8</artifactId>
            <version>1.1.0.Final</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>1.1.0.Final</version>
            <scope>provided</scope>
        </dependency>

再 build 自动生成实现类 EmployeeMapperImpl，请在以下的位置检查实现类是否已经生成，内容是否如自己预期

​ target>generated-source>annotations

EmployeeMapperImpl 内容如下：

```
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setParkingSpaceId( employeePSpaceId( employee ) );
        employeeDTO.setLot( employeePSpaceLot( employee ) );
        employeeDTO.setName( employee.getName() );
        employeeDTO.setLocation( employeePSpaceLocation( employee ) );
        employeeDTO.setId( employee.getId() );
        Date pSpace = employeePSpaceParkingSpaceUpdateTime( employee );
        if ( pSpace != null ) {
            employeeDTO.setParkingSpaceUpdateTime( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( pSpace ) );
        }
        employeeDTO.setSalary( employee.getSalary() );
        if ( employee.getEmployeeUpdateTime() != null ) {
            employeeDTO.setEmployeeUpdateTime( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( employee.getEmployeeUpdateTime() ) );
        }

        return employeeDTO;
    }

    private Long employeePSpaceId(Employee employee) {

        if ( employee == null ) {
            return null;
        }
        ParkingSpace pSpace = employee.getpSpace();
        if ( pSpace == null ) {
            return null;
        }
        Long id = pSpace.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Integer employeePSpaceLot(Employee employee) {

        if ( employee == null ) {
            return null;
        }
        ParkingSpace pSpace = employee.getpSpace();
        if ( pSpace == null ) {
            return null;
        }
        Integer lot = pSpace.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot;
    }

    private String employeePSpaceLocation(Employee employee) {

        if ( employee == null ) {
            return null;
        }
        ParkingSpace pSpace = employee.getpSpace();
        if ( pSpace == null ) {
            return null;
        }
        String location = pSpace.getLocation();
        if ( location == null ) {
            return null;
        }
        return location;
    }

    private Date employeePSpaceParkingSpaceUpdateTime(Employee employee) {

        if ( employee == null ) {
            return null;
        }
        ParkingSpace pSpace = employee.getpSpace();
        if ( pSpace == null ) {
            return null;
        }
        Date parkingSpaceUpdateTime = pSpace.getParkingSpaceUpdateTime();
        if ( parkingSpaceUpdateTime == null ) {
            return null;
        }
        return parkingSpaceUpdateTime;
    }
}
```

### 6 在 service 包下 IEmployeeService 接口，在 service>impl 包下建 EmployeeServiceImpl 实现类

```
public interface IEmployeeService {
    /**
     *  添加雇员，级联保存停车位
     * @param employeeDTO
     */
   public void add(EmployeeDTO employeeDTO);

    /**
     * 级联删除雇员和停车位
     * @param id
     */
    public void delete(Long id);

    /**
     * 修改雇员和停车位信息
     * @param employeeDTO
     */
    public void modify(EmployeeDTO employeeDTO);

    /**
     * 查询雇员信息-分页
     * @param husband
     * @param pageable
     * @return
     */
    public Page<EmployeeDTO> queryPage(EmployeeDTO husband, Pageable pageable);

}

@Transactional
@Service
public class EmployeeServiceImpl implements IEmployeeService {
   @Autowired
   EmployeeRepository employeeRepository;
    /**
     * 添加雇员，级联保存雇员信息
     * @param employeeDTO
     */
    @Override
    public void add(EmployeeDTO  employeeDTO){
        //设置从属者停车位的信息
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setLocation(employeeDTO.getLocation());
        parkingSpace.setLot(employeeDTO.getLot());
        parkingSpace.setParkingSpaceUpdateTime(new Date());
        //设置主导者雇员的信息
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setEmployeeUpdateTime(new Date());
        //设置雇员的停车位
        employee.setpSpace(parkingSpace);
        //级联保存雇员和停车位
        employeeRepository.save(employee);
    }
    /**
     * 删除雇员，级联删除停车位
     * @param id
     */
    @Override
    public void delete(Long id){
        Employee employee = employeeRepository.findOne(id);
        if(employee != null) {
            employeeRepository.delete(employee);
        }
    }

    /**
     * 修改雇员信息
     * @param employeeDTO
     */
    @Override
    public void modify(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findOne(employeeDTO.getId());
        if (employee != null) {
           if(employeeDTO.getName() != null){
               employee.setName(employeeDTO.getName());
           }
           if(employeeDTO.getSalary() != null){
               employee.setSalary(employeeDTO.getSalary());
           }
           employee.setEmployeeUpdateTime(new Date());
            ParkingSpace parkingSpace = employee.getpSpace();
            if(parkingSpace != null){
                if(employeeDTO.getLot() != null){
                    parkingSpace.setLot(employeeDTO.getLot());
                }
                if(employeeDTO.getLocation() != null){
                    parkingSpace.setLocation(employeeDTO.getLocation());
                }
                parkingSpace.setParkingSpaceUpdateTime(new Date());
            }
            employeeRepository.save(employee);
        }
    }

    /**
     * 查询雇员信息
     * @param employeeDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<EmployeeDTO> queryPage(EmployeeDTO employeeDTO, Pageable pageable){
        Specification<Employee> specification = createSpecification(employeeDTO);
       return  employeeRepository.findAll(specification,pageable).map(h -> {
             EmployeeDTO employeeDTO1 = EmployeeMapper.MAPPER.employeeToEmployeeDTO(h);
             return employeeDTO1;
         });
    }

    /**
     * 创建查询条件
     * @param employeeDTO
     * @return
     */

    private Specification<Employee> createSpecification(EmployeeDTO employeeDTO) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> query = new ArrayList<Predicate>();
            List<Order> orders = new ArrayList<Order>();
            orders.add(criteriaBuilder.desc(root.get("employeeUpdateTime")));
            criteriaQuery.orderBy(orders);
            if(employeeDTO.getName() != null){
                    query.add(criteriaBuilder.like(root.get("name"), "%" + employeeDTO.getName() + "%"));
                }
                Predicate[] ps = new Predicate[query.size()];
                return criteriaBuilder.and(query.toArray(ps));
        });
    }
}
```

### 7 在 web.rest 包下建 EmployeeResource 类，实现 rest api 接口

```
@RestController
@RequestMapping(value = "/api/DemoForOneToOne")
public class EmployeeResource extends BaseResource {
    private final Log logger = LogFactory.getLog(EmployeeResource.class);
    @Autowired
    IEmployeeService employeeService;
    /**
     * 添加雇员，级联保存停车位
     * @param employeeDTO
     * @return
     */
   @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseDTO addEmployee(@RequestBody EmployeeDTO employeeDTO){
        try{
            employeeService.add(employeeDTO);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }

    /**
     * 删除雇员和相应的停车位
     * @param idInfo 雇员id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO deleteHusband(@RequestBody Map<String,Long> idInfo){
        try{
            if(idInfo != null && idInfo.get("id") !=null){
                employeeService.delete(idInfo.get("id"));
            }
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }

    /**
     * 修改雇员信息
     * @param employeeDTO
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResponseDTO modify(@RequestBody EmployeeDTO employeeDTO){
        try{
            employeeService.modify(employeeDTO);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }

    /**
     * 查询雇员信息-分页
     * @param employeeDTO
     * @param pageable
     * @return
     */

    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody EmployeeDTO employeeDTO, Pageable pageable) {
        try {
            Page<EmployeeDTO> result = employeeService.queryPage(employeeDTO, pageable);
            return getResponseDTO("200", "操作成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
    }
}
```

接口代码完成，用 postman 进行接口的测试
