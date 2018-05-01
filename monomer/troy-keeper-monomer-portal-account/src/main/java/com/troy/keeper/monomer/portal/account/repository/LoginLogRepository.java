package com.troy.keeper.monomer.portal.account.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.monomer.portal.account.domain.LoginLog;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

/**
 * Created by yg on 2017/4/7.
 */
public interface LoginLogRepository extends BaseRepository<LoginLog, Serializable> {
    @Query("select loginLog from LoginLog loginLog where loginLog.sessionId = ?1 ")
    public LoginLog findBySessionId(String sessionId);
}
