package com.troy.keeper.system.security.MethodAuthority;

import com.google.common.base.Splitter;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.system.dto.UserInfoDTO;
import com.troy.keeper.system.service.SmLoginService;
import com.troy.keeper.system.service.impl.SmLoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * * * * * * * * * * * * *
 * 方法权限拦截器
 * * * * * * * * * * * * *
 * Class Name:MethodInterceptor
 *
 * @author SimonChu By Troy
 * @create 2017-10-26 16:54
 **/
public class MethodAuthorityInterceptor implements HandlerInterceptor {

    @Autowired
    public SmLoginService smLoginService;

    private final Logger log = LoggerFactory.getLogger(MethodAuthorityInterceptor.class);

    /**
     * 在执行目标方法之前执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的Access注解
        MethodAuthority methodAuthority = method.getAnnotation(MethodAuthority.class);
        // 1.判断接口权限主方法
        return checkAuthority(httpServletResponse, methodAuthority);
    }

    /**
     * 1.判断接口权限主方法
     * 第一步：主要明确该方法是否存在方法权限注解信息，如果没有注解信息那么直接放行，反之则进入下一步方法权限注解功能开关验证。
     *
     * @param httpServletResponse
     * @param methodAuthority
     * @return
     * @throws IOException
     */
    private boolean checkAuthority(HttpServletResponse httpServletResponse, MethodAuthority methodAuthority) throws IOException {
        // 如果methodAuthority为空则直接放行通过
        if (methodAuthority == null) {
            return true;
        } else {
            // 2.获取接口注解判断权限
            return checkAnnotation(httpServletResponse, methodAuthority);
        }
    }

    /**
     * 2.获取接口注解判鉴权是否需要启动
     * 第二步：主要是判定注解上的方法权限开关是否开启，如果是关闭状态，那么直接予以放行，反之则进入第三步获取用户信息，检查用户信息是否完整
     *
     * @param httpServletResponse
     * @param methodAuthority
     * @return
     * @throws IOException
     */
    private boolean checkAnnotation(HttpServletResponse httpServletResponse, MethodAuthority methodAuthority) throws IOException {
        // 如果注解开关呈关闭状态，则直接放行通过
        if (!methodAuthority.annotationSwitch()) {
            return true;
        } else {
            // 3.判定是否用户是否有菜单级权限赋予
            return checkSwitch(httpServletResponse, methodAuthority);
        }
    }

    /**
     * 3.获取用户信息，检查用户信息是否完整
     * 第三步：主要是对用户的信息进行检查，没有问题后进入权限鉴权
     *
     * @param httpServletResponse
     * @param methodAuthority
     * @return
     * @throws IOException
     */
    private boolean checkSwitch(HttpServletResponse httpServletResponse, MethodAuthority methodAuthority) throws IOException {
        // 如果注解开关呈开启状态，则直接开始验证
        // 获取当前登录用户缓存信息
        UserInfoDTO userInfoDTO = getUserInfo();
        // 该用户菜单IDS为空时，则直接返回错误信息
        if (userInfoDTO.getMenuIds() == null || userInfoDTO.getMenuIds().isEmpty()) {
            returnMsg(httpServletResponse, getResponseDTO("403", "用户信息缺损，请重新登录后重试！"));
            return false;
        } else {
            return checkLimit(httpServletResponse, methodAuthority, userInfoDTO);

        }
    }

    /**
     * 4.判定是否有权限
     * 第四步：主要是对注解上的菜单编码进行耦合
     *
     * @param httpServletResponse
     * @param methodAuthority
     * @param userInfoDTO
     * @return
     * @throws IOException
     */
    private boolean checkLimit(HttpServletResponse httpServletResponse, MethodAuthority methodAuthority, UserInfoDTO userInfoDTO) throws IOException {
        // 开始认证过程
        // 处理注解上的MenuCode
        String menuCodeString = methodAuthority.menuCode();
        if (StringUtils.isEmpty(menuCodeString)) {
            returnMsg(httpServletResponse, getResponseDTO("403", "该操作未定义归属菜单编码，请求拒绝！"));
            return false;
        } else {
            // 注释当的MenuCode逗号分段
            List<String> methodMenuCode = Splitter.on(",").trimResults().splitToList(menuCodeString);
            List<Map<String, Object>> menuIds = userInfoDTO.getMenuIds();
            List<String> levelList = new ArrayList<>();
            // 交叉匹配菜单Code
            for (int i = 0; i < menuIds.size(); i++) {
                for (int t = 0; t < methodMenuCode.size(); t++) {
                    if (methodMenuCode.get(t).equals(menuIds.get(i).get("menuCode"))) {
                        // 匹配到菜单授权后，判断权限级别
                        if (StringUtils.isEmpty(menuIds.get(i).get("limitLevel").toString())) {
                            returnMsg(httpServletResponse, getResponseDTO("404", "用户菜单权限获取失败，拒绝请求！"));
                            return false;
                        }else {
                            levelList.add(menuIds.get(i).get("limitLevel").toString());
                        }
                    }
                }
            }
            // 如果levelList为空时，则判定未获取到权限
            log.debug("权限level: {}",levelList.toString());
            if (levelList.isEmpty()){
                returnMsg(httpServletResponse, getResponseDTO("403", "该接口无权访问，调用失败！！"));
                return false;
            }
            // 获取最大权限
            if (checkLimitLevel(methodAuthority,Collections.max(levelList))){
                return true;
            }else {
                returnMsg(httpServletResponse, getResponseDTO("403", "该接口无权访问，调用失败！！"));
                return false;
            }
        }
    }

    /**
     * 检查操作权限
     * 前端：1.访问 创建 更新 执行 删除 <-> 后端：QUERY ADD UPDATE DELETE
     * 前端：2.访问 创建 更新 执行 <-> 后端：QUERY ADD UPDATE
     * 前端：3.访问 创建 更新 <-> 后端：QUERY ADD UPDATE
     * 前端：4.访问 创建 执行 <-> 后端：QUERY ADD UPDATE
     * 前端：5.访问 创建 <-> 后端：QUERY ADD UPDATE
     * 前端：6.访问 执行 <-> 后端：QUERY UPDATE
     * 前端：7.访问 <-> 后端：QUERY
     * 前端：8.无 <-> 后端：无
     *
     * @param methodAuthority
     * @param limitLevel
     * @return
     */
    private boolean checkLimitLevel(MethodAuthority methodAuthority, String limitLevel) {
        log.debug("开始判断权限，level：{}",limitLevel);
        // 查询权限
        if (methodAuthority.CONTROL_TYPE().equals(ControlType.QUERY)) {
            if (("1").equals(limitLevel) || ("2").equals(limitLevel) || ("3").equals(limitLevel) || ("4").equals(limitLevel) || ("5").equals(limitLevel) || ("6").equals(limitLevel) || ("7").equals(limitLevel)) {
                return true;
            } else {
                return false;
            }
            // 更新权限
        } else if (methodAuthority.CONTROL_TYPE().equals(ControlType.UPDATE)) {
            if (("1").equals(limitLevel) || ("2").equals(limitLevel) || ("3").equals(limitLevel)){
//                    || ("4").equals(limitLevel) || ("5").equals(limitLevel) || ("6").equals(limitLevel)) {
                return true;
            } else {
                return false;
            }
            // 删除权限
        } else if (methodAuthority.CONTROL_TYPE().equals(ControlType.DELETE)) {
            if (("1").equals(limitLevel)) {
                return true;
            } else {
                return false;
            }
            // 新增权限
        } else if (methodAuthority.CONTROL_TYPE().equals(ControlType.ADD)) {
            if (("1").equals(limitLevel) || ("2").equals(limitLevel) || ("3").equals(limitLevel) || ("4").equals(limitLevel) || ("5").equals(limitLevel)) {
                return true;
            } else {
                return false;
            }
            // 可执行
        } else if (methodAuthority.CONTROL_TYPE().equals(ControlType.EXECUTE) &&
                (("1").equals(limitLevel) || ("2").equals(limitLevel) || ("4").equals(limitLevel) || ("6").equals(limitLevel) ) ){
            return true;
        } else {// 为符合情况 返回false
            return false;
        }
    }

    /**
     * 获取返回信息
     *
     * @param code
     * @param msg
     * @return
     */
    private ResponseDTO getResponseDTO(String code, String msg) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(code);
        responseDTO.setMsg(msg);
        return responseDTO;
    }

    /**
     * 封装返回信息和状态码
     *
     * @param httpServletResponse
     * @param responseDTO
     * @throws IOException
     */
    private void returnMsg(HttpServletResponse httpServletResponse, ResponseDTO responseDTO) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(Integer.valueOf(responseDTO.getCode()));
        PrintWriter out = httpServletResponse.getWriter();
        out.append(JsonUtils.toJson(responseDTO));
    }

    /**
     * 获取当前登录用户缓存信息
     *
     * @return
     */
    private UserInfoDTO getUserInfo() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object object = securityContext.getAuthentication().getPrincipal();
        return smLoginService.getUserByUserIdToCache(((Account) object).getAccountId());
    }

    /**
     * 执行目标方法之后执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在请求已经返回之后执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
