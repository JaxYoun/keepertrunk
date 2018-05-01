package com.troy.keeper.system.service;

import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.intercept.account.AccountService;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/5/27.
 */
public interface SmUserService extends AccountService {

    public List<Map<String, Object>> list(SmUserDTO smUserDTO,HttpServletRequest httpServletRequest) throws Exception;

    public Page<Map<String, Object>> listForPage(SmUserDTO smUserDTO) throws Exception;
    public List<SmUser> checkLoginName(SmUserDTO smUserDTO);

    public List<SmUser> checkEmail(SmUserDTO smUserDTO);

    public Map<String, Object> get(SmUserDTO smUserDTO) throws Exception;

    public List<Map<String, Object>> queryOrgUserTree(HttpServletRequest httpServletRequest);

    public void inactivate(SmUserDTO smUserDTO);

    public void activate(SmUserDTO smUserDTO);

    public int ifUserId(SmUserDTO smUserDTO);

    public int checkUserOldPassword(SmUserDTO smUserDTO);

    public List<SmUser> getUserIdAndUserName();

    public void resetPassword(SmUserDTO smUserDTO);

}
