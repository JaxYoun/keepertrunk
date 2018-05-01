package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.SmPost;
import com.troy.keeper.system.dto.SmPostDTO;
import com.troy.keeper.system.repository.SmPostRepository;
import com.troy.keeper.system.security.MethodAuthority.ControlType;
import com.troy.keeper.system.security.MethodAuthority.MethodAuthority;
import com.troy.keeper.system.security.SysAccount;
import com.troy.keeper.system.service.SmPostService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * Created by SimonChu on 2017/6/5.
 */
@RestController
public class SmPostResource extends BaseResource<SmPostDTO> {

    private final Logger log = LoggerFactory.getLogger(SmPostResource.class);

    @Autowired
    private SmPostService smPostService;

    @Autowired
    private SmPostRepository smPostRepository;

    // 默认分页条数
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 查询岗位列表
     *
     * @return
     */
    @RequestMapping(value = "/api/system/post/queryPostList", method = RequestMethod.POST)
    public ResponseDTO<List<SmPost>> queryPostList() {
        try {
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smPostService.queryPostList());
        } catch (Exception e) {
            log.error("[SmPostResource Class] queryPostList:岗位机构查询异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询指定岗位数据
     *
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/getPost", method = RequestMethod.POST)
    public ResponseDTO<SmPost> getPost(@RequestBody SmPostDTO smPostDTO) {
        try {
            if (smPostDTO.getId() == null) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smPostService.getPost(smPostDTO));
        } catch (Exception e) {
            log.error("[SmPostResource Class] queryPostList:岗位机构查询异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 岗位机构树查询
     * queryPostOrgTree:岗位机构树查询 2017/6/6 已完成接口测试
     *
     * @return
     */
    @RequestMapping(value = "/api/system/post/queryPostOrgTree", method = RequestMethod.POST)
    public ResponseDTO<List<Map<String, Object>>> queryPostOrgTree(HttpServletRequest httpServletRequest) {
        try {
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smPostService.queryPostOrgTree(httpServletRequest));
        } catch (Exception e) {
            log.error("[SmPostResource Class] queryPostOrgTree:岗位机构树查询接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 保存岗位信息
     * save:保存岗位信息 2017/6/6 已完成接口测试
     *
     * @return
     */
    @RequestMapping(value = "/api/system/post/save", method = RequestMethod.POST)
    public ResponseDTO<Object> save(@RequestBody SmPostDTO smPostDTO) {
        try {
            if (smPostDTO.getOrgId() == null || ("").equals(smPostDTO.getOrgId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_ORG_ID, null);
            }
            if (smPostDTO.getPostName() == null || ("").equals(smPostDTO.getPostName())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_POST_NAME, null);
            }
//            if (StringUtils.isEmpty(smPostDTO.getPostCode())){
//                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_CODE_ERROR, null);
//            }
            if (StringUtils.isNotEmpty(smPostDTO.getPostCode())){//岗位编码不是空的时候开始验证
                //验证岗位编码是否重复
                Long count = smPostDTO.getId() == null ?smPostRepository.countByCode(smPostDTO.getPostCode()):
                        smPostRepository.countUpdateByCode(smPostDTO.getPostCode(),smPostDTO.getId());
                if (count > 0l){
                    return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_CODE_EXIT_ERROR, null);
                }
            }
            if (smPostDTO.getId() == null || ("").equals(smPostDTO.getId())) {
                // 新增
                smPostService.createdData(smPostDTO);
            } else {
                // 修改
                smPostService.updateData(smPostDTO);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmPostResource Class] save:保存岗位信息接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 删除岗位信息
     * del:删除岗位信息 2017/6/6 已完成接口测试
     * del:新增检测岗位是否与人员关联功能
     *
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/del", method = RequestMethod.POST)
    public ResponseDTO<Object> del(@RequestBody SmPostDTO smPostDTO) {
        try {
            if (smPostDTO.getId() == null || ("").equals(smPostDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_ID, null);
            }
            // 检测岗位是否与人员关联
            if (!smPostService.checkPost(smPostDTO)) {
                smPostService.del(smPostDTO);
            } else {
                return fail(Const.MSG_CODE_202_POST_USER_DEL_ERROR);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmPostResource Class] del:删除岗位信息接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询岗位对应的角色权限
     * queryRolesByPostId:查询岗位对应的角色权限 2017/6/6 已完成接口测试
     *
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/queryRolesByPostId", method = RequestMethod.POST)
    public ResponseDTO<List<String>> queryRolesByPostId(@RequestBody SmPostDTO smPostDTO) {
        try {
            if (smPostDTO.getPostId() == null || ("").equals(smPostDTO.getPostId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_POST_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smPostService.queryRolesByPostId(smPostDTO));
        } catch (Exception e) {
            log.error("[SmPostResource Class] queryRolesByPostId:查询岗位对应的角色权限接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 保存岗位对应的角色权限
     * saveRoleLimit:保存岗位对应的角色权限 2017/6/5 已完成接口测试
     *
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/saveRoleLimit", method = RequestMethod.POST)
    public ResponseDTO<Object> saveRoleLimit(@RequestBody SmPostDTO smPostDTO) {
        try {
            if (smPostDTO.getPostId() == null || ("").equals(smPostDTO.getPostId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_POST_ID, null);
            }
            smPostService.saveRoleLimit(smPostDTO);
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmPostResource Class] saveRoleLimit:保存岗位对应的角色权限接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询岗位对应角色下的所有菜单数据及权限（Old:过时的方法）
     *
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/queryPostMenus", method = RequestMethod.POST)
    public ResponseDTO<Object> queryPostMenus(@RequestBody SmPostDTO smPostDTO) {
        try {
            if (smPostDTO.getPostId() == null || ("").equals(smPostDTO.getPostId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_POST_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smPostService.queryPostMenus(smPostDTO));
        } catch (Exception e) {
            log.error("[SmPostResource Class] queryPostMenus:查询岗位对应角色下的所有菜单数据及权限接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 根据岗位ID查询用户详情
     *
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/queryPostUsersByPostId", method = RequestMethod.POST)
    public ResponseDTO<Object> queryPostUsersByPostId(@RequestBody SmPostDTO smPostDTO) throws Exception {
        if (smPostDTO.getPostId() == null) {
            return fail(Const.MSG_CODE_202_POST_ID);
        }
        return success(smPostService.queryPostUsersByPostId(smPostDTO.getPostId()));
    }

    /***************************************************岗位批量操作*****************************************/

    /**
     * 批量更新保存
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/batchSaveOrUpdatePost", method = RequestMethod.POST)
    public ResponseDTO batchSaveOrUpdatePost(@RequestBody SmPostDTO smPostDTO){
//        if (StringUtils.isEmpty(smPostDTO.getPostCode())){
//            return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_CODE_ERROR, null);
//        }
        if (StringUtils.isEmpty(smPostDTO.getPostName()) || smPostDTO.getPostLevel() == null){
            return fail("参数错误");
        }else {
            String msg = smPostService.batchSaveOrUpdatePost(smPostDTO);
            if (StringUtils.isNotEmpty(msg)){
                return fail(msg);
            }
            return success();
        }
    }

    /**
     * 分页查询
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/findBatchOptionPost", method = RequestMethod.POST)
    public ResponseDTO findBatchOptionPost(@RequestBody SmPostDTO smPostDTO, @PageableDefault(value = 20) Pageable pageable,HttpServletRequest httpServletRequest){
        return success(smPostService.findBatchOptionPost(smPostDTO,pageable,httpServletRequest));
    }

    /**
     *  根据岗位名和岗位级别删除
     */
//    @MethodAuthority(menuCode = "01",CONTROL_TYPE= ControlType.DELETE)
    @RequestMapping(value = "/api/system/post/batchDeletePost", method = RequestMethod.POST)
    public ResponseDTO batchDeletePost(@RequestBody SmPostDTO smPostDTO){
        if (StringUtils.isEmpty(smPostDTO.getPostName()) || smPostDTO.getPostLevel() == null){
            return fail("参数错误");
        }else {
            smPostService.batchDeletePost(smPostDTO.getPostName(), smPostDTO.getPostLevel());
            return success();
        }
    }

    /**
     * 给岗位指定角色
     * @param smPostDTO
     */
//    @MethodAuthority(menuCode = "01",CONTROL_TYPE= ControlType.ADD)
    @RequestMapping(value = "/api/system/post/savePostAndRole", method = RequestMethod.POST)
    public ResponseDTO savePostAndRole(@RequestBody SmPostDTO smPostDTO) {
        if (StringUtils.isEmpty(smPostDTO.getPostName()) || smPostDTO.getPostLevel() == null){
            return fail("参数错误");
        }else{
            smPostService.savePostAndRole(smPostDTO);
            return success();
        }
    }

    /**
     * 根据岗位查询岗位下的角色
     * @param smPostDTO
     * @return
     */
    @RequestMapping(value = "/api/system/post/findRoleIdsByPost", method = RequestMethod.POST)
    public ResponseDTO findRoleIdsByPost(@RequestBody SmPostDTO smPostDTO) {
        if (StringUtils.isEmpty(smPostDTO.getPostName()) || smPostDTO.getPostLevel() == null){
            return fail("参数错误");
        }else{
            return success(smPostService.findRoleIdsByPost(smPostDTO));
        }
    }


    @RequestMapping(value = "/api/system/post/test", method = RequestMethod.POST)
//    @Cacheable(cacheNames = "test",key = "11")
    public String test(HttpServletRequest request){
        Long id = SecurityUtils.getCurrentUserId();
        return id+"";
    }
//
//    @RequestMapping(value = "/api/system/post/tests", method = RequestMethod.POST)
//    public  void tests(){
//        // 得到当前的认证信息
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        SysAccount sysAccount = (SysAccount)auth.getPrincipal();
//        sysAccount.setPostId(1l);
//        // 生成新的认证信息
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(sysAccount, auth.getCredentials(), auth.getAuthorities());
//        // 重置认证信息
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//    }

}
