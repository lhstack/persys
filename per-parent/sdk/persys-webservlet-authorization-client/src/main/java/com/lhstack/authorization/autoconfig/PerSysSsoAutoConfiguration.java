package com.lhstack.authorization.autoconfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhstack.authorization.pojo.SsoAuthorityInfo;
import com.lhstack.authorization.pojo.SsoPermission;
import com.lhstack.authorization.pojo.SsoRole;
import com.lhstack.authorization.pojo.SsoUserInfo;
import com.lhstack.authorization.properties.SsoProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * ClassName: PerSysSsoAutoConfiguration
 * Description:
 * date: 2019/12/24 21:36
 *
 * @author lhstack
 * @since
 */
@ConditionalOnExpression("${persys.enable:true}")
@EnableConfigurationProperties(SsoProperties.class)
@Slf4j
public class PerSysSsoAutoConfiguration {

    public static final String SSO_USER_INFO_KEY = "SSO_USER_INFO_KEY::";
    @Autowired
    private SsoProperties ssoProperties;

    @Bean
    public OncePerRequestFilter ssoFilter(){
        return new SsoFilter();
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> ssoServletRegistrationBean(){
        String redirectUrl = ssoProperties.getRedirectUrl();
        redirectUrl = redirectUrl.replaceAll("(http://|https://)","");
        redirectUrl = redirectUrl.substring(redirectUrl.indexOf("/"));
        redirectUrl = redirectUrl.charAt(redirectUrl.length() - 1) == '/' ? redirectUrl.substring(0,redirectUrl.length() - 1) : redirectUrl;
        return new ServletRegistrationBean<>(new SsoServlet(),redirectUrl);
    }

    private class SsoServlet extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String token = req.getParameter("access_token");
            try{
                if(StringUtils.equals(ssoProperties.getAuthorityType().toLowerCase(),"jwt")){
                    parseJwtTokenAndStoreInfo(req,resp,token);
                }else{
                    throw new IllegalAccessError("请使用jwt类型的sso");
                }
            }catch (Exception e){
                log.error("token 不合法，重定向到登陆页面");
                resp.sendRedirect(ssoProperties.getLoginUrl() + "?token=" + ssoProperties.getSsoToken() + "&service=" + ssoProperties.getRedirectUrl());
            }
        }


        private void parseJwtTokenAndStoreInfo(HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(ssoProperties.getSingingKey())
                    .parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            HashMap userInfo = body.get("userInfo", HashMap.class);
            List roles = body.get("roles", ArrayList.class);
            List permissions = body.get("permissions", ArrayList.class);
            SsoUserInfo ssoUserInfo = JSONObject.parseObject(JSONObject.toJSONString(userInfo),SsoUserInfo.class);
            List<SsoRole> ssoRoles = JSONArray.parseArray(JSONArray.toJSONString(roles), SsoRole.class);
            List<SsoPermission> ssoPermissions = JSONArray.parseArray(JSONArray.toJSONString(permissions), SsoPermission.class);
            SsoAuthorityInfo ssoAuthorityInfo = new SsoAuthorityInfo();
            ssoAuthorityInfo.setPermissionList(ssoPermissions == null ? new ArrayList<>() : ssoPermissions )
                    .setRoleList(ssoRoles == null ? new ArrayList<>() : ssoRoles)
                    .setUserInfo(ssoUserInfo);
            req.getSession().setAttribute(SSO_USER_INFO_KEY,ssoAuthorityInfo);

            resp.sendRedirect(req.getContextPath() + ssoProperties.getSuccessUrl());
        }
    }

    private class SsoFilter extends OncePerRequestFilter{

        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            String prefix = ssoProperties.getPrefix();
            if(StringUtils.isBlank(prefix)){
                log.error("ssoProperties --> prefix is not setting");
                filterChain.doFilter(httpServletRequest,httpServletResponse);
                return ;
            }
            prefix = prefix.charAt(prefix.length() - 1) == '/' ? prefix.substring(0,prefix.length() - 1) : prefix;
            if(StringUtils.startsWith(httpServletRequest.getRequestURI(),prefix)){
                handlerIsInterceptorRequest(httpServletRequest,httpServletResponse,filterChain);
                return ;
            }
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }

        private void handlerIsInterceptorRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
            HttpSession session = request.getSession();
            if(ObjectUtils.isNotEmpty(session.getAttribute(SSO_USER_INFO_KEY)) && ObjectUtils.isNotEmpty(((SsoAuthorityInfo)session.getAttribute(SSO_USER_INFO_KEY)).getUserInfo())){
                filterChain.doFilter(request,response);
                return ;
            }
            if(isIngoreUrl(request)){
                filterChain.doFilter(request,response);
                return ;
            }
            String loginUrl = ssoProperties.getLoginUrl();
            response.sendRedirect(loginUrl + "?token=" + ssoProperties.getSsoToken() + "&service=" + ssoProperties.getRedirectUrl());
        }

        private boolean isIngoreUrl(HttpServletRequest request) {
            String requestURI = request.getRequestURI();
            Set<String> ingoreUrl = ssoProperties.getIngoreUrl();
            String redirectUrl = ssoProperties.getRedirectUrl();
            redirectUrl = redirectUrl.replaceAll("(http://|https://)","");
            redirectUrl = redirectUrl.substring(redirectUrl.indexOf("/"));
            redirectUrl = redirectUrl.charAt(redirectUrl.length() - 1) == '/' ? redirectUrl.substring(0,redirectUrl.length() - 1) : redirectUrl;
            return ingoreUrl != null && ingoreUrl.stream().anyMatch(item ->{
                if(item.endsWith("**")){
                    return requestURI.startsWith(item.substring(0,item.length() - 3));
                }else{
                    item = item.charAt(item.length() - 1) == '/' ? item.substring(0,item.length() - 1) : item;
                    return StringUtils.equals(requestURI,item);
                }
            }) || StringUtils.equals(requestURI,redirectUrl);
        }
    }
}

