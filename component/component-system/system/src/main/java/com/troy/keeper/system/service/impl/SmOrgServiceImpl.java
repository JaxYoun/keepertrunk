package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.domain.SmPost;
import com.troy.keeper.system.domain.SmPostRole;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmOrgDTO;
import com.troy.keeper.system.dto.SmPostDTO;
import com.troy.keeper.system.repository.SmOrgRepository;
import com.troy.keeper.system.repository.SmPostRepository;
import com.troy.keeper.system.repository.SmPostRoleRepository;
import com.troy.keeper.system.repository.SmUserRepository;
import com.troy.keeper.system.service.SmOrgService;
import com.troy.keeper.system.service.SmPostService;
import com.troy.keeper.system.util.PostUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by SimonChu on 2017/5/25.
 */
@Service
@Transactional
public class SmOrgServiceImpl extends BaseServiceImpl<SmOrg, SmOrgDTO> implements SmOrgService {

    @Autowired
    private SmOrgRepository smOrgRepository;

    @Autowired
    private SmUserRepository smUserRepository;

    @Autowired
    private SmPostRepository smPostRepository;

    @Autowired
    private SmPostRoleRepository smPostRoleRepository;

    @Autowired
    private SmPostService smPostService;

    @Autowired
    private PostUtils postUtils;

    // 有效数据状态
    private static final String VALID_DATA_STATUS = "1";
    // 无效数据状态
    private static final String INVALID_DATA_STATUS = "0";


    /**
     * 查询机构数据
     *
     * @return
     */
    public List<SmOrg> list(HttpServletRequest httpServletRequest) {
        if (postUtils.getOrgLimit()){
            return smOrgRepository.getListInIds(postUtils.getOrgIds(httpServletRequest));
        }else {
            return smOrgRepository.getList();
        }
    }

    /**
     * 查询机构数据（分页）
     *
     * @return
     */
    public Page<SmOrg> listForPage(SmOrgDTO smOrgDTO) {
        PageRequest pageRequest = new PageRequest(smOrgDTO.getPage(), smOrgDTO.getSize());
        Specification<SmOrg> spec = (root, query, cb) -> query.where(cb.equal(root.get("status").as(Integer.class), 1)).orderBy(cb.asc(root.get("orderCode").as(Long.class))).getRestriction();
        Page<SmOrg> orglist = smOrgRepository.findAll(spec, pageRequest);
        return orglist;
    }

    /**
     * 查询指定组织机构
     *
     * @param smOrgDTO
     * @return
     */
    public SmOrg get(SmOrgDTO smOrgDTO) {
        SmOrg one = smOrgRepository.findOne(smOrgDTO.getId());
        return one;
    }

    /**
     * 更新机构
     *
     * @param smOrgDTO
     */
    public void updateData(SmOrgDTO smOrgDTO) {
        SmOrg smOrg = smOrgRepository.findOne(smOrgDTO.getId());
        Integer oldLevel = smOrg.getOrgLevel();
        smOrg.setId(smOrgDTO.getId());
        // 更新序列
        if (smOrgDTO.getpId() != null) {
            // 查询父级数据
            SmOrg smOrg1 = smOrgRepository.findOne(smOrgDTO.getpId());
            StringBuffer sb = new StringBuffer(smOrg1.getRelationship().toString());
            sb.append("-");
            sb.append(smOrg.getId());
            smOrg.setRelationship(sb.toString());
            smOrg.setpId(smOrgDTO.getpId());
        } else {
            smOrg.setpId(null);
            smOrg.setRelationship(smOrg.getId().toString());
        }
        smOrg.setOrgLevel(smOrgDTO.getOrgLevel());
        smOrg.setOrgName(smOrgDTO.getOrgName());
        smOrg.setOrderCode(smOrgDTO.getOrderCode());
        smOrg.setStatus(VALID_DATA_STATUS);
        smOrgRepository.save(smOrg);

        //同步岗位数据
        if (oldLevel == null && smOrgDTO.getOrgLevel() != null) {//新加
            synchronizationPost(smOrg.getOrgLevel(), smOrg.getId());
        }else if (oldLevel != null && smOrgDTO.getOrgLevel() == null){//删除
            this.deletePostByOrgId(smOrg.getId());
        }else if (oldLevel != null && smOrgDTO.getOrgLevel() != null
                && oldLevel.intValue() != smOrgDTO.getOrgLevel().intValue()){//替换
            //先删除，在新增
            this.deletePostByOrgId(smOrg.getId());
            synchronizationPost(smOrg.getOrgLevel(), smOrg.getId());
        }
    }

    /**
     * 根据机构ID删除
     * @param orgId
     */
    public void deletePostByOrgId(Long orgId){
        //根据机构ID找出所有的岗位ID集合(批量操作的岗位)
        List<Long> postIds = smPostRepository.findPostIdByOrgId(orgId);
        if (postIds != null && !postIds.isEmpty()){
            SmPostDTO smPostDTO = new SmPostDTO();
            for (Long postId : postIds) {
                smPostDTO.setId(postId);
                smPostService.del(smPostDTO);
            }
        }
    }

    /**
     * 新增机构
     *
     * @param smOrgDTO
     */
    public void createdData(SmOrgDTO smOrgDTO) {
        SmOrg smOrg = new SmOrg();
        if (smOrgDTO.getpId() != null) {
            smOrg.setpId(smOrgDTO.getpId());
        }
        smOrg.setOrgName(smOrgDTO.getOrgName());
        smOrg.setOrderCode(smOrgDTO.getOrderCode());
        smOrg.setStatus(VALID_DATA_STATUS);
        smOrg.setOrgLevel(smOrgDTO.getOrgLevel());
        smOrg = smOrgRepository.save(smOrg);
        // 新增返回的父ID为空
        if (smOrg.getpId() == null) {
            smOrg.setRelationship(smOrg.getId().toString());
            smOrgRepository.save(smOrg);
        } else {
            // 将存入数据库的父ID作为主键ID取回父级序列并追本数据的序列中
            SmOrg smOrg1 = smOrgRepository.findOne(smOrg.getpId());
            StringBuffer sb = new StringBuffer(smOrg1.getRelationship().toString());
            sb.append("-");
            sb.append(smOrg.getId());
            smOrg.setRelationship(sb.toString());
            smOrgRepository.save(smOrg);
        }
        //同步岗位数据1
        if(smOrgDTO.getOrgLevel() != null) {
            synchronizationPost(smOrg.getOrgLevel(), smOrg.getId());
        }
    }

    /**
     * 检查改ID是否有子数据有关的数据
     *
     * @param smOrgDTO
     * @return
     */
    public boolean checkPidData(SmOrgDTO smOrgDTO) {
        List<SmOrg> list = smOrgRepository.getListByPId(smOrgDTO.getId());
        if (list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除机构
     *
     * @param smOrgDTO
     */
    public void del(SmOrgDTO smOrgDTO) {
        SmOrg smOrg = smOrgRepository.findOne(smOrgDTO.getId());
        smOrg.setId(smOrgDTO.getId());
        smOrg.setStatus(INVALID_DATA_STATUS);
        smOrgRepository.save(smOrg);
        // 清理用户组织机构ID
//        List<SmUser> smUserList = smOrgRepository.getSmUserListByOrgId(smOrgDTO.getId());
//        for (int i = 0; i < smUserList.size(); i++) {
//            smUserList.get(i).setOrgId(null);
//            smUserRepository.save(smUserList.get(i));
//        }
        // 获得机构下的所有岗位数据
        List<SmPost> smPostList = smPostRepository.getSmPostListsByOrgId(smOrgDTO.getId());
        // 逻辑删除岗位数据
        smPostRepository.updatePostStatusByOrgId(smOrgDTO.getId());
        // 清空岗位下的角色关联数据
        for (int j = 0; j < smPostList.size(); j++) {
            smPostRoleRepository.deletePostRoleByPostId(smPostList.get(j).getId());
        }
    }

    /**
     * 检查机构用户关联
     *
     * @param smOrgDTO
     * @return
     */
    public boolean checkOrgAndUser(SmOrgDTO smOrgDTO) {
        List<SmUser> smUserList = smOrgRepository.getSmUserListByOrgId(smOrgDTO.getId());
        if (smUserList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据岗位级别同步岗位数据
     * @param postLevel
     * @param orgId
     */
    public void synchronizationPost(Integer postLevel,Long orgId){
        //根据岗位级别找出岗位信息
        List<SmPost> smPostList = smPostRepository.findPostByLevel(postLevel);
        if (smPostList != null && !smPostList.isEmpty()){
            for (SmPost smPost : smPostList) {
                SmPost post = new SmPost();
                post.setPostName(smPost.getPostName());
                post.setPostDesc(smPost.getPostDesc());
                post.setOrgId(orgId);
                post.setPostLevel(smPost.getPostLevel());
                post.setStatus("1");
                post = smPostRepository.save(post);

                //根据岗位名称和级别找出所有岗位信息下的角色ID集合
                List<Long> postIds = smPostRepository.findIdByCondition(post.getPostName(),post.getPostLevel());
                List<Long> roleIds = smPostRoleRepository.findRoleIdsByCondition(postIds);
                if (roleIds != null && !roleIds.isEmpty()){
                    for (Long roleId : roleIds) {
                        SmPostRole smPostRole = new SmPostRole();
                        smPostRole.setSourceBatch(true);
                        smPostRole.setRoleId(roleId);
                        smPostRole.setPostId(post.getId());
                        smPostRoleRepository.save(smPostRole);
                    }
                }
            }
        }


    }


}

