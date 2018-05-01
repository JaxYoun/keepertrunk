package com.troy.keeper.system.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.dto.SmOrgDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by SimonChu on 2017/5/25.
 */
public interface SmOrgService extends BaseService<SmOrg, SmOrgDTO> {

    public List<SmOrg> list(HttpServletRequest httpServletRequest);

    public Page<SmOrg> listForPage(SmOrgDTO smOrgDTOe);

    public SmOrg get(SmOrgDTO smOrgDTO);

    public void updateData(SmOrgDTO smOrgDTO);

    public void createdData(SmOrgDTO smOrgDTO);

    public boolean checkPidData(SmOrgDTO smOrgDTO);

    public void del(SmOrgDTO smOrgDTO);

    public boolean checkOrgAndUser(SmOrgDTO smOrgDTO);

}
