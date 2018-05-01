package com.troy.uaa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.troy.api.dto.MenuDTO;
import com.troy.api.dto.TenantDTO;
import com.troy.api.dto.UserDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.uaa.config.Constants;
import com.troy.uaa.domain.Role;
import com.troy.uaa.domain.User;
import com.troy.uaa.repository.RoleRepository;
import com.troy.uaa.repository.UserRepository;
import com.troy.uaa.service.SystemService;
import com.troy.uaa.service.UserService;
import com.troy.uaa.service.mapper.RoleMapper;
import com.troy.uaa.web.rest.vm.ManagedUserVM;
import com.troy.uaa.web.rest.vm.UpdatePasswordVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import com.troy.uaa.service.MailService;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource extends BaseResource {


    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SystemService systemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * 保存用户
     */
    @RequestMapping(value = "/user/saveUser",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO saveUser(@RequestBody UserDTO userDTO){
        try{
            if (userDTO.getId() != null || StringUtils.isEmpty(userDTO.getLoginName())){
                return getResponseDTO("202", "参数错误", null);
            }
            if (userDTO.getMobile() != null && userDTO.getMobile().length()>11){
                return getResponseDTO("202", "字段超长", null);
            }
            //验证登陆名
            User user = userRepository.findUserByLogin(userDTO.getLoginName());
            if (user != null){
                return getResponseDTO("203", "登陆名已存在", null);
            }
            user = userService.saveUser(userDTO);
            //保存用户名到redis
            set("user_"+user.getId(), user.getUserName());
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }

    /**
     * 更新用户
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/user/updateUser",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO updateUser(@RequestBody UserDTO userDTO){
        try{
            if (userDTO.getId() == null){
                return getResponseDTO("202", "参数错误", null);
            }
            if (userDTO.getMobile() != null && userDTO.getMobile().length()>11){
                return getResponseDTO("202", "字段超长", null);
            }
            User user = userService.updateUser(userDTO);
            set("user_"+user.getId(), user.getUserName());
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }

    /**
     * 查询用户列表接口
     * @param pageable 分页对象
     * @return
     */
    @RequestMapping(value = "/user/queryUsers",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<Page<TenantDTO>> queryUsers(@PageableDefault(value = 20,sort = {"userName"},direction = Sort.Direction.ASC)
                                                   Pageable pageable){
        try{
            Page<UserDTO> userDTOPage = userService.queryUsers(pageable,new UserDTO());
            return getResponseDTO("200", "操作成功", userDTOPage);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/deleteUser")
    @ResponseBody
    public ResponseDTO deleteUser(Long id){
        try{
            if (id == null){
                return getResponseDTO("202", "参数错误", null);
            }
            List<Long> ids = new ArrayList<>();
            ids.add(id);
            userService.deleteUser(ids);
            //删除redis
            delete("user_"+id);
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }


    /**
     * 查询角色
     * @return
     */
    @RequestMapping(value = "/user/queryRoles")
    @ResponseBody
    public ResponseDTO<List> queryRoles(){
        try{
            List list = userService.queryRoles(false);
            return getResponseDTO("200", "操作成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }

    /**
     * 查询用户
     * @return
     */
    @RequestMapping(value = "/user/queryUser")
    @ResponseBody
    public ResponseDTO<UserDTO> queryUser(Long id){
        try{
            if (id == null){
                return getResponseDTO("202", "参数错误", null);
            }
            UserDTO userDTO = userService.queryUsers(id);
            return getResponseDTO("200", "操作成功", userDTO);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }


    /**
     * 修改密码
     * @return
     */
    @RequestMapping(value = "/user/updatePass")
    @ResponseBody
    public ResponseDTO updatePass(String oldPass, String newPass,HttpServletRequest request){
        try{
            String userIdStr = request.getHeader("userId");
            if (StringUtils.isEmpty(userIdStr)){
                return getResponseDTO("202", "参数错误：用户id不存在", null);
            }
            Long userId = Long.valueOf(userIdStr);
            if (!StringUtils.isNotBlank(newPass) || newPass.equals("null")){
                return getResponseDTO("202", "参数错误", null);
            }
            String message = userService.updatePass(oldPass,newPass,userId);
            if (message == null) {
                return getResponseDTO("200", "操作成功", null);
            }else{
                return getResponseDTO("201", message, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }


    /**
     * 重置密码
     * @return
     */
    @RequestMapping(value = "/user/resetPass")
    @ResponseBody
    public ResponseDTO resetPass(Long id){
        try{
            if (id == null){
                return getResponseDTO("202", "参数错误", null);
            }
            userService.resetPass(id);
            return getResponseDTO("200", "操作成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }

    /**
     * 根据租户ID删除用户
     * 删除租户的时候调用
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/deleteUserByTenantId/{id}")
    @ResponseBody
    public ResponseDTO deleteUserByTenantId(@PathVariable("id") Long id){
        try{
            if (id == null){
                return getResponseDTO("202", "参数错误", null);
            }
//            System.out.println("uaa调用成功："+id);
            List<Long> userIds = userService.deleteUserByTenantId(id);
            //删除reids关于用户的信息
            if (userIds != null && !userIds.isEmpty()){
                for (Long userId : userIds) {
                    delete("user_"+userId);
                }
            }
            return getResponseDTO("200", "操作成功", id);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }

    @PostMapping(path = "/account/change_password",
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity changePassword(@RequestBody UpdatePasswordVM updatePasswordVM) {
        User user = userRepository.findUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!checkPasswordLength(updatePasswordVM.getNewPassword()) || user == null) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
//        userService.changePassword(password);
        String msg = userService.updatePass
            (updatePasswordVM.getOldPassword(),updatePasswordVM.getNewPassword(),user.getId());
        if (msg!= null){
            return new ResponseEntity<>("old password error", HttpStatus.METHOD_NOT_ALLOWED);
        }else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * 查询当前用户角色
     * @return
     */
    @RequestMapping(value = "/user/queryUserRoles")
    @ResponseBody
    public ResponseDTO<List> queryUserRoles(){
        try{
            List list = userService.queryRoles(true);
            return getResponseDTO("200", "操作成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }
    /**
     * 查询当前用户角色
     * @return
     */
    @RequestMapping(value = "/user/setRoleId")
    @ResponseBody
    public ResponseDTO setRoleId(HttpServletRequest request,Long id){
        try{
            if (id == null){
                return getResponseDTO("202", "参数错误", null);
            }
            //获取用户的租户
            Long account = SecurityUtils.getCurrentUserId();
            Long tenantId =null;
            if (account != null){
                tenantId = userRepository.findTenantId(account);
            }
            //获取出菜单,存放在redis中
            tenantId = tenantId == null?-1l:tenantId;
            ResponseDTO<List<MenuDTO>> responseDTO = systemService.queryMenu(id,tenantId);
            List<MenuDTO> menuDTOList = responseDTO.getData();
            if (menuDTOList != null && !menuDTOList.isEmpty()){
                String menuJson =  mapper.writeValueAsString(menuDTOList);
                set("menu_"+id, menuJson);
            }else{
                delete("menu_"+id);
            }
            Role role = roleRepository.findOne(id);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SELECT_ROLE,roleMapper.roleToDto(role));
            return getResponseDTO("200", "操作成功", get("menu_"+id));
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误",null);
        }
    }

    @RequestMapping(value = "/user/test")
    ResponseDTO<String> test(@RequestBody String s){
        return getResponseDTO("200","www",s);
    }

    public void delete(String key) {
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.del(serializer.serialize(key));
                return null;
            }
        }, true);
    }

    public String get(final String key) {
        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

    public boolean set(final String key, final String value) {
        boolean result = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    //验证密码长度
    private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    //更新用户到redis
    public void refreshUser(){
        List<User> userList = (List<User>) userRepository.findAll();
        if (userList != null && !userList.isEmpty()){
            for (User user : userList) {
                set("user_"+user.getId(),user.getUserName());
            }
        }
    }

//    private final Logger log = LoggerFactory.getLogger(UserResource.class);
//
//    private static final String ENTITY_NAME = "userManagement";
//
//    private final UserRepository userRepository;
//
//    //    private final MailService mailService;
////
//    private final UserService userService;
//
//    //    public UserResource(UserRepository userRepository, MailService mailService,
////            UserService userService) {
//    public UserResource(UserRepository userRepository,
//                        UserService userService) {
//
//        this.userRepository = userRepository;
////        this.mailService = mailService;
//        this.userService = userService;
//    }
//
//    /**
//     * POST  /users  : Creates a new user.
//     * <p>
//     * Creates a new user if the login and email are not already used, and sends an
//     * mail with an activation link.
//     * The user needs to be activated on creation.
//     * </p>
//     *
//     * @param managedUserVM the user to create
//     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PostMapping("/users")
//    @Timed
//    @Secured(AuthoritiesConstants.ADMIN)
//    public ResponseEntity createUser(@RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
//        log.debug("REST request to save User : {}", managedUserVM);
//
//        if (managedUserVM.getId() != null) {
//            return ResponseEntity.badRequest()
//                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
//                .body(null);
//            // Lowercase the user login before comparing with database
//        } else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
//            return ResponseEntity.badRequest()
//                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
//                .body(null);
//        } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest()
//                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
//                .body(null);
//        } else {
//            User newUser = userService.createUser(managedUserVM);
////            mailService.sendCreationEmail(newUser);
//            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
//                .headers(HeaderUtil.createAlert("A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
//                .body(newUser);
//        }
//    }
//
//    /**
//     * PUT  /users : Updates an existing User.
//     *
//     * @param managedUserVM the user to update
//     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
//     * or with status 400 (Bad Request) if the login or email is already in use,
//     * or with status 500 (Internal Server Error) if the user couldn't be updated
//     */
//    @PutMapping("/users")
//    @Timed
//    @Secured(AuthoritiesConstants.ADMIN)
//    public ResponseEntity<UserDTO> updateUser(@RequestBody ManagedUserVM managedUserVM) {
//        log.debug("REST request to update User : {}", managedUserVM);
//        Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "E-mail already in use")).body(null);
//        }
//        existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
//        }
//        Optional<UserDTO> updatedUser = userService.updateUser(managedUserVM);
//
//        return ResponseUtil.wrapOrNotFound(updatedUser,
//            HeaderUtil.createAlert("A user is updated with identifier " + managedUserVM.getLogin(), managedUserVM.getLogin()));
//    }
//
//    /**
//     * GET  /users : get all users.
//     *
//     * @param pageable the pagination information
//     * @return the ResponseEntity with status 200 (OK) and with body all users
//     */
//    @GetMapping("/users")
//    @Timed
//    public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable) {
//        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }
//
//    /**
//     * GET  /users/:login : get the "login" user.
//     *
//     * @param login the login of the user to find
//     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
//     */
//    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
//    @Timed
//    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
//        log.debug("REST request to get User : {}", login);
//        return ResponseUtil.wrapOrNotFound(
//            userService.getUserWithAuthoritiesByLogin(login)
//                .map(UserDTO::new));
//    }
//
//    /**
//     * DELETE /users/:login : delete the "login" User.
//     *
//     * @param login the login of the user to delete
//     * @return the ResponseEntity with status 200 (OK)
//     */
//    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
//    @Timed
//    @Secured(AuthoritiesConstants.ADMIN)
//    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
//        log.debug("REST request to delete User: {}", login);
//        userService.deleteUser(login);
//        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is deleted with identifier " + login, login)).build();
//    }
//
//    @RequestMapping("/create")
//    public void create() {
//        User user = userService.createUser
//            ("johndoe", "123456", "John", "Doe", "john.doe@localhost", "http://placehold.it/50x50", "en-US");
//    }
}
