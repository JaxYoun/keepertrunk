package com.troy.uaa.service.impl;

import com.troy.api.dto.RoleDTO;
import com.troy.api.dto.UserDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.util.BeanUtils;
import com.troy.uaa.config.ApplicationProperties;
import com.troy.uaa.domain.Role;
import com.troy.uaa.domain.User;
import com.troy.uaa.domain.UserRole;
import com.troy.uaa.repository.RoleRepository;
import com.troy.uaa.repository.UserRepository;
import com.troy.uaa.repository.UserRoleRepository;
import com.troy.uaa.service.TenantService;
import com.troy.uaa.service.UserService;
import com.troy.uaa.service.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yjm on 2017/4/9.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private RedisService redisService;

    private final Logger log = LoggerFactory.getLogger(UserService.class);


    /**
     * 保存用户
     * @param userDTO
     */
    @Override
    public User saveUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        user.setLogin(userDTO.getLoginName());
        //默认密码
        String initPassword = applicationProperties.getAccount().getInitPassword();
        if (StringUtils.isEmpty(initPassword)){
            initPassword = "111111";
        }
        user.setPassword(passwordEncoder.encode(initPassword));
        user.setUserType("uaa.user.user_type");
        user.setUserTypeValue("0");//本地
        user = userRepository.save(user);
        //角色用户
        updateUserRole(userDTO,user);
        return user;
    }

    /**
     * 更新用户
     */
    @Override
    public User updateUser(UserDTO userDTO) throws Exception{
        User user = userRepository.findOne(userDTO.getId());
        BeanUtils.copyProperties(userDTO,user);
        user = userRepository.save(user);
        if (userDTO.isUpdateRole()){//更新角色
            userRoleRepository.deleteByUserId(userDTO.getId());
            updateUserRole(userDTO,user);
        }
        return user;
    }

    /**
     * 查询用户
     * @param pageable
     * @param userDTO
     * @return
     */
    @Override
    public Page<UserDTO> queryUsers(Pageable pageable, UserDTO userDTO) {
        Page<User> pageUser = userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(userDTO.getValue())){
                    predicateList.add(criteriaBuilder.equal(root.get("userTypeValue"),userDTO.getValue()));
                }
                if (userDTO.getTenantId() != null){
                    predicateList.add(criteriaBuilder.equal(root.get("tenantId"),userDTO.getTenantId()));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        },pageable);
        ResponseDTO<Map<Long,String>> responseDTO = tenantService.getTenantNameIds();
        Map<Long, String> map = responseDTO.getData();
        Page<UserDTO> pageDTO = pageUser.map(new Converter<User, UserDTO>() {
            @Override
            public UserDTO convert(User user) {
                UserDTO userDto = userMapper.userToDto(user);
                userDto.setTenantName(map.get(userDto.getTenantId()));
                if (userDto.getType() != null && userDto.getValue() != null) {
                    userDto.setType(redisService.get(userDto.getType() + "." + userDto.getValue()));
                }else{
                    userDto.setType("");
                }
                if (user.getUserRoleList() != null && !user.getUserRoleList().isEmpty()){
                    StringBuffer sb = new StringBuffer();
                    for (UserRole userRole : user.getUserRoleList()) {
                        if (userRole.getRole() != null){
                            sb.append(userRole.getRole().getRoleName()+",");
                        }
                    }
                    String sbString = sb.toString();
                    userDto.setRoleName(sbString.length()>0?
                            sbString.substring(0,sbString.length()-1):"");
                }
                return userDto;
            }
        });
        return pageDTO;
    }

    /**
     * 删除用户
     * @param ids 用户id集合
     */
    @Override
    public void deleteUser(List<Long> ids) {
        //删除用户角色中间表
        userRoleRepository.deleteByUserIds(ids);
        userRepository.deleteByIds(ids);
    }

    /**
     * 查询所有角色
     */
    @Override
    public List<RoleDTO> queryRoles(Boolean falg) {
        List<Role> roleList = null;
        if (falg == true){//获取当前用户角色
            String userName = SecurityUtils.getCurrentUserLogin();
            System.out.println(userName);
            if (StringUtils.isEmpty(userName)){
                return null;
            }else{
                roleList = roleRepository.findByUser(userName);
            }
        }else{
            roleList = (List<Role>) roleRepository.findAll();
        }
        //转换dto
        if (roleList != null && !roleList.isEmpty()){
            List<RoleDTO> roleDTOList = new ArrayList<>();
            for (Role role : roleList) {
                RoleDTO roleDto = new RoleDTO();
                roleDto.setId(role.getId());
                //名字
                roleDto.setRoleName(role.getRoleName());
                roleDto.setRoleCode(role.getRoleCode());
                roleDTOList.add(roleDto);
            }
            return roleDTOList;
        }
        return null;
    }

    /**
     * 查询用户
     * @param id
     * @return
     */
    @Override
    public UserDTO queryUsers(Long id) {
        User user = userRepository.findOne(id);
        UserDTO userDTO = userMapper.userToDto(user);
        //角色ID
        if (user != null && user.getUserRoleList() != null && !user.getUserRoleList().isEmpty()){
            List<Long> roleIds = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            for (UserRole userRole : user.getUserRoleList()) {
                roleIds.add(userRole.getRole().getId());
                sb.append(userRole.getRole().getRoleName()+",");
            }
            String sbString = sb.toString();
            userDTO.setRoleName(sbString.length()>0?
                    sbString.substring(0,sbString.length()-1):"");
            userDTO.setRoles(roleIds);
        }
        return userDTO;
    }

    /**
     * 修改密码
     * @param oldPass
     * @param newPass
     * @return
     */
    @Override
    public String updatePass(String oldPass, String newPass,Long id) {
        //对比旧密码
        String password = userRepository.findPassWord(id);
        if (!StringUtils.isNotBlank(password) || passwordEncoder.matches(oldPass,password)){//可以修改
            userRepository.updatePassword(passwordEncoder.encode(newPass),id);
        }else{
            return "正在使用密码输入错误";
        }
        return null;
    }

    /**
     * 重置密码
     * @param id 用户ID
     */
    @Override
    public void resetPass(Long id) {
        String initPassword = applicationProperties.getAccount().getInitPassword();
        if (StringUtils.isEmpty(initPassword)){
            initPassword = "111111";
        }
        userRepository.updatePassword(passwordEncoder.encode(initPassword),id);
    }


    /**
     * 根据租户ID删除用户
     * @param id
     */
    @Override
    public List<Long> deleteUserByTenantId(Long id) {
        List<Long> userIds = userRepository.findIdByTenantId(id);
        //删除用户角色表
        userRoleRepository.deleteByTenantIds(id);
        //删除用户
        userRepository.deleteByTenantId(id);
        return userIds;
    }


    /**
     * 添加用户角色中间信息
     * @param userDTO
     * @param user
     */
    private void updateUserRole(UserDTO userDTO,User user){
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()){
            List<UserRole> userRoleList = new ArrayList<>();
            for (Long aLong : userDTO.getRoles()) {
                Role role = roleRepository.findOne(aLong);
                if (role == null)continue;
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);
                userRoleList.add(userRole);
            }
            userRoleRepository.save(userRoleList);
        }
    }

    /**
     * 修改密码
     * @param password
     */
    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            log.debug("Changed password for User: {}", user);
        });
    }

//    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
    }
}
