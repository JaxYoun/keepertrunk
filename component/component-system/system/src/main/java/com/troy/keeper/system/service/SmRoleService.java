package com.troy.keeper.system.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.SmRole;
import com.troy.keeper.system.domain.SmRoleMenu;
import com.troy.keeper.system.dto.SmRoleDTO;
import com.troy.keeper.system.dto.SmRoleMenuDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by SimonChu on 2017/6/1.
 */
public interface SmRoleService extends BaseService<SmRole, SmRoleDTO> {

    public List<SmRole> list(SmRoleDTO smRoleDTO,HttpServletRequest httpServletRequest);

    public Page<SmRole> listForPage(SmRoleDTO smRoleDTO);

    public SmRole get(SmRoleDTO smRoleDTO);

    public void createdData(SmRoleDTO smRoleDTO);

    public void updateDate(SmRoleDTO smRoleDTO);

    public void del(SmRoleDTO smRoleDTO);

    public List<SmRoleMenu> queryMenuListByRoleId(SmRoleMenuDTO smRoleMenuDTO);

    public void saveMenuLimit(SmRoleMenuDTO smRoleMenuDTO);

    public boolean checkMenuData(SmRoleMenuDTO smRoleMenuDTO);

    public boolean checkRole(SmRoleDTO smRoleDTO);

}
