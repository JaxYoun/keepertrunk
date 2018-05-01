package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.utils.MapperParam;
import com.troy.keeper.core.utils.MapperUtils;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.*;
import com.troy.keeper.system.dto.SmPostDTO;
import com.troy.keeper.system.repository.*;
import com.troy.keeper.system.service.SmPostService;
import com.troy.keeper.system.util.PostUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/5.
 */
@Service
@Transactional
//@CacheConfig(cacheNames = "post")
public class SmPostServiceImpl extends BaseServiceImpl<SmPost, SmPostDTO> implements SmPostService {

    @Autowired
    private SmPostRepository smPostRepository;

    @Autowired
    private SmOrgRepository smOrgRepository;

    @Autowired
    private SmPostUserRepository smPostUserRepository;

    @Autowired
    private SmPostRoleRepository smPostRoleRepository;

    @Autowired
    private SmRoleMenuRepository smRoleMenuRepository;

    @Autowired
    private SmUserRepository smUserRepository;

    @Autowired
    private MapperUtils mapperUtils;

    @Autowired
    private PostUtils postUtils;

    // 有效数据状态
    private static final String VALID_DATA_STATUS = "1";
    // 无效数据状态
    private static final String INVALID_DATA_STATUS = "0";

    /**
     * 查询岗位列表
     *
     * @return
     */
    public List<SmPost> queryPostList() {
        return smPostRepository.getSmPostList();
    }

    /**
     * 查询指定ID岗位信息
     *
     * @param smPostDTO
     * @return
     */
    public SmPost getPost(SmPostDTO smPostDTO) {
        return smPostRepository.getPost(smPostDTO.getId());
    }

    /**
     * 岗位机构数查询
     *
     * @return
     */
    public List<Map<String, Object>> queryPostOrgTree(HttpServletRequest httpServletRequest) {
//        Specification spec = (root, query, cb) -> query.where(cb.equal(root.get("status").as(Integer.class), VALID_DATA_STATUS)).getRestriction();

        List<Map<String, Object>> data = new ArrayList<>();
        // 查询岗位数据
        List<SmPost> postData = smPostRepository.findAll(new Specification<SmPost>() {
            @Override
            public Predicate toPredicate(Root<SmPost> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class), VALID_DATA_STATUS));
                if (postUtils.getOrgLimit()){
                    predicateList.add(root.get("orgId").in(postUtils.getOrgIds(httpServletRequest)));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        });
        // 循环读取岗位数据
        if (!postData.isEmpty()) {
            for (int i = 0; i < postData.size(); i++) {
                Map<String, Object> map = new HashedMap();
                StringBuilder id = new StringBuilder(postData.get(i).getId().toString()).insert(0, "post-");
                StringBuilder orgId = new StringBuilder(postData.get(i).getOrgId().toString()).insert(0, "org-");
                map.put("id", id.toString());
                map.put("nodeType", 2);
                map.put("name", postData.get(i).getPostName());
                map.put("pId", orgId.toString());
                data.add(map);
            }
        }
        // 查询机构数据
        List<SmOrg> orgData = smOrgRepository.findAll(new Specification<SmOrg>() {
            @Override
            public Predicate toPredicate(Root<SmOrg> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class), VALID_DATA_STATUS));
                if (postUtils.getOrgLimit()){
                    predicateList.add(root.get("id").in(postUtils.getOrgIds(httpServletRequest)));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        });
        // 循环读取机构数据
        if (!orgData.isEmpty()) {
            for (int j = 0; j < orgData.size(); j++) {
                Map<String, Object> map = new HashedMap();
                StringBuilder id = new StringBuilder(orgData.get(j).getId().toString()).insert(0, "org-");
                map.put("id", id.toString());
                // 判断父ID是否为空
                if (orgData.get(j).getpId() != null || ("").equals(orgData.get(j).getpId())) {
                    StringBuilder pId = new StringBuilder(orgData.get(j).getpId().toString()).insert(0, "org-");
                    map.put("pId", pId.toString());
                } else {
                    map.put("pId", null);
                }
                map.put("nodeType", 1);
                map.put("name", orgData.get(j).getOrgName());
                data.add(map);
            }
        }
        return data;
    }

    /**
     * 新增岗位信息
     *
     * @param smPostDTO
     */
    public void createdData(SmPostDTO smPostDTO) {
        SmPost smPost = new SmPost();
        smPost.setStatus(VALID_DATA_STATUS);
        smPost.setPostName(smPostDTO.getPostName());
        smPost.setOrgId(smPostDTO.getOrgId());
        if (smPostDTO.getPostDesc() != null) {
            smPost.setPostDesc(smPostDTO.getPostDesc());
        }
        smPost.setPostCode(smPostDTO.getPostCode());
        smPost.setSmUserId(smPostDTO.getSmUserId());
        smPostRepository.save(smPost);
    }

    /**
     * 修改岗位信息
     *
     * @param smPostDTO
     */
    public void updateData(SmPostDTO smPostDTO) {
        SmPost smPost = smPostRepository.findOne(smPostDTO.getId());
        smPost.setStatus(VALID_DATA_STATUS);
        if (smPostDTO.getPostName() != null) {
            smPost.setPostName(smPostDTO.getPostName());
        }
        if (smPostDTO.getOrgId() != null) {
            smPost.setOrgId(smPostDTO.getOrgId());
        }
        if (smPostDTO.getPostDesc() != null) {
            smPost.setPostDesc(smPostDTO.getPostDesc());
        }
//        if (smPostDTO.getSmUserId() != null){
        smPost.setSmUserId(smPostDTO.getSmUserId());
//        }
        smPost.setPostCode(smPostDTO.getPostCode());
        smPostRepository.save(smPost);
    }

    /**
     * 删除指定岗位信息
     *
     * @param smPostDTO
     */
    public void del(SmPostDTO smPostDTO) {
        SmPost smPost = smPostRepository.findOne(smPostDTO.getId());
        smPost.setId(smPostDTO.getId());
        smPost.setStatus(INVALID_DATA_STATUS);
        smPostRepository.save(smPost);
        // 逻辑删除岗位后，删除岗位与角色关联表数据
        smPostRoleRepository.deletePostRoleByPostId(smPostDTO.getId());
        // 逻辑删除岗位后，删除岗位与用户关联表数据
        smPostUserRepository.deletePostUserByPostId(smPostDTO.getId());
    }

    /**
     * 检测岗位 是否与人员关联
     *
     * @param smPostDTO
     * @return
     */
    public boolean checkPost(SmPostDTO smPostDTO) {
        Specification<SmPostUser> spec = (root, query, cb) -> query.where(cb.equal(root.get("postId").as(Long.class), smPostDTO.getId())).getRestriction();
        List<SmPostUser> smPostUserList = smPostUserRepository.findAll(spec);
        if (smPostUserList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 查询岗位对应的角色权限
     *
     * @param smPostDTO
     * @return
     */
    public List<String> queryRolesByPostId(SmPostDTO smPostDTO) {
        Specification<SmPostRole> spec = (root, query, cb) -> query.where(cb.equal(root.get("postId").as(Long.class), smPostDTO.getPostId())).getRestriction();
        List<SmPostRole> list = smPostRoleRepository.findAll(spec);
        List<String> data = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                data.add(list.get(i).getRoleId().toString());
            }
        }
        return data;
    }

    /**
     * 保存岗位对应的角色权限
     *
     * @param smPostDTO
     */
    public void saveRoleLimit(SmPostDTO smPostDTO) {
        // 先清空该岗位角色关联
        smPostRoleRepository.deletePostRoleByPostId(Long.valueOf(smPostDTO.getPostId()));
        // 保存岗位数据
        if (smPostDTO.getRoleIds() != null) {
            List<String> list = smPostDTO.getRoleIds();
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {

//                    //2017/1/4 修改批量操作的角色，不需要重复添加
//                    Long postRoleId = smPostRoleRepository.checkByPostAndRole(smPostDTO.getPostId(),Long.valueOf(list.get(i)));
//                    if (postRoleId != null){
//                        continue;
//                    }
                    SmPostRole smPostRole = new SmPostRole();
                    smPostRole.setPostId(smPostDTO.getPostId());
                    smPostRole.setRoleId(Long.valueOf(list.get(i)));
                    smPostRoleRepository.save(smPostRole);
                }
            }
        }
    }

    /**
     * 查询岗位对应角色下的所有菜单数据及权限
     *
     * @param smPostDTO
     * @return
     */
    public List<SmMenu> queryPostMenus(SmPostDTO smPostDTO) {
        return smRoleMenuRepository.queryPostMenus(smPostDTO.getPostId());
    }

    /**
     * 根据岗位ID查询用户详情
     *
     * @param postId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryPostUsersByPostId(Long postId) throws Exception {
        MapperParam<SmUser> mapperParam = new MapperParam<>();
        List<SmPostUser> smPostUserList = smPostUserRepository.queryDistinctByPostId(postId);
        List<SmUser> smUsers = new ArrayList<>();
        for (int i = 0; i < smPostUserList.size(); i++) {
            SmUser smUser = smPostUserList.get(i).getSmUser();
            smUsers.add(smUser);
        }
        mapperParam.setList(smUsers);
        List<String> ignoreColumnList = new ArrayList<>();
        ignoreColumnList.add("password");
        ignoreColumnList.add("createdBy");
        ignoreColumnList.add("createdDate");
        ignoreColumnList.add("lastUpdatedBy");
        ignoreColumnList.add("lastUpdatedDate");
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        return mapperUtils.convertList(mapperParam);
    }


    /**
     * 批量更新保存
     * @param smPostDTO
     * @return
     */
    @Override
    public String batchSaveOrUpdatePost(SmPostDTO smPostDTO) {
        List<Long> postIds = null;
        if (smPostDTO.getPostIds() != null && !smPostDTO.getPostIds().isEmpty()){
            SmPost oldPost = smPostRepository.findOne(smPostDTO.getPostIds().get(0));
            postIds = smPostRepository.findIdByCondition(oldPost.getPostName(),oldPost.getPostLevel());
        }
        //验证岗位编码是否重复
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(smPostDTO.getPostCode())) {
            Long countCode = postIds == null ? smPostRepository.countByCode(smPostDTO.getPostCode()) :
                    smPostRepository.countUpdateBatchByCode(smPostDTO.getPostCode(), postIds);
            if (countCode > 0l) {
                return Const.MSG_CODE_202_POST_CODE_EXIT_ERROR;
            }
        }

        //岗位重复验证
        Long count = postIds == null?smPostRepository.countBySave(smPostDTO.getPostName(),smPostDTO.getPostLevel()):
                smPostRepository.countByUpdate(smPostDTO.getPostName(),smPostDTO.getPostLevel(),postIds);
        if (count > 0l){
            return "该岗位已经存在";
        }

        List<SmPost> smPostList = null;
        if (postIds == null){//添加
            //根据岗位级别找出同级别的机构
            List<Long> orgIds = smOrgRepository.findIdsByLevel(smPostDTO.getPostLevel());
            if (orgIds != null && !orgIds.isEmpty()){
                smPostList = new ArrayList<>(orgIds.size());
                for (Long orgId : orgIds) {
                    SmPost smPost = new SmPost();
                    BeanUtils.copyProperties(smPostDTO,smPost);
                    smPost.setStatus("1");
                    smPost.setOrgId(orgId);
                    smPostList.add(smPost);
                }
                smPostRepository.save(smPostList);
            } else{
                return "该级别机构不存在";
            }
        }else{//更新
            smPostList = (List<SmPost>) smPostRepository.findAll(postIds);
            for (SmPost smPost : smPostList) {
                smPost.setPostDesc(smPostDTO.getPostDesc());
                smPost.setPostName(smPostDTO.getPostName());
                smPost.setSmUserId(smPostDTO.getSmUserId());
                smPost.setPostCode(smPostDTO.getPostCode());
            }
            smPostRepository.save(smPostList);
        }
        return null;
    }


    /**
     * 分页查询
     * @param smPostDTO
     * @return
     */
    @Override
    public Page<SmPostDTO> findBatchOptionPost(SmPostDTO smPostDTO, Pageable pageable,HttpServletRequest httpServletRequest) {
        List<Long> ids  = smPostRepository.findAllDistionctId();
        if (ids == null){
            ids = new ArrayList<>();
        }
        ids.add(-1l);

        Page<SmPost> smPostPage = null;
        if (postUtils.getOrgLimit()){
            smPostPage = smPostRepository.findPageByCondition(ids,postUtils.getOrgIds(httpServletRequest), pageable);
        }else {
            smPostPage = smPostRepository.findPageByCondition(ids, pageable);
        }
//            @Override
//            public Predicate toPredicate(Root<SmPost> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicateList = new ArrayList<>();
//                predicateList.add(criteriaBuilder.equal(root.get("status"),"1"));
//                predicateList.add(criteriaBuilder.isNotNull(root.get("postLevel")));
//                if (StringUtils.isNotEmpty(smPostDTO.getPostName())){
//                    predicateList.add(criteriaBuilder.like(root.get("postName"),"%"+smPostDTO.getPostName()+"%"));
//                }
////                criteriaQuery.groupBy(root.get("postName"),root.get("postLevel"));
//                criteriaBuilder.countDistinct(root.get("postName"));
////                criteriaQuery.distinct(true);
//                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
//                criteriaQuery.groupBy(root.get("postName"),root.get("postLevel"));
//                return null;
//            }
//        },pageable);
        return smPostPage.map(new Converter<SmPost, SmPostDTO>() {
            @Override
            public SmPostDTO convert(SmPost smPost) {
                SmPostDTO postDTO = new SmPostDTO();
                BeanUtils.copyProperties(smPost,postDTO);
                //根据名字和级别查询ID集合
                postDTO.setPostIds(smPostRepository.findIdByCondition(smPost.getPostName(),smPost.getPostLevel()));
                return postDTO;
            }
        });
    }

    /**
     *  根据岗位名和岗位级别删除
     * @param postName
     * @param postLevel
     */
    @Override
    public void batchDeletePost(String postName, Integer postLevel) {
        List<Long> postIds = smPostRepository.findIdByCondition(postName,postLevel);
        if (postIds != null && !postIds.isEmpty()){
            for (Long postId : postIds) {
                //删除岗位
                SmPost smPost = smPostRepository.findOne(postId);
                smPost.setStatus(INVALID_DATA_STATUS);
                smPostRepository.save(smPost);
                // 逻辑删除岗位后，删除岗位与角色关联表数据
                smPostRoleRepository.deletePostRoleByPostId(postId);
                // 逻辑删除岗位后，删除岗位与用户关联表数据
                smPostUserRepository.deletePostUserByPostId(postId);
            }
        }
    }


    /**
     * 给岗位指定角色
     * @param smPostDTO
     */
    @Override
    public void savePostAndRole(SmPostDTO smPostDTO) {
        //处理角色集合数据
//        List<Long> roleIds = new ArrayList<>();
//        roleIds.add(-1l);
//        for (String s : smPostDTO.getRoleIds()) {
//            roleIds.add(Long.valueOf(s));
//        }

        List<Long> postIds = smPostRepository.findIdByCondition(smPostDTO.getPostName(),smPostDTO.getPostLevel());
        if (postIds !=  null && !postIds.isEmpty()){
            List<SmPostRole> smPostRoleList = new ArrayList<>();
            for (Long postId : postIds) {
                // 删除岗位与角色关联表数据（只删除批量操作的数据）
                smPostRoleRepository.deletePostRoleByPost(postId);
                for (String roleId : smPostDTO.getRoleIds()) {
                    SmPostRole smPostRole = smPostRoleRepository.selectPostRoleByPost(postId,Long.valueOf(roleId));
                    if (smPostRole == null) {
                        smPostRole = new SmPostRole();
                    }
                    smPostRole.setPostId(postId);
                    smPostRole.setRoleId(Long.valueOf(roleId));
                    smPostRole.setSourceBatch(true);
                    smPostRoleList.add(smPostRole);
                }
            }
            smPostRoleRepository.save(smPostRoleList);
        }
    }


    /**
     * 根据岗位查询岗位下的角色
     * @param smPostDTO
     * @return
     */
    @Override
    public List<Long> findRoleIdsByPost(SmPostDTO smPostDTO) {
        //根据岗位查询出岗位ID集合
        List<Long> postIds = smPostRepository.findIdByCondition(smPostDTO.getPostName(),smPostDTO.getPostLevel());
        List<Long> roleIds = smPostRoleRepository.findRoleIdsByCondition(postIds);
        return roleIds;
    }


    @Override
    @Cacheable(value = "test",key = "#u")
    public String test(String u) {
        return "wqwqwqqw";
    }

}
