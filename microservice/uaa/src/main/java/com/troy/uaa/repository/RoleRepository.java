package com.troy.uaa.repository;

import com.troy.uaa.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * Created by yjm on 2017/4/9.
 */
public interface RoleRepository extends BaseRepository<Role,Long> {

    @Query("SELECT r.role FROM UserRole r WHERE r.user.login = ?1 ")
    public List<Role>findByUser(String userName);

    @Query("SELECT r.roleName FROM Role r WHERE r.id = ?1")
    public String findRoleNameById(Long id);

    @Query("select mi.role.roleCode from UserRole mi where mi.user.id = ?1 ")
    public Set<String> getCodeByUser(Long id);
}
