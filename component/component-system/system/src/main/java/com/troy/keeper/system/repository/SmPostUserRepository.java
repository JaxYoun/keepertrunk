package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmPostUser;
import com.troy.keeper.system.domain.SmUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/6.
 */
public interface SmPostUserRepository extends BaseRepository<SmPostUser, Long> {

    @Query("select spu.userId from SmPostUser as spu where spu.postId = ?1")
    public List<Long> queryPostUsers(Long postId);

    @Query
    public List<SmPostUser> queryDistinctByPostId(Long postId);

    @Query("select spu.postId,su.postName from SmPostUser as spu left join spu.smPost su where spu.userId = ?1")
    public List<Object[]> queryUserPosts(Long userId);

    @Query("select spu.postId from SmPostUser spu where spu.userId = ?1")
    public List<Long> getPostUserByUserId(Long userId);

    @Modifying
    @Query("delete from SmPostUser where postId = ?1")
    public void deletePostUserByPostId(Long postId);

    @Modifying
    @Query("delete from SmPostUser where userId = ?1")
    public void deletePostUserByUserId(Long userId);

    @Query("select pu.userId, pu.smPost.smOrg.orgName from SmPostUser pu  ")
    public List<Object[]> findAllUserOrg();

}
