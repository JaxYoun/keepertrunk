---
title: OneToMany
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

# OneToMany 例子说明


设有 2 个实体，分别是班级，学生，是一对多关系。

## 实现步骤

### 1 建表 我们用 sql 语句建立者两个表，也可以使用 hibernate 自己创建。



-- 创建 school_class 表 --
DROP TABLE IF EXISTS `school_class`;
CREATE TABLE `school_class` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`created_by` bigint(20) NOT NULL,
`created_date` bigint(20) NOT NULL,
`last_modified_by` bigint(20) DEFAULT NULL,
`last_modified_date` bigint(20) DEFAULT NULL,
`data_status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
`class_number` int(11) DEFAULT NULL,
`grade` int(11) DEFAULT NULL,
`stu_number` int(11) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 创建 student 表 --
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`created_by` bigint(20) NOT NULL,
`created_date` bigint(20) NOT NULL,
`last_modified_by` bigint(20) DEFAULT NULL,
`last_modified_date` bigint(20) DEFAULT NULL,
`data_status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
`age` int(11) DEFAULT NULL,
`name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
`sex` tinyint(4) DEFAULT NULL,
`school_class_id` bigint(20) DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `FKfun0xyj3ifo7h4fhdbh9uc5d5` (`school_class_id`),
CONSTRAINT `FKfun0xyj3ifo7h4fhdbh9uc5d5` FOREIGN KEY (`school_class_id`) REFERENCES `school_class` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

### 2 在 domain 包下建实体类 Employee 和 ParkingSpace



班级类：
@Entity
@Table(name = "school_class")
public class SchoolClass extends BaseAuditingEntity {

    //班级号
    @Column(name = "class_number")
    private Integer classNumber;

    //所属年级
    @Column(name = "grade")
    private Integer grade;

    //班级人数
    @Column(name = "stu_number")
    private Integer stuNumber;

    //学生
    @OneToMany(mappedBy = "schoolClass",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Student> studentList;
    //此处省略getter和setter的代码-
    }

学生类：
@Entity
@Table(name = "student")
public class Student extends BaseAuditingEntity {

    //学生名
    @Column(name = "name")
    private String name;


    //年级
    @Column(name = "age")
    private Integer age;


    //性别 女0，男1
    @Column(name = "sex")
    private Byte sex;

    //所属班级
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    //此处省略getter和setter的代码-
    }

### 3 在 repository 下建 接口 SchoolClassRepository，StudentRepository



```
public interface StudentRepository extends BaseRepository<Student,Long> {

    @Modifying
    @Query("delete from Student s where s.schoolClass.id = ?1")
    public void deleteByClassId(Long id);

}

public interface SchoolClassRepository extends BaseRepository<SchoolClass,Long> {

    @Query("select count (s.id) from SchoolClass s where s.classNumber = ?1 and s.grade = ?2 ")
    public Long addCheck(Integer classNumber,Integer grade);

    @Query("select count (s.id) from SchoolClass s where s.classNumber = ?1 and s.grade = ?2 and s.id <> ?3")
    public Long updateCheck(Integer classNumber,Integer grade,Long id);
}
```

### 4 在 dto 包下建 SchoolClassDTO，StudentDTO 类



```
  public class SchoolClassDTO extends BaseDTO{

    private Long id;

    //班级号
    private Integer classNumber;

    //所属年级
    private Integer grade;

    //班级人数
    private Integer stuNumber;

    private List<StudentDTO> studentDTOList;
    //此处省略getter和setter的代码-
    }

  public class StudentDTO extends BaseDTO{
    //学生名
    private String name;

    //年级
    private Integer age;

    //性别 女0，男1
    private Byte sex;
    //此处省略getter和setter的代码-
    }
```

### 5 可以在 service>mapper 包下使用 mapstruct 创建类 mapper,此处实体和 dto 的转换使用 spring 的 BeanUtils。



```
	例如：BeanUtils.copyProperties(consumerOrderDTO,consumerOrder);
	该方法的第一个参数是source，第一个参数是target。达到将第一个类里面的属性拷贝到第二个类的目的。
```

### 6 在 service 包下 SchoolClassService 接口，在 service>impl 包下建 SchoolClassServiceImpl 实现类



```
public interface SchoolClassService {

    /**
     * 添加保存
     * @param schoolClassDTO
     */
    public String saveOrUpdate(SchoolClassDTO schoolClassDTO);

    /**
     * 根据id删除
     * @param id
     */
    public void deleteById(Long id);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    public Page<SchoolClassDTO> findByPage(Pageable pageable);
}

//将实现类创建为spring bean
@Service
@Transactional
public class SchoolClassServiceImpl implements SchoolClassService {

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 添加保存
     * @param schoolClassDTO
     */
    @Override
    public String saveOrUpdate(SchoolClassDTO schoolClassDTO) {
        Long count = schoolClassDTO.getId() == null?schoolClassRepository.addCheck(schoolClassDTO.getClassNumber(),schoolClassDTO.getGrade()):
                schoolClassRepository.updateCheck(schoolClassDTO.getClassNumber(),schoolClassDTO.getGrade(),schoolClassDTO.getId());
        if (count >0l){
            return "该班级已经存在！";
        }

        SchoolClass schoolClass = null;
        if (schoolClassDTO.getId() != null){
            schoolClass = schoolClassRepository.findOne(schoolClassDTO.getId());
            //删除学生
            studentRepository.deleteByClassId(schoolClassDTO.getId());
        }else{
            schoolClass = new SchoolClass();
        }
        BeanUtils.copyProperties(schoolClassDTO,schoolClass);
        schoolClass = schoolClassRepository.save(schoolClass);

        if (schoolClassDTO.getStudentDTOList() != null && !schoolClassDTO.getStudentDTOList().isEmpty()){
            List<Student> studentList = new ArrayList<>(schoolClassDTO.getStudentDTOList().size());
            for (StudentDTO studentDTO : schoolClassDTO.getStudentDTOList()) {
                Student student = new Student();
                BeanUtils.copyProperties(studentDTO,student);
                student.setSchoolClass(schoolClass);
                studentList.add(student);
            }
//            schoolClass.setStudentList(studentList);
            studentRepository.save(studentList);
        }
        return null;
    }


    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        schoolClassRepository.delete(id);
    }

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    public Page<SchoolClassDTO> findByPage(Pageable pageable) {
        Page<SchoolClass> page = schoolClassRepository.findAll(pageable);
        return page.map(new Converter<SchoolClass, SchoolClassDTO>() {
            @Override
            public SchoolClassDTO convert(SchoolClass schoolClass) {
                SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
                BeanUtils.copyProperties(schoolClass,schoolClassDTO);
                return schoolClassDTO;
            }
        });
    }
}
```

### 7 在 web.rest 包下建 SchoolClassResource 类，实现 rest api 接口



```
//将类创建为spring bean,该注解包含了@Controller
//@ResponseBody两个注解的功能。ResponseBody用于将返回的对了转换成json
@RestController
//添加访问路径
@RequestMapping("/api/stu")
public class SchoolClassResource extends BaseResource {

    @Autowired
    private SchoolClassService schoolClassService;

    /**
     * 添加保存
     * @param schoolClassDTO
     */
    @RequestMapping("/saveOrUpdate")
    public ResponseDTO saveOrUpdate(@RequestBody SchoolClassDTO schoolClassDTO){
        if (schoolClassDTO.getClassNumber() == null || schoolClassDTO.getGrade() == null
                || schoolClassDTO.getStuNumber() == null){
            return getResponseDTO("202","参数错误",null);
        }
        String msg = schoolClassService.saveOrUpdate(schoolClassDTO);
        if (StringUtils.isEmpty(msg)){
            return getResponseDTO("200","操作成功！", null);
        }
        return getResponseDTO("202",msg,null);
    }

    /**
     * 根据id删除
     * @param schoolClassDTO
     */
    @RequestMapping("/deleteById")
    public ResponseDTO deleteById(@RequestBody SchoolClassDTO schoolClassDTO){
        if (schoolClassDTO.getId() == null){
            getResponseDTO("202","参数错误！",null);
        }
        schoolClassService.deleteById(schoolClassDTO.getId());
        return getResponseDTO("200","操作成功",null);
    }

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @RequestMapping("/findByPage")
    public ResponseDTO<SchoolClassDTO> findByPage(@PageableDefault Pageable pageable){
        return getResponseDTO("200","操作成功",schoolClassService.findByPage(pageable));
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.printf(date.getTime()+"");
    }

}
```

接口代码完成，用 postman 进行接口的测试
