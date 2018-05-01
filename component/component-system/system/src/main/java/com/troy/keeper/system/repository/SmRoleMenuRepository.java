package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.SmMenu;
import com.troy.keeper.system.domain.SmRoleMenu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/5.
 */
public interface SmRoleMenuRepository extends BaseRepository<SmRoleMenu, Long> {

    @Modifying
    @Query("delete FROM SmRoleMenu where roleId = ?1")
    public void deleteRoleMenus(Long roleId);

    @Query("select distinct m from SmMenu m left join m.smRoleMenuList rm left join rm.smRole.smPostRoleList pr where pr.postId= ?1 order by m.orderCode asc ")
    public List<SmMenu> queryPostMenus(Long postId);

    @Query("select count(*) from SmMenu m where m.id = ?1")
    public int checkMenuDataByMenuId(Long menuId);

    @Query("select srm from SmRoleMenu srm where srm.roleId = ?1")
    public List<Object[]> getMenuIdsByRoleId(Long roleId);

    @Query("select distinct rm.menuId,sm.pId,sm.menuName,sm.menuUrl,sm.menuIcon,sm.orderCode,sm.relationship,sm.menuType,min(rm.level),sm.menuCode " +
            "as limit_level from SmMenu sm left join sm.smRoleMenuList rm where rm.roleId in ?1 " +
            "group by rm.menuId,sm.pId,sm.menuName,sm.menuUrl,sm.menuIcon,sm.orderCode,sm.relationship,sm.menuType,sm.menuCode order by sm.orderCode asc ")
    public List<Object[]> getMenuIdsLevelByRoleIds(List<Long> menuIdList);
}
