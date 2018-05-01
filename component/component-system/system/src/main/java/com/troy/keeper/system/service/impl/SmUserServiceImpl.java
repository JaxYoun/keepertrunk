package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.core.utils.MapperParam;
import com.troy.keeper.core.utils.MapperUtils;
import com.troy.keeper.core.utils.string.StringUtils;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.intercept.AdviceIntercept;
import com.troy.keeper.system.intercept.account.AccountService;
import com.troy.keeper.system.repository.SmOrgRepository;
import com.troy.keeper.system.repository.SmPostUserRepository;
import com.troy.keeper.system.repository.SmUserRepository;
import com.troy.keeper.system.service.SmUserService;
import com.troy.keeper.system.util.PostUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/5/27.
 */
@Service
@Transactional
public class SmUserServiceImpl extends BaseServiceImpl<SmUser, SmUserDTO> implements SmUserService {
       @Autowired
    private SmUserRepository smUserRepository;

    @Autowired
    private SmOrgRepository smOrgRepository;

    @Autowired
    private SmPostUserRepository smPostUserRepository;

    @Autowired
    private MapperUtils mapperUtils;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PostUtils postUtils;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 有效数据状态
    private static final String VALID_DATA_STATUS = "1";
    // 无效数据状态
    private static final String INVALID_DATA_STATUS = "0";
    // 新增用户默认密码
    private static final String DEFAULT_PASSWORD = "111111";
    // Redis默认Key
    private static final String KEY_NAME = "user_";
    // RedisKey标志
    private static final String UNDERLINE = "_";

    /**
     * 查询用户列表
     *
     * @param smUserDTO
     * @return
     */
    public List<Map<String, Object>> list(SmUserDTO smUserDTO,HttpServletRequest httpServletRequest) throws Exception {
        Specification<SmUser> spec = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (smUserDTO.getOrgId() != null) {
                //根据机构ID找到所以的子机构
//                List<Long> childIds = smOrgRepository.findAllChildIds("%"+smUserDTO.getOrgId()+"-%");
//                childIds.add(smUserDTO.getOrgId());
//                list.add(cb.equal(root.get("orgId").as(Long.class), smUserDTO.getOrgId()));
                List<Long> childIds = smOrgRepository
                        .findAllOrgIdsById(smUserDTO.getOrgId()+"-%","%-"+smUserDTO.getOrgId()+"-%",smUserDTO.getOrgId());
                list.add(root.get("orgId").as(Long.class).in(childIds));
            }
            if (postUtils.getOrgLimit()){
                list.add(root.get("orgId").as(Long.class).in(postUtils.getOrgIds(httpServletRequest)));
            }
            if (smUserDTO.getUserName() != null) {
                list.add(cb.like(root.get("userName").as(String.class), "%" + smUserDTO.getUserName() + "%"));
            }
            if (smUserDTO.getLoginName() != null) {
                list.add(cb.like(root.get("loginName").as(String.class), "%" + smUserDTO.getLoginName() + "%"));
            }
            if (!StringUtils.isEmpty(smUserDTO.getMobilePhone())) {
                list.add(cb.like(root.get("mobilePhone").as(String.class), "%" + smUserDTO.getMobilePhone() + "%"));
            }
            if (smUserDTO.getStatus() != null) {
                list.add(cb.equal(root.get("status").as(String.class), smUserDTO.getStatus()));
            }
            if (smUserDTO.getActivated() != null) {
                if (smUserDTO.getActivated() == 0) {
                    list.add(cb.equal(root.get("activated").as(Integer.class), 0));
                }
                if (smUserDTO.getActivated() == 1) {
                    list.add(cb.equal(root.get("activated").as(Integer.class), 1));
                }
            }
            list.add(cb.equal(root.get("status").as(Integer.class), 1));
            Predicate[] p = new Predicate[list.size()];
            query.orderBy(cb.desc(root.get("userName")));
            return cb.and(list.toArray(p));
        };
        // 转换List实体为Map对象
        List<SmUser> list = smUserRepository.findAll(spec);
        MapperParam mapperParam = new MapperParam();
        List<String> ignoreColumnList = new ArrayList<>();
        // 过滤字段及关联关系
        ignoreColumnList.add("smPostUserList");
        ignoreColumnList.add("password");
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        mapperParam.setList(list);
        List<Map<String, Object>> data = mapperUtils.convertList(mapperParam);
        // 补全password的键值对
        Map<Long,String> userIdOrgMap =  makeUserIdOrgName() ;
        for (int i = 0; i < data.size(); i++) {
            data.get(i).put("password", null);
            //根据ID找到用户对应的机构名称
            if (userIdOrgMap.containsKey(Long.valueOf(data.get(i).get("id").toString()))){
                data.get(i).put("orgName",
                        userIdOrgMap.get(Long.valueOf(data.get(i).get("id").toString())));
            }else{
                data.get(i).put("orgName",null);
            }
        }
        return data;
    }

    /**
     *  生成 key:userId, value :orgName
     * @return
     */
    private Map<Long,String> makeUserIdOrgName(){
        List<Object[]> objects = smPostUserRepository.findAllUserOrg();
        Map<Long,String> map = new HashMap<>();
        for (Object[] object : objects) {
            map.put(Long.valueOf(object[0].toString()),object[1].toString());
        }
        return map;
    }

    /**
     * 查询用户列表（分页）
     *
     * @param smUserDTO
     * @return
     */
    public Page<Map<String, Object>> listForPage(SmUserDTO smUserDTO) throws Exception {
        PageRequest pageRequest = new PageRequest(smUserDTO.getPage(), smUserDTO.getSize());
        Specification<SmUser> spec = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (smUserDTO.getOrgId() != null) {
                list.add(cb.equal(root.get("orgId").as(Long.class), smUserDTO.getOrgId()));
            }
            if (smUserDTO.getLoginName() != null) {
                list.add(cb.like(root.get("loginName").as(String.class), "%" + smUserDTO.getLoginName() + "%"));
            }
            if (smUserDTO.getUserName() != null) {
                list.add(cb.like(root.get("userName").as(String.class), "%" + smUserDTO.getUserName() + "%"));
            }
            if (!StringUtils.isEmpty(smUserDTO.getMobilePhone())) {
                list.add(cb.like(root.get("mobilePhone").as(String.class), "%" + smUserDTO.getMobilePhone() + "%"));
            }
            if (smUserDTO.getStatus() != null) {
                list.add(cb.equal(root.get("status").as(String.class), smUserDTO.getStatus()));
            }
            if (smUserDTO.getActivated() != null) {
                if (smUserDTO.getActivated() == 0) {
                    list.add(cb.equal(root.get("activated").as(Integer.class), 0));
                }
                if (smUserDTO.getActivated() == 1) {
                    list.add(cb.equal(root.get("activated").as(Integer.class), 1));
                }
            }
            list.add(cb.equal(root.get("status").as(Integer.class), 1));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        Page<SmUser> orglist = smUserRepository.findAll(spec, pageRequest);
        // 将Page实体转换为Page<Map<String,Object>>
        MapperParam mapperParam = new MapperParam();
        List<String> ignoreColumnList = new ArrayList<>();
        // 过滤字段及关联关系
        ignoreColumnList.add("smPostUserList");
        ignoreColumnList.add("password");
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        mapperParam.setPage(orglist);
        Page<Map<String, Object>> pageData = mapperUtils.convertPage(mapperParam);
        for (int i = 0; i < pageData.getContent().size(); i++) {
            pageData.getContent().get(i).put("password", null);
        }
        return pageData;
    }

    /**
     * 新增用户数据
     *
     * @param smUserDTO
     */
    @AdviceIntercept(AccountService.class)
    public void createData(SmUserDTO smUserDTO) {
        SmUser smUser = new SmUser();
        smUser.setOrgId(smUserDTO.getOrgId());
        smUser.setUserName(smUserDTO.getUserName());
        smUser.setType(smUserDTO.getType());
        smUser.setLoginName(smUserDTO.getLoginName());
        smUser.setEmail(smUserDTO.getEmail());
        //设置默认密码
        smUser.setPassword(getDefaultPassword());
        smUser.setStatus(VALID_DATA_STATUS);
        if (!("").equals(smUserDTO.getMobilePhone()) || !("null").equals(smUserDTO.getMobilePhone())) {
            smUser.setMobilePhone(smUserDTO.getMobilePhone());
        }
        smUser.setActivated(true);
        smUser.setLogin(smUserDTO.getLoginName());
        smUserRepository.save(smUser);
        putRedisByKey(smUser.getId(), smUser.getUserName());
    }

    /**
     * 重置用户为默认密码
     *
     * @param smUserDTO
     */
    public void resetPassword(SmUserDTO smUserDTO) {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        smUser.setPassword(getDefaultPassword());
        smUserRepository.save(smUser);
    }

    /**
     * 更新用户数据
     *
     * @param smUserDTO
     */
    public void updateData(SmUserDTO smUserDTO) {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        smUser.setOrgId(smUserDTO.getOrgId());
        smUser.setUserName(smUserDTO.getUserName());
        smUser.setType(smUserDTO.getType());
        smUser.setLoginName(smUserDTO.getLoginName());
        if (!("").equals(smUserDTO.getMobilePhone()) || !("null").equals(smUserDTO.getMobilePhone())) {
            smUser.setMobilePhone(smUserDTO.getMobilePhone());
        }
        smUser.setEmail(smUserDTO.getEmail());
        smUser.setLogin(smUserDTO.getLoginName());
        smUserRepository.save(smUser);
        putRedisByKey(smUser.getId(), smUser.getUserName());
    }

    /**
     * 检查登录名是否重复
     *
     * @param smUserDTO
     * @return
     */
    public List<SmUser> checkLoginName(SmUserDTO smUserDTO) {
        List<SmUser> list = smUserRepository.checkLoginName(smUserDTO.getLoginName());
        return list;
    }

    /**
     * 检查邮箱是否存在重复
     *
     * @param smUserDTO
     * @return
     */
    public List<SmUser> checkEmail(SmUserDTO smUserDTO) {
        List<SmUser> list = smUserRepository.checkEmail(smUserDTO.getEmail());
        return list;
    }

    /**
     * 根据用户ID返回用户数据实体
     *
     * @param smUserDTO
     * @return
     */
    public Map<String, Object> get(SmUserDTO smUserDTO) throws Exception {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        // 将实体转换为Map<String,Object>
        MapperParam mapperParam = new MapperParam();
        List<String> ignoreColumnList = new ArrayList<>();
        // 过滤字段及关联关系
        ignoreColumnList.add("smPostUserList");
        ignoreColumnList.add("password");
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        mapperParam.setObj(smUser);
        Map<String, Object> obj = mapperUtils.convert(mapperParam);
        obj.put("password", null);
        return obj;
    }

    /**
     * 查询机构用户树
     *
     * @return
     */
    public List<Map<String, Object>> queryOrgUserTree(HttpServletRequest httpServletRequest) {
        List<Map<String, Object>> list = new ArrayList<>();
        // 查询机构数据
//        Specification<SmOrg> spec = (root, query, cb) -> query.where(cb.equal(root.get("status").as(Integer.class), 1)).getRestriction();
        List<SmOrg> smOrgs = smOrgRepository.findAll(new Specification<SmOrg>() {
            @Override
            public Predicate toPredicate(Root<SmOrg> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class), 1));
                if (postUtils.getOrgLimit()){
                    predicateList.add(root.get("id").in(postUtils.getOrgIds(httpServletRequest)));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        });
        // 组装机构数据
        for (int i = 0; i < smOrgs.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            StringBuffer sb1 = new StringBuffer(smOrgs.get(i).getId().toString()).insert(0, "org-");
            if (smOrgs.get(i).getpId() != null) {
                StringBuffer sb2 = new StringBuffer(smOrgs.get(i).getpId().toString()).insert(0, "org-");
                map.put("pid", sb2.toString());
            } else {
                map.put("pid", null);
            }
            map.put("id", sb1.toString());
            map.put("nodeType", 1);
            map.put("name", smOrgs.get(i).getOrgName());
            list.add(map);
        }
        // 查询用户数据
        Specification<SmUser> spec2 = new Specification<SmUser>() {
            @Override
            public Predicate toPredicate(Root<SmUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(cb.equal(root.get("status").as(Integer.class), 1));
                predicateList.add(cb.equal(root.get("activated").as(Integer.class), 1));
                if (postUtils.getOrgLimit()){
                    predicateList.add(root.get("orgId").in(postUtils.getOrgIds(httpServletRequest)));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return cb.and(predicateList.toArray(predicates));
            }
        };
        List<SmUser> smUsers = smUserRepository.findAll(spec2);
        // 组装用户数据
        for (int j = 0; j < smUsers.size(); j++) {
            Map<String, Object> map = new HashMap<>();
            StringBuffer sb1 = new StringBuffer(smUsers.get(j).getId().toString()).insert(0, "user-");
            StringBuffer sb2 = new StringBuffer(smUsers.get(j).getOrgId().toString()).insert(0, "org-");
            map.put("id", sb1.toString());
            map.put("pid", sb2.toString());
            map.put("nodeType", 2);
            map.put("name", smUsers.get(j).getUserName());
            list.add(map);
        }
        return list;
    }

    /**
     * 删除指定用户（逻辑删除）
     *
     * @param smUserDTO
     */
    public void del(SmUserDTO smUserDTO) {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        smUser.setStatus(INVALID_DATA_STATUS);
        smUser.setActivated(false);
        smUserRepository.save(smUser);
        // 删除用户，物理删除岗位用户关联表数据
        smPostUserRepository.deletePostUserByUserId(smUserDTO.getId());
        removeRedisByKey(smUser.getId());
    }

    /**
     * 停用指定用户
     * 2017年9月7日18:01:14
     *
     * @param smUserDTO
     */
    public void inactivate(SmUserDTO smUserDTO) {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        smUser.setActivated(false);
        smUserRepository.save(smUser);
        removeRedisByKey(smUser.getId());
    }

    /**
     * 激活指定用户
     * 2017年9月7日18:01:18
     *
     * @param smUserDTO
     */
    public void activate(SmUserDTO smUserDTO) {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        smUser.setActivated(true);
        smUserRepository.save(smUser);
    }

    /**
     * 修改用户密码接口(BCryptPasswordEncoder Password)
     *
     * @param smUserDTO
     */
    public void changeUserPassword(SmUserDTO smUserDTO) {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        String password = smUserDTO.getNewPassword();
        // Md5Password
        smUser.setPassword(getPasswordToBCryptPasswordEncoder(password));
        smUserRepository.save(smUser);
    }

    /**
     * 判断用户ID是否存在
     *
     * @param smUserDTO
     * @return
     */
    public int ifUserId(SmUserDTO smUserDTO) {
        return smUserRepository.ifUserId(smUserDTO.getId());
    }

    /**
     * 检查用户老密码是否正确（）
     *
     * @param smUserDTO
     * @return
     */
    public int checkUserOldPassword(SmUserDTO smUserDTO) {
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        // 取得老密码
        String password = smUserDTO.getOldPassword();
        // 若原始密码有值则验证传入密码和数据库密码比对验证
        if (passwordEncoder.matches(password, smUser.getPassword())) {
            // 密码正确
            return 0;
        } else {
            // 密码错误
            return 1;
        }
    }

    /**
     * 获取用户ID和用户名称
     *
     * @return
     */
    public List<SmUser> getUserIdAndUserName() {
        return smUserRepository.getUserIdAndUserName();
    }

    /**
     * 获取MD5加密后的默认密码(OLD)
     * 获取BCryptPasswordEncoder加密后的默认密码
     *
     * @return
     */
    private String getDefaultPassword() {
        return passwordEncoder.encode(DEFAULT_PASSWORD);
    }

    /**
     * 获取BCryptPasswordEncoder加密后的密码
     *
     * @param password
     * @return
     */
    private String getPasswordToBCryptPasswordEncoder(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 获取RedisUserKey
     *
     * @param userId
     * @return
     */
    private static String getRedisUserKey(Long userId) {
        StringBuilder stringBuilder = new StringBuilder(KEY_NAME);
        stringBuilder.append(userId);
        return stringBuilder.toString();
    }

    /**
     * 以UserKey删除Redis缓存数据
     *
     * @param userId
     */
    private void removeRedisByKey(Long userId) {
        redisService.removeByKey(getRedisUserKey(userId));
    }

    /**
     * 插入Redis数据
     *
     * @param userId
     * @param userName
     */
    private void putRedisByKey(Long userId, String userName) {
        redisService.set(getRedisUserKey(userId), userName);
    }

}
