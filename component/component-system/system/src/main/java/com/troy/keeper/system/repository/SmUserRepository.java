package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmUser;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by SimonChu on 2017/5/27.
 */
public interface SmUserRepository extends BaseRepository<SmUser, Long> {

    @Query("select smUser from SmUser smUser where smUser.loginName = ?1 and smUser.status = '1' ")
    public List<SmUser> checkLoginName(String loginName);

    @Query("select smUser from SmUser smUser where smUser.email = ?1")
    public List<SmUser> checkEmail(String email);

    @Query("select count(*) from SmUser smUser where smUser.id = ?1")
    public int ifUserId(Long id);

    @Query("select smUser from SmUser smUser where smUser.loginName = ?1 and smUser.status = '1'")
    public Optional<SmUser> findOneWithAuthoritiesByLoginName(String loginName);

    @Query("select smUser from SmUser smUser where smUser.activated=1 and smUser.status=1 order by smUser.id")
    public List<SmUser> getUserIdAndUserName();

    @Query("select distinct smUser from SmUser smUser where smUser.id in ?1 and smUser.status=1 and smUser.activated=1")
    public List<SmUser> getUserByIds(List<Long> userIds);

}
