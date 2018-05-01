package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.domain.SmUser;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SimonChu on 2017/5/25.
 */
public interface SmOrgRepository extends BaseRepository<SmOrg, Long> {

    @Query("select s from SmUser s where s.orgId = ?1 and s.status=1")
    public List<SmUser> getSmUserListByOrgId(Long orgId);

    @Query("select s from SmOrg s where s.status=1 order by s.orderCode")
    public List<SmOrg> getList();

    @Query("select s from SmOrg s where s.pId=?1 and s.status=1")
    public List<SmOrg> getListByPId(Long pId);

    @Query("select org.id from SmOrg org where org.orgLevel = ?1 and org.status = '1' ")
    public List<Long> findIdsByLevel(Integer orgLevel);

    @Query("select org.id from SmOrg org where org.status = '1' and org.relationship like ?1 ")
    public List<Long> findAllChildIds(String orgIdStr);

    /***
     * 根据机构查询出所有子机构
     * @param pramOne
     * @param paramTwo
     * @param id
     * @return
     */
    @Query("select org.id from SmOrg org where org.relationship like ?1 or org.relationship like ?2 or " +
            " org.id = ?3 ")
    public List<Long> findAllOrgIdsById(String pramOne,String paramTwo,Long id);

    @Query("select s from SmOrg s where s.status=1 and s.id in (?1) order by s.orderCode")
    public List<SmOrg> getListInIds(List<Long> ids);

}
