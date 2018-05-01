package com.troy.uaa.repository;

import com.troy.uaa.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yjm on 2017/4/9.
 */
public interface UserRoleRepository extends BaseRepository<UserRole,Long> {

    @Modifying
    @Query("DELETE FROM UserRole r WHERE r.user.id = ?1 ")
    public void deleteByUserId(Long id);

    @Modifying
    @Query("DELETE FROM UserRole r WHERE r.user.id IN (?1) ")
    public void deleteByUserIds(List<Long> ids);

    //根据租户ID删除角色用户表
    @Modifying
    @Query("DELETE FROM UserRole r WHERE r.user.id IN (SELECT u.id FROM User u WHERE u.tenantId = ?1) ")
    public void deleteByTenantIds(Long id);
}
