package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/5.
 */
public interface SmPostRepository extends BaseRepository<SmPost, Long> {

    @Modifying
    @Query("update SmPost set status = 0 where orgId = ?1")
    public void updatePostStatusByOrgId(Long orgId);

    @Query("select sm from SmPost sm where sm.orgId = ?1 and sm.status = 1")
    public List<SmPost> getSmPostListsByOrgId(Long orgId);

    @Query("select sm from SmPost sm where sm.status = 1")
    public List<SmPost> getSmPostList();

    @Query("select sm from SmPost sm where sm.id=?1 and sm.status = 1")
    public SmPost getPost(Long id);

    @Query("select sm.id from SmPost sm where sm.postName = ?1 and sm.postLevel = ?2 and sm.status = '1' ")
    public List<Long> findIdByCondition(String postName,Integer postLevel);

    @Query("select count(sm.id) from SmPost sm where sm.postName = ?1 and sm.postLevel = ?2 and sm.status = '1' ")
    public Long countBySave(String postName,Integer postLevel);

    @Query("select count(sm.id) from SmPost sm where sm.postName = ?1 and sm.postLevel = ?2 and sm.status = '1' and sm.id not in (?3)")
    public Long countByUpdate(String postName,Integer postLevel,List<Long> ids);

    @Query("select sm from SmPost sm where sm.postLevel = ?1 and sm.status = '1' group by sm.postLevel,sm.postName ")
    public List<SmPost> findPostByLevel(Integer postLevel);

    @Query("select sm.id from SmPost sm where sm.orgId = ?1 and sm.status = '1' and sm.postLevel is not null ")
    public List<Long> findPostIdByOrgId(Long orgId);

    @Query(value = "select sm from SmPost sm where sm.id in (?1)"
    ,countQuery = "select count (s.id) from SmPost s where s.id in (?1)")
    public Page<SmPost> findPageByCondition(List<Long> ids, Pageable pageable);


    @Query(value = "select sm from SmPost sm where sm.id in (?1) and sm.orgId in (?2)"
            ,countQuery = "select count (s.id) from SmPost s where s.id in (?1) and s.orgId in (?2)")
    public Page<SmPost> findPageByCondition(List<Long> ids,List<Long> orgIds, Pageable pageable);

    @Query(value = "select sm.id from SmPost sm where sm.status = '1' and sm.postLevel is not null group by sm.postLevel,sm.postName")
    public List<Long> findAllDistionctId();

    @Query(value = "select count (sm.id) from SmPost sm where sm.postCode = ?1 and sm.status = '1' and sm.postCode is not null ")
    public Long countByCode(String postCode);

    @Query(value = "select count (sm.id) from SmPost sm where sm.postCode = ?1 and sm.status = '1' and sm.id <> ?2 and sm.postCode is not null")
    public Long countUpdateByCode(String postCode,Long id);

    @Query(value = "select count (sm.id) from SmPost sm where sm.postCode = ?1 and sm.status = '1' and sm.id not in (?2) and sm.postCode is not null ")
    public Long countUpdateBatchByCode(String postCode,List<Long> ids);

    @Query("select post.orgId from SmPost post where post.id = ?1")
    public Long findOrgIdById(Long id);
}
