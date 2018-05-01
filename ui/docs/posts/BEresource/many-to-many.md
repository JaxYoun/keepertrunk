---
order: 201801221522
title: ManyToMany demo 说明文档
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

设有 2 个实体，分别是学生，老师，老师和学生是多对多关系。这里采用中间表的形式维护关系。

---

## 实现步骤

### 1 建表 我们用 sql 语句建立者两个表，也可以使用 hibernate 自己创建。

---

-- 创建 students 表 --
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brithday` varchar(255) COLLATE utf8_bin NOT NULL,
  `gender` varchar(255) COLLATE utf8_bin NOT NULL,
  `s_name` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 创建 teachers 表 --
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_name` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
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

### 2 在 domain 包下建实体类 Students，Teachers

---

学生类：

@Table(name ="students")
@Entity
public class Students extends IdEntity {
    /**
     * 性别
     */
    @Column(name="GENDER", nullable=false)
    private String gender;
    /**
     * 姓名
     */
    @Column(name="S_NAME", nullable=false)
    private String stName;
    /**
     * 生日
     */
    @Column(name="BRITHDAY", nullable=false)
    private String brithday;
    /**
     * 学生持有教师的集合
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="teachers_students",//中间表的表名
            joinColumns={@JoinColumn(name="sid")},//本表的主键
            inverseJoinColumns={@JoinColumn(name="tid")})//所映射表的主键
    private List<Teachers> teachers;
}

老师类：
@Table(name ="teachers")
@Entity
public class Teachers extends IdEntity {
    /**
     * 教师的名字
     */
    @Column(name="T_NAME", nullable=false)
    private String teaName;
    /**
     * 教师持有的学生的集合
     */
    @ManyToMany(mappedBy="teachers" ,fetch = FetchType.LAZY)//把主控方交给学生
    private List<Students> stus;
}

### 3 在 repository 下建 接口 StudentRepository

---

```

public interface StudentRepository extends BaseRepository<Student,Long> {

   /**
   * 根据学生ID删除学生
   * @param cardNum
   * @return
   */
    @Modifying
    @Query("delete from Student s where s.schoolClass.id = ?1")
    public void deleteByClassId(Long id);

}

```

### 4 在 dto 包下建 StudentsDTO，StudentsReturnDTO 类
      StudentsDTO用于接收前台传入数据
	  StudentsReturnDTO用于后台返回数据给前台
---

```
public class StudentsDTO extends BaseDTO {
    /**
     * 分页参数：页数
     */
    private Integer page;

    /**
     * 分页参数：条数
     */
    private Integer size;
    /**
     * 学生id
     */
    private Long sid;
    /**
     * 性别
     */
    private String gender;
    /**
     * 学生姓名
     */
    private String stName;
    /**
     * 生日
     */
    private String brithday;
    /**
     * 老师id字符串(1，2，3，4.....)
     */
    private String teachers;
    /**
     * 老师姓名
     */
    private String teName;

    //------此处省略getter和setter的代码----------
}
public class StudentsReturnDTO {
    /**
     * 学生id
     */
    private Long sid;
    /**
     * 性别
     */
    private String gender;
    /**
     * 学生姓名
     */
    private String stName;
    /**
     * 生日
     */
    private String brithday;
    /**
     * 老师姓名
     */
    private List<String> teName;

   //------此处省略getter和setter的代码----------
}

```

### 5 可以在 service>mapper 包下使用 mapstruct 创建类 mapper,此处实体和 dto 的转换使用 spring 的 BeanUtils。

---

```
	例如：BeanUtils.copyProperties(consumerOrderDTO,consumerOrder);
	该方法的第一个参数是source，第一个参数是target。达到将第一个类里面的属性拷贝到第二个类的目的。
```

### 6 在 service 包下 StudentsService 接口，在 service>impl 包下建 StudentsServiceImpl 实现类

---

```
public interface StudentsService extends BaseService<Students, StudentsDTO> {

    /**
     *  添加学生，级联保存老师关系
     * @param studentsDTO
     */
    public void add(StudentsDTO studentsDTO);
    /**
     *  添加学生和老师
     * @param studentsDTO
     */
    public void addStudentsAndTeacher(StudentsDTO studentsDTO);

    /**
     * 级联删除学生和学生老师关系
     * @param studentsDTO
     */
    public void delete(StudentsDTO studentsDTO);
    /**
     * 修改学生信息和学生老师关系
     * @param studentsDTO
     */
    public void modify(StudentsDTO studentsDTO);

    /**
     * 前端接口：查询学生信息列表接口
     *
     * @param studentsDTO
     * @return
     */
    public List<Map<String, Object>> list(StudentsDTO studentsDTO) throws Exception;
    /**
     * 前端接口：查询学生信息列表接口（分页）
     *
     * @param studentsDTO
     * @return
     */
    public Page<Map<String, Object>> listPage(StudentsDTO studentsDTO) throws Exception;

    /**
     * 后端接口：查询学生信息列表接口
     *
     * @param studentsDTO
     * @return
     */
    public List<Students> listObj(StudentsDTO studentsDTO);
    /**
     * 后端接口：查询学生信息列表接口（分页）
     *
     * @param studentsDTO
     * @return
     */
    public Page<Students> listPageObj(StudentsDTO studentsDTO);
    /**
     * 后端：Students转StudentsReturnDTO
     *
     * @param
     * @return
     */
    public StudentsReturnDTO studentsToStudentsReturnDTO(Students students);
}
//将实现类创建为spring bean
@Service
//添加事物
@Transactional
public class StudentsServiceImpl extends BaseServiceImpl<Students, StudentsDTO> implements StudentsService {

    /**
     * 注入通用Repository
     */
    @Autowired
    private CommonRepository commonRepository;
    @Autowired
    StudentsRepository studentsRepository;
    @Autowired
    TeachersRepository teachersRepository;

    /**
     * 注入实体转LIST OR MAP工具
     */
    @Autowired
    private MapperUtils mapperUtils;

    /**
     *  添加学生，级联保存老师关系
     * @param studentsDTO
     */
    @Override
    public void add(StudentsDTO studentsDTO) {
        String[] tids=studentsDTO.getTeachers().split(",");
        if(tids.length>0){
            List<Long> lsit=new ArrayList<>();
            for(int i=0;i<tids.length;i++){
                lsit.add(Long.parseLong(tids[i]));
            }
            /**
             * 通过老师ID找到该学生的老师集合
             */
            Iterable<Teachers> teachersIterable=teachersRepository.findAll(lsit);
            List<Teachers> teachersList=new ArrayList<>();
            teachersIterable.forEach(singele ->teachersList.add(singele));
            Students student=new Students();
            if(!teachersList.isEmpty()){
                student.setBrithday(studentsDTO.getBrithday());
                student.setGender(studentsDTO.getGender());
                student.setStName(studentsDTO.getStName());
                student.setTeachers(teachersList);
                /**
                 * 保存学生并保存学生老师关系
                 */
                studentsRepository.save(student);
            }
        }else{
            Students student=new Students();
            student.setBrithday(studentsDTO.getBrithday());
            student.setGender(studentsDTO.getGender());
            student.setStName(studentsDTO.getStName());
            /**
             * 保存学生
             */
            studentsRepository.save(student);
        }


    }
    /**
     *  添加学生和老师
     * @param studentsDTO
     */
    @Override
    public void addStudentsAndTeacher(StudentsDTO studentsDTO) {
        Students student=new Students();
        List<Teachers> teachersList=new ArrayList<>();
        Teachers teacher=new Teachers();
        student.setBrithday(studentsDTO.getBrithday());
        student.setGender(studentsDTO.getGender());
        student.setStName(studentsDTO.getStName());
        teacher.setTeaName(studentsDTO.getTeName());

        /**
         * 保存老师
         */
        teachersRepository.save(teacher);
        Teachers teachers=teachersRepository.findByName(studentsDTO.getTeName());
        if(teachers!=null){
            teachersList.add(teachers);
            student.setTeachers(teachersList);
            /**
             * 保存学生.老师学生关系
             */
            studentsRepository.save(student);
        }

    }

    /**
     * 级联删除学生和学生老师关系
     * @param studentsDTO
     */
    @Override
    public void delete(StudentsDTO studentsDTO) {
        Students students=studentsRepository.findOne(studentsDTO.getSid());
        if(students!=null){
            studentsRepository.delete(students);
        }
    }
    /**
     * 修改学生信息和学生老师关系
     * @param studentsDTO
     */
    @Override
    public void modify(StudentsDTO studentsDTO) {
        Students students=studentsRepository.findOne(studentsDTO.getSid());
        if(students!=null){
            if(StringUtils.isNotEmpty(studentsDTO.getTeachers())){
                String[] tids=studentsDTO.getTeachers().split(",");
                if(tids.length>0){
                    List<Long> lsit=new ArrayList<>();
                    for(int i=0;i<tids.length;i++){
                        lsit.add(Long.parseLong(tids[i]));
                    }
                    /**
                     * 通过老师ID找到该学生的老师集合
                     */
                    Iterable<Teachers> teachersIterable=teachersRepository.findAll(lsit);
                    List<Teachers> teachersList=new ArrayList<>();
                    teachersIterable.forEach(singele ->teachersList.add(singele));
                    if(!teachersList.isEmpty()){
                        students.setBrithday(studentsDTO.getBrithday());
                        students.setGender(studentsDTO.getGender());
                        students.setStName(studentsDTO.getStName());
                        students.setTeachers(teachersList);
                        /**
                         * 修改学生.老师学生关系
                         */
                        studentsRepository.save(students);
                    }
                }else{
                    students.setStName(studentsDTO.getStName());
                    students.setGender(studentsDTO.getGender());
                    students.setBrithday(studentsDTO.getBrithday());
                    students.setTeachers(null);
                    /**
                     * 修改学生
                     */
                    studentsRepository.save(students);
                }
            }else{
                students.setStName(studentsDTO.getStName());
                students.setGender(studentsDTO.getGender());
                students.setBrithday(studentsDTO.getBrithday());
                students.setTeachers(null);
                /**
                 * 修改学生
                 */
                studentsRepository.save(students);
            }

        }
    }
    /**
     * 前端接口：查询学生信息
     *
     * @param studentsDTO
     * @return
     */
    @Override
    public List<Map<String, Object>> list(StudentsDTO studentsDTO) throws Exception {
        MapperParam<StudentsReturnDTO> mapperParam = new MapperParam<>();
        List<Students> studentsList=listObj(studentsDTO);
        List<StudentsReturnDTO> studentsReturnDTOList=new ArrayList<>();
        for (Students students:studentsList
             ) {
            StudentsReturnDTO studentsReturnDTO=new StudentsReturnDTO();
            studentsReturnDTO.setBrithday(students.getBrithday());
            studentsReturnDTO.setGender(students.getGender());
            studentsReturnDTO.setSid(students.getId());
            studentsReturnDTO.setStName(students.getStName());
            if(students.getTeachers()!=null){
                List<String> teNameList=new ArrayList<>();
                for (Teachers teacher:students.getTeachers()
                     ) {
                    teNameList.add(teacher.getTeaName());
                }
                studentsReturnDTO.setTeName(teNameList);
            }else{
                studentsReturnDTO.setTeName(null);
            }
            studentsReturnDTOList.add(studentsReturnDTO);
        }
        mapperParam.setList(studentsReturnDTOList);
        return mapperUtils.convertListDefault(mapperParam);
    }
    /**
     * 前端接口：查询学生信息列表接口（分页）
     *
     * @param studentsDTO
     * @return
     */
    @Override
    public Page<Map<String, Object>> listPage(StudentsDTO studentsDTO) throws Exception {
        MapperParam<Students> mapperParam = new MapperParam<>();
        MapperParam<StudentsReturnDTO> mapperParamStudentsReturnDTO = new MapperParam<>();
        Page<Students> studentsPage=listPageObj(studentsDTO);
        if(studentsPage!=null){
            Page<StudentsReturnDTO> newPage=studentsPage.map(new Converter<Students, StudentsReturnDTO>() {
                @Override
                public StudentsReturnDTO convert(Students students) {
                    return studentsToStudentsReturnDTO(students);
                }
            });
            mapperParamStudentsReturnDTO.setPage(newPage);
            return mapperUtils.convertPageDefault(mapperParamStudentsReturnDTO);
        }
        mapperParam.setPage(studentsPage);
        return mapperUtils.convertPageDefault(mapperParam);
    }

    /**
     * 后端接口：查询学生信息列表接口
     *
     * @param studentsDTO
     * @return
     */
    @Override
    public List<Students> listObj(StudentsDTO studentsDTO) {
        return commonRepository.findAll(Students.class, getQuerySpec(studentsDTO));
    }
    /**
     * 后端接口：查询学生信息列表接口（分页）
     *
     * @param studentsDTO
     * @return
     */
    @Override
    public Page<Students> listPageObj(StudentsDTO studentsDTO) {
        PageRequest pageRequest = new PageRequest(studentsDTO.getPage(), studentsDTO.getSize());
        return commonRepository.findAll(pageRequest, Students.class, getQuerySpec(studentsDTO));
    }

    /**
     * 获取查询学生信息条件
     *
     * @param studentsDTO
     * @return
     */
    private Specification<Students> getQuerySpec(StudentsDTO studentsDTO) {
        return(root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            /**
             * 通过名字模糊查询
             */
            if(StringUtils.isNotBlank(studentsDTO.getStName())){
                list.add(cb.like(root.get("stName"), "%" + studentsDTO.getStName() + "%"));
            }
            /**
             * 通过生日模糊查询
             */
            if(StringUtils.isNotBlank(studentsDTO.getBrithday())){
                list.add(cb.like(root.get("brithday"), "%" + studentsDTO.getBrithday() + "%"));
            }
            /**
             * 通过性别查询
             */
            if(StringUtils.isNotBlank(studentsDTO.getGender())){
                list.add(cb.equal(root.get("gender"),studentsDTO.getGender()));
            }
            /**
             * 通过老师查询
             */
            if(StringUtils.isNotBlank(studentsDTO.getTeName())){
                query.distinct(true);
                list.add(cb.like(root.join("teachers").get("teaName"),"%" + studentsDTO.getTeName() + "%"));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return cb.and(query.where(list.toArray(predicates)).getRestriction());
        };
    }
    /**
     * 后端：Students转StudentsReturnDTO
     *
     * @param
     * @return
     */
    @Override
    public StudentsReturnDTO studentsToStudentsReturnDTO(Students students) {
        StudentsReturnDTO studentsReturnDTO=new StudentsReturnDTO();
        studentsReturnDTO.setBrithday(students.getBrithday());
        studentsReturnDTO.setGender(students.getGender());
        studentsReturnDTO.setSid(students.getId());
        studentsReturnDTO.setStName(students.getStName());
        if(students.getTeachers()!=null){
            List<String> teNameList=new ArrayList<>();
            for (Teachers teacher:students.getTeachers()
                    ) {
                teNameList.add(teacher.getTeaName());
            }
            studentsReturnDTO.setTeName(teNameList);
        }else{
            studentsReturnDTO.setTeName(null);
        }
        return studentsReturnDTO;
    }

}

```

### 7 在 web.rest 包下建 StudentsResource 类，实现 rest api 接口

---

```
//将类创建为spring bean,该注解包含了@Controller
//@ResponseBody两个注解的功能。ResponseBody用于将返回的对了转换成json
@RestController
//添加访问路径
@RequestMapping(value = "/api/DemoForManyToMany")
public class StudentsResource extends BaseResource<StudentsDTO> {

    @Autowired
    StudentsService studentsService;
    /**
     * 前端接口：添加学生，级联保存老师关系
     * @param studentsDTO
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseDTO addStudents(@RequestBody StudentsDTO studentsDTO){
        if(StringUtils.isEmpty(studentsDTO.getGender())){
            return getResponseDTO("201", "学生性别不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getStName())){
            return getResponseDTO("201", "学生姓名不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getBrithday())){
            return getResponseDTO("201", "学生生日不能为空", null);
        }

        try{
            studentsService.add(studentsDTO);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }
    /**
     * 前端接口：新增学生和老师，并级联保存老师学生关系
     * @param studentsDTO
     * @return
     */
    @RequestMapping(value = "/newTeacherAndStudent", method = RequestMethod.POST)
    public ResponseDTO addStudentsAndTeacher(@RequestBody StudentsDTO studentsDTO){
        if(StringUtils.isEmpty(studentsDTO.getTeName())){
            return getResponseDTO("201", "老师姓名不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getGender())){
            return getResponseDTO("201", "学生性别不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getStName())){
            return getResponseDTO("201", "学生姓名不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getBrithday())){
            return getResponseDTO("201", "学生生日不能为空", null);
        }

        try{
            studentsService.addStudentsAndTeacher(studentsDTO);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }
    /**
     * 前端接口：级联删除学生和学生老师关系
     * @param studentsDTO
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO deleteStudents(@RequestBody StudentsDTO studentsDTO){
        if(studentsDTO.getSid()==null){
            return getResponseDTO("201", "学生ID不能为空", null);
        }
        try{
            studentsService.delete(studentsDTO);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }
    /**
     * 前端接口：修改学生信息和学生老师关系
     * @param studentsDTO
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResponseDTO modifyStudents(@RequestBody StudentsDTO studentsDTO){
        if(studentsDTO.getSid()==null){
            return getResponseDTO("201", "学生ID不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getGender())){
            return getResponseDTO("201", "学生性别不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getStName())){
            return getResponseDTO("201", "学生姓名不能为空", null);
        }
        if(StringUtils.isEmpty(studentsDTO.getBrithday())){
            return getResponseDTO("201", "学生生日不能为空", null);
        }
        try{
            studentsService.modify(studentsDTO);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统错误", null);
        }
        return getResponseDTO("200", "操作成功", null);
    }
    /**
     * 前端接口：查询学生信息
     *
     * @param studentsDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO<List<Map<String, Object>>> list(@RequestBody StudentsDTO studentsDTO) throws Exception {
        return success(studentsService.list(studentsDTO));
    }
    /**
     * 前端接口：查询学生信息(分页)
     *
     * @param studentsDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO<Page<Map<String, Object>>> listPage(@RequestBody StudentsDTO studentsDTO) throws Exception {
        return success(studentsService.listPage(studentsDTO));
    }
}
```

接口代码完成，用 postman 进行接口的测试
