package com.lhstack.authorization.holder;

import com.lhstack.authorization.autoconfig.PerSysSsoAutoConfiguration;
import com.lhstack.authorization.pojo.SsoAuthorityInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author: hp
 * date: 2020/1/2
 **/
public class AuthorizationClientHolder {

    /**
     * 获取当前线程的ServletRequestAttributes
     * @return
     */
    public static ServletRequestAttributes getServletRequestAttributes(){
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    /**
     * 获取当前线程的HttpServletRequest
     * @return
     */
    public static HttpServletRequest getCurrentHttpServletRequest(){
        return getServletRequestAttributes().getRequest();
    }

    /**
     * 获取当前线程的HttpServletResponse
     * @return
     */
    public static HttpServletResponse getCurrentHttpServletResponse(){
        return getServletRequestAttributes().getResponse();
    }

    /**
     * 获取当前线程的HttpSession
     * @return
     */
    public static HttpSession getHttpSession(){
        return getCurrentHttpServletRequest().getSession();
    }

    /**
     * 设置登陆用户
     * @return
     */
    public static void setAuthorization(SsoAuthorityInfo ssoAuthorityInfo){
        HttpSession httpSession = getHttpSession();
        httpSession.setAttribute(PerSysSsoAutoConfiguration.SSO_USER_INFO_KEY,ssoAuthorityInfo);
    }

    /**
     * 获取当前授权用户
     * @return
     */
    public static SsoAuthorityInfo getCurrentAuthorityInfo(){
        return (SsoAuthorityInfo) getHttpSession().getAttribute(PerSysSsoAutoConfiguration.SSO_USER_INFO_KEY);
    }
}
