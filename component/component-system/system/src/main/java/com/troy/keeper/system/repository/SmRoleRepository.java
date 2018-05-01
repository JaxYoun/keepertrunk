package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmRole;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by SimonChu on 2017/6/1.
 */
public interface SmRoleRepository extends BaseRepository<SmRole, Long> {

    @Query("select count (role.id) from SmRole role where role.roleCode = ?1 and role.status = '1' and role.roleCode is not null ")
    public Long createCount(String roleCode);

    @Query("select count (role.id) from SmRole role where role.roleCode = ?1 and role.status = '1' and role.id <> ?2 and role.roleCode is not null")
    public Long updateCount(String roleCode,Long id);

}
