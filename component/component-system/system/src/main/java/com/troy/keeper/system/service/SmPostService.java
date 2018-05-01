package com.troy.keeper.system.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.SmMenu;
import com.troy.keeper.system.domain.SmPost;
import com.troy.keeper.system.dto.SmPostDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/5.
 */
public interface SmPostService extends BaseService<SmPost, SmPostDTO> {

    public List<SmPost> queryPostList();

    public SmPost getPost(SmPostDTO smPostDTO);

    public List<Map<String, Object>> queryPostOrgTree(HttpServletRequest httpServletRequest);

    public void createdData(SmPostDTO smPostDTO);

    public void updateData(SmPostDTO smPostDTO);

    public void del(SmPostDTO smPostDTO);

    public List<String> queryRolesByPostId(SmPostDTO smPostDTO);

    public void saveRoleLimit(SmPostDTO smPostDTO);

    public List<SmMenu> queryPostMenus(SmPostDTO smPostDTO);

    public List<Map<String, Object>> queryPostUsersByPostId(Long postId) throws Exception;

    public boolean checkPost(SmPostDTO smPostDTO);

    /**
     * 批量更新保存
     * @param smPostDTO
     * @return
     */
    public String batchSaveOrUpdatePost(SmPostDTO smPostDTO);

    /**
     * 分页查询
     * @param smPostDTO
     * @return
     */
    public Page<SmPostDTO> findBatchOptionPost(SmPostDTO smPostDTO, Pageable pageable,HttpServletRequest httpServletRequest);

    /**
     *  根据岗位名和岗位级别删除
     * @param postName
     * @param postLevel
     */
    public void batchDeletePost(String postName,Integer postLevel);

    /**
     * 给岗位指定角色
     * @param smPostDTO
     */
    public void savePostAndRole(SmPostDTO smPostDTO);

    /**
     * 根据岗位查询岗位下的角色
     * @param smPostDTO
     * @return
     */
    public List<Long> findRoleIdsByPost(SmPostDTO smPostDTO);

    @Cacheable(cacheNames = "test",key = "#p0")
    public String test(String u);

}
