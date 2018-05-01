package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmMenu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/1.
 */
public interface SmMenuRepository extends BaseRepository<SmMenu, Long> {

    @Modifying
    @Query("delete from SmRoleMenu where menuId = ?1")
    public void deleteRoleMenuByMenuId(Long menuId);

    @Query("select s from SmMenu s where s.pId=?1 and s.status=1")
    public List<SmMenu> checkDataToPid(Long pId);

    @Query("select count (s.id) from SmMenu s where s.pId = ?2 and s.menuName = ?1 and s.status = '1'")
    public Long countByNameAndPId(String name,Long pid);

    @Query("select count (s.id) from SmMenu s where s.pId is null and s.menuName = ?1 and s.status = '1' ")
    public Long countByMenu(String name);

    @Query("select count (s.id) from SmMenu s where s.pId = ?2 and s.menuName = ?1 and s.status = '1'" +
            " and s.id <> ?3 ")
    public Long updatePidCount(String name,Long pid,Long id);

    @Query("select count (s.id) from SmMenu s where s.pId is null and s.menuName = ?1 and s.status = '1'" +
            " and s.id <> ?2 ")
    public Long updateCount(String name,Long id);

    @Query("select count (m.id) from SmMenu m where m.menuCode = ?1 and m.status = 1")
    public Long createCount(String menuCode);

    @Query("select count (m.id) from SmMenu m where m.menuCode = ?1 and m.status = 1 and m.id<>?2")
    public Long updateCountByCode(String menuCode,Long id);

}
