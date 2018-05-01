package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.system.domain.SmPostRole;
import com.troy.keeper.system.domain.SmRole;
import com.troy.keeper.system.domain.SmRoleMenu;
import com.troy.keeper.system.dto.SmRoleDTO;
import com.troy.keeper.system.dto.SmRoleMenuDTO;
import com.troy.keeper.system.repository.SmPostRoleRepository;
import com.troy.keeper.system.repository.SmRoleMenuRepository;
import com.troy.keeper.system.repository.SmRoleRepository;
import com.troy.keeper.system.service.SmRoleService;
import com.troy.keeper.system.util.PostUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/1.
 */
@Service
@Transactional
public class SmRoleServiceImpl extends BaseServiceImpl<SmRole, SmRoleDTO> implements SmRoleService {

    @Autowired
    private SmRoleRepository smRoleRepository;

    @Autowired
    private SmPostRoleRepository smPostRoleRepository;

    @Autowired
    private SmRoleMenuRepository smRoleMenuRepository;

    @Autowired
    private PostUtils postUtils;

    // 有效数据状态
    private static final String VALID_DATA_STATUS = "1";
    // 无效数据状态
    private static final String INVALID_DATA_STATUS = "0";

    /**
     * 查询角色列表
     *
     * @param smRoleDTO
     * @return
     */
    public List<SmRole> list(SmRoleDTO smRoleDTO,HttpServletRequest httpServletRequest) {
        Specification<SmRole> spec = (root, query, cb) -> {
            Join<SmRole,SmPostRole> postRoleJoin = root.join("smPostRoleList",JoinType.LEFT);
            List<Predicate> list = new ArrayList<>();
            if (smRoleDTO.getRoleName() != null) {
                list.add(cb.like(root.get("roleName").as(String.class), "%" + smRoleDTO.getRoleName() + "%"));
            }
            if (postUtils.getOrgLimit()){
                list.add(postRoleJoin.get("smPost").get("orgId").in(postUtils.getOrgIds(httpServletRequest)));
            }
            list.add(cb.equal(root.get("status").as(Integer.class), VALID_DATA_STATUS));
            Predicate[] p = new Predicate[list.size()];
            query.distinct(true);
            return cb.and(list.toArray(p));
        };
        List<SmRole> list = smRoleRepository.findAll(spec);
        return list;
    }

    /**
     * 查询角色列表（分页）
     *
     * @param smRoleDTO
     * @return
     */
    public Page<SmRole> listForPage(SmRoleDTO smRoleDTO) {
        PageRequest pageRequest = new PageRequest(smRoleDTO.getPage(), smRoleDTO.getSize());
        Specification<SmRole> spec = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (smRoleDTO.getRoleName() != null) {
                list.add(cb.like(root.get("roleName").as(String.class), "%" + smRoleDTO.getRoleName() + "%"));
            }
            list.add(cb.equal(root.get("status").as(Integer.class), VALID_DATA_STATUS));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        Page<SmRole> smRoles = smRoleRepository.findAll(spec, pageRequest);
        return smRoles;
    }

    /**
     * 查询指定角色
     *
     * @param smRoleDTO
     * @return
     */
    public SmRole get(SmRoleDTO smRoleDTO) {
        return smRoleRepository.findOne(smRoleDTO.getId());
    }

    /**
     * 新增角色
     *
     * @param smRoleDTO
     */
    public void createdData(SmRoleDTO smRoleDTO) {
        SmRole smRole = new SmRole();
        smRole.setStatus(VALID_DATA_STATUS);
        smRole.setRoleName(smRoleDTO.getRoleName());
        smRole.setRoleCode(smRoleDTO.getRoleCode());
        if (smRoleDTO.getRoleDesc() != null) {
            smRole.setRoleDesc(smRoleDTO.getRoleDesc());
        }
        smRoleRepository.save(smRole);
    }

    /**
     * 更新角色
     *
     * @param smRoleDTO
     */
    public void updateDate(SmRoleDTO smRoleDTO) {
        SmRole smRole = smRoleRepository.findOne(smRoleDTO.getId());
        smRole.setRoleName(smRoleDTO.getRoleName());
        if (smRoleDTO.getRoleDesc() != null) {
            smRole.setRoleDesc(smRoleDTO.getRoleDesc());
        }
        smRole.setRoleCode(smRoleDTO.getRoleCode());
        smRoleRepository.save(smRole);
    }

    /**
     * 删除角色
     *
     * @param smRoleDTO
     */
    public void del(SmRoleDTO smRoleDTO) {
        SmRole smRole = smRoleRepository.findOne(smRoleDTO.getId());
        smRole.setStatus(INVALID_DATA_STATUS);
        smRoleRepository.save(smRole);
        // 清空该角色所有菜单权限
        smRoleMenuRepository.deleteRoleMenus(smRoleDTO.getId());
        // 逻辑删除角色后完成清空角色上的所有岗位关联数据
        smPostRoleRepository.deletePostRoleByRoleId(smRoleDTO.getId());
    }

    /**
     * 检查角色是否与岗位关联
     *
     * @param smRoleDTO
     * @return
     */
    public boolean checkRole(SmRoleDTO smRoleDTO) {
        Specification<SmPostRole> spec = (root, query, cb) -> query.where(cb.equal(root.get("roleId").as(Long.class), smRoleDTO.getId())).getRestriction();
        List<SmPostRole> smPostRoles = smPostRoleRepository.findAll(spec);
        if (smPostRoles.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查询角色对应的菜单
     *
     * @param smRoleMenuDTO
     * @return
     */
    public List<SmRoleMenu> queryMenuListByRoleId(SmRoleMenuDTO smRoleMenuDTO) {
        Specification<SmRoleMenu> spec = (root, query, cb) -> query.where(cb.equal(root.get("roleId").as(Long.class), smRoleMenuDTO.getRoleId())).getRestriction();
        return smRoleMenuRepository.findAll(spec);
    }

    /**
     * 保存角色的菜单权限
     */
    public void saveMenuLimit(SmRoleMenuDTO smRoleMenuDTO) {
        SmRoleMenu smRoleMenu = new SmRoleMenu();
        smRoleMenu.setRoleId(smRoleMenuDTO.getRoleId());
        // 清空该角色所有菜单权限
        smRoleMenuRepository.deleteRoleMenus(smRoleMenuDTO.getRoleId());
        List<Map<String, String>> menus = smRoleMenuDTO.getMenus();
        if (menus.size() != 0) {
            for (int i = 0; i < menus.size(); i++) {
                SmRoleMenu smRoleMenu1 = new SmRoleMenu();
                smRoleMenu1.setRoleId(smRoleMenuDTO.getRoleId());
                smRoleMenu1.setLevel(Integer.valueOf(menus.get(i).get("level").toString()));
                smRoleMenu1.setMenuId(Long.valueOf(menus.get(i).get("menuId").toString()));
                smRoleMenuRepository.save(smRoleMenu1);
            }
        }

    }

    /**
     * 检查菜单数据是否存在
     *
     * @param smRoleMenuDTO
     * @return
     */
    public boolean checkMenuData(SmRoleMenuDTO smRoleMenuDTO) {
        List<Map<String, String>> menus = smRoleMenuDTO.getMenus();
        int check = 0;
        if (menus.size() != 0) {
            for (int i = 0; i < menus.size(); i++) {
                if (smRoleMenuRepository.checkMenuDataByMenuId(Long.valueOf(menus.get(i).get("menuId"))) != 0) {
                    check = 1;
                } else {
                    check = 0;
                }
            }
        } else {
            check = 2;
        }
        if (check == 0) {
            return false;
        } else if (check == 2) {
            return true;
        } else {
            return true;
        }
    }

}