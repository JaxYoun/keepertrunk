package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmPostRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by SimonChu on 2017/6/5.
 */
public interface SmPostRoleRepository extends BaseRepository<SmPostRole, Long> {

    @Modifying
    @Query("delete from SmPostRole where postId = ?1")
    public void deletePostRoleByPostId(Long postId);

    @Modifying
    @Query("delete from SmPostRole where roleId = ?1")
    public void deletePostRoleByRoleId(Long roleId);

    @Query("select sm.roleId from SmPostRole sm where sm.postId = ?1")
    public List<Long> getPostRoleByPostId(Long postId);

    @Query("select distinct r.roleId from SmPostRole r where r.postId in (?1) and r.sourceBatch = true ")
    public List<Long> findRoleIdsByCondition(List<Long> postId);

    @Modifying
    @Query("delete from SmPostRole where postId = ?1 AND sourceBatch = true ")
    public void deletePostRoleByPost(Long postId );


    @Modifying
    @Query("delete from SmPostRole where postId = ?1 AND sourceBatch is null")
    public void deletePostRoleByPostSource(Long postId);

    @Query("select r.id from SmPostRole r where r.postId = ?1 and r.roleId = ?2")
    public Long checkByPostAndRole(Long postId,Long roleId);

    @Query("select r from SmPostRole r where r.postId = ?1 and r.roleId = ?2")
    public SmPostRole selectPostRoleByPost(Long postId,Long roleId );

}
