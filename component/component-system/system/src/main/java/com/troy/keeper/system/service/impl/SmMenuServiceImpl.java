package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.system.domain.SmMenu;
import com.troy.keeper.system.dto.SmMenuDTO;
import com.troy.keeper.system.repository.SmMenuRepository;
import com.troy.keeper.system.service.SmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SimonChu on 2017/6/1.
 */
@Service
@Transactional
public class SmMenuServiceImpl extends BaseServiceImpl<SmMenu, SmMenuDTO> implements SmMenuService {
    @Autowired
    private SmMenuRepository smMenuRepository;

    // 有效数据状态
    private static final String VALID_DATA_STATUS = "1";
    // 无效数据状态
    private static final String INVALID_DATA_STATUS = "0";

    /**
     * 查询菜单列表
     *
     * @return
     */
    public List<SmMenu> list() {
        Specification<SmMenu> spec = (root, query, cb) -> query.where(cb.equal(root.get("status").as(Integer.class), VALID_DATA_STATUS)).orderBy(cb.asc(root.get("orderCode").as(Long.class))).getRestriction();
        return smMenuRepository.findAll(spec);
    }

    /**
     * 查询指定菜单
     *
     * @param smMenuDTO
     * @return
     */
    public SmMenu get(SmMenuDTO smMenuDTO) {
        return smMenuRepository.findOne(smMenuDTO.getId());
    }

    /**
     * 查询菜单列表（分页）
     *
     * @param smMenuDTO
     * @return
     */
    public Page<SmMenu> listForPage(SmMenuDTO smMenuDTO) {
        PageRequest pageRequest = new PageRequest(smMenuDTO.getPage(), smMenuDTO.getSize());
        Specification<SmMenu> spec = (root, query, cb) -> query.where(cb.equal(root.get("status").as(Integer.class), VALID_DATA_STATUS)).orderBy(cb.asc(root.get("orderCode").as(Long.class))).getRestriction();
        Page<SmMenu> smMenus = smMenuRepository.findAll(spec,pageRequest);
        return smMenus;
    }

    /**
     * 新增菜单数据
     *
     * @param smMenuDTO
     */
    public String createdData(SmMenuDTO smMenuDTO) {
        //验证菜单重复性
        Long count = smMenuDTO.getpId() != null?smMenuRepository.countByNameAndPId(smMenuDTO.getMenuName()
        ,smMenuDTO.getpId()):smMenuRepository.countByMenu(smMenuDTO.getMenuName());
        if (count >0l){
            return "该数据已经存在！";
        }
        SmMenu smMenu = new SmMenu();
        if (smMenuDTO.getpId() != null) {
            smMenu.setpId(smMenuDTO.getpId());
        }
        if (smMenuDTO.getMenuUrl() != null) {
            smMenu.setMenuUrl(smMenuDTO.getMenuUrl());
        }
        if (smMenuDTO.getMenuIcon() != null) {
            smMenu.setMenuIcon(smMenuDTO.getMenuIcon());
        }
        smMenu.setMenuType(smMenuDTO.getMenuType());
        smMenu.setMenuName(smMenuDTO.getMenuName());
        smMenu.setOrderCode(smMenuDTO.getOrderCode());
        if (!StringUtils.isEmpty(smMenuDTO.getMenuCode())){
            smMenu.setMenuCode(smMenuDTO.getMenuCode());
        }
        smMenu.setStatus(VALID_DATA_STATUS);
        smMenuRepository.save(smMenu);
        if (smMenu.getpId() == null) {
            smMenu.setRelationship(smMenu.getId().toString());
            smMenuRepository.save(smMenu);
        } else {
            // 将存入数据库的父ID作为主键ID取回父级序列并追本数据的序列中
            SmMenu smMenu1 = smMenuRepository.findOne(smMenu.getpId());
            StringBuffer sb = new StringBuffer(smMenu1.getRelationship().toString());
            sb.append("-");
            sb.append(smMenu.getId());
            smMenu.setRelationship(sb.toString());
            smMenuRepository.save(smMenu);
        }
        return null;
    }

    /**
     * 更新菜单数据
     *
     * @param smMenuDTO
     */
    public String updateData(SmMenuDTO smMenuDTO) {
        //修改重复性验证
        Long count = smMenuDTO.getpId() != null?smMenuRepository.updatePidCount(smMenuDTO.getMenuName(),
                smMenuDTO.getpId(),smMenuDTO.getId()):smMenuRepository.updateCount(smMenuDTO.getMenuName(),
                smMenuDTO.getId());
        if (count > 0l){
            return "该数据已经存在！";
        }
        SmMenu smMenu = smMenuRepository.findOne(smMenuDTO.getId());
        if (smMenuDTO.getMenuUrl() != null) {
            smMenu.setMenuUrl(smMenuDTO.getMenuUrl());
        }
        if (smMenuDTO.getMenuIcon() != null) {
            smMenu.setMenuIcon(smMenuDTO.getMenuIcon());
        }
        smMenu.setMenuType(smMenuDTO.getMenuType());
        smMenu.setMenuName(smMenuDTO.getMenuName());
        smMenu.setOrderCode(smMenuDTO.getOrderCode());
        if (!StringUtils.isEmpty(smMenuDTO.getMenuCode())){
            smMenu.setMenuCode(smMenuDTO.getMenuCode());
        }
        // 更新序列
        if (smMenuDTO.getpId() != null) {
            // 查询父级数据
            SmMenu smMenu1 = smMenuRepository.findOne(smMenuDTO.getpId());
            StringBuffer sb = new StringBuffer(smMenu1.getRelationship().toString());
            sb.append("-");
            sb.append(smMenu.getId());
            smMenu.setRelationship(sb.toString());
            smMenu.setpId(smMenuDTO.getpId());
        } else {
            smMenu.setpId(null);
            smMenu.setRelationship(smMenu.getId().toString());
        }
        smMenuRepository.save(smMenu);
        return null;
    }

    /**
     * 删除菜单
     *
     * @param smMenuDTO
     */
    public void del(SmMenuDTO smMenuDTO) {
        SmMenu smMenu = smMenuRepository.findOne(smMenuDTO.getId());
        smMenu.setStatus(INVALID_DATA_STATUS);
        smMenuRepository.save(smMenu);
        // 逻辑删除菜单后 物理删除角色菜单中间表数据
        smMenuRepository.deleteRoleMenuByMenuId(smMenuDTO.getId());
    }

    /**
     * 检查菜单数据是否有子数据
     * @param smMenuDTO
     * @return
     */
    public boolean checkDataToPid(SmMenuDTO smMenuDTO) {
        List<SmMenu> list = smMenuRepository.checkDataToPid(smMenuDTO.getId());
        if (list.size() == 0){
            return true;
        }else{
            return false;
        }
    }
}
