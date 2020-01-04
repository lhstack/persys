package com.lhstack.config.security.holder;

import com.lhstack.config.security.SecurityConst;
import com.lhstack.entity.permission.User;
import com.lhstack.utils.IpUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 存储当前用户信息
 */
public class SecurityContextHolder implements WebFilter {

    private static ThreadLocal<Authentication> local = new ThreadLocal<>();

    private static ThreadLocal<ServerWebExchange> webExchangeThreadLocal = new ThreadLocal<>();

    public static Collection<? extends GrantedAuthority> getAuthorities(){
        Authentication authentication = local.get();
        if(authentication == null){
            return new ArrayList<>();
        }
        return authentication.getAuthorities();
    }

    public static  List<String> getAuthoritiesFilterToString(Predicate<? super GrantedAuthority> predicate){
       return getAuthorities().stream().filter(predicate).map(item -> {
           String authority = ((GrantedAuthority) item).getAuthority();
           return authority.substring(authority.indexOf("_") + 1);
       }).collect(Collectors.toList());
    }

    public static List<String> getPermissions() {
        return getAuthoritiesFilterToString(item -> item.getAuthority().startsWith(SecurityConst.PERMISSION));
    }

    public static List<String> getRoles() {
        return getAuthoritiesFilterToString(item -> item.getAuthority().startsWith(SecurityConst.ROLE));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        Mono<WebSession> session = serverWebExchange.getSession();
        local.remove();
        SecurityContext securityContext = session.block().getAttribute(SecurityConst.SPRING_SECURITY_CONTEXT);
        if(securityContext != null){
            local.set(securityContext.getAuthentication());
        }
        webExchangeThreadLocal.remove();
        webExchangeThreadLocal.set(serverWebExchange);
        return webFilterChain.filter(serverWebExchange);
    }

    public static Authentication get() {
        return local.get();
    }

    public static String getUsername(){
       try{
           User user = (User) get().getPrincipal();
           return user.getUsername();
       }catch (Exception e){
           return "TEST";
       }
    }
    public static String getIp(){
        ServerWebExchange serverWebExchange = webExchangeThreadLocal.get();
        if(serverWebExchange == null){
            return "localhost";
        }
        return IpUtils.getIp(serverWebExchange.getRequest());
    }
}
