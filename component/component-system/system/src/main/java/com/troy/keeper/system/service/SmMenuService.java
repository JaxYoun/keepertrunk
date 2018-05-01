package com.troy.keeper.system.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.SmMenu;
import com.troy.keeper.system.dto.SmMenuDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/1.
 */
public interface SmMenuService extends BaseService<SmMenu, SmMenuDTO> {

    public List<SmMenu> list();

    public SmMenu get(SmMenuDTO smMenuDTO);

    public Page<SmMenu> listForPage(SmMenuDTO smMenuDTO);

    public String createdData(SmMenuDTO smMenuDTO);

    public String updateData(SmMenuDTO smMenuDTO);

    public void del(SmMenuDTO smMenuDTO);

    public boolean checkDataToPid(SmMenuDTO smMenuDTO);

}
