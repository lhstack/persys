package com.lhstack.config.security.manager;

import com.lhstack.config.security.SecurityConst;
import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.User;
import com.lhstack.service.permission.IPermissionService;
import com.lhstack.service.permission.IRoleService;
import com.lhstack.service.permission.IUserService;
import com.lhstack.utils.PasswordEncoderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private PasswordEncoderUtils passwordEncoderUtils;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        log.info("access authenticate method : username : {} <-_->  password : {}",username,password);
        if(StringUtils.isEmpty(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        User user = queryResultUser(username);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("用户不存在");
        }
        if(!passwordEncoderUtils.matches(user.getSalt(),password,user.getPassword())){
            throw new BadCredentialsException("用户名密码输入有误");
        }
        if(user.getIsDel()){
            throw new AccountExpiredException("用户已经被删除，不可使用");
        }
        if(user.getIsLock()){
            throw new LockedException("用户已经被锁定，请与管理员联系");
        }

        List<GrantedAuthority> grantedAuthorities = getRolesAndPermission(user.getId());
        user.setPassword(null)
                .setSalt(null);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, password, grantedAuthorities);
        return Mono.just(usernamePasswordAuthenticationToken);
    }

    /**
     * 判断参数是邮箱还是用户名
     * @param arg
     * @return
     */
    private User queryResultUser(String arg) {
        if(arg.matches("^([0-9A-Za-z\\-_\\.]+)@([0-9a-z]+\\.[a-z]{2,3}(\\.[a-z]{2})?)$")){
            return userService.findByEmail(arg);
        }
        return userService.findByUsername(arg);
    }

    /**
     * 返回权限和角色
     * @param id
     * @return
     */
    private List<GrantedAuthority> getRolesAndPermission(Long id) {
        List<Role> roles = roleService.findByUid(id);
        List<Permission> permissions = new ArrayList<>();
        roles.forEach(r -> {
            List<Permission> permissionList = permissionService.findByRid(r.getId());
            permissions.addAll(permissionList);
        });
        List<GrantedAuthority> rolesAuthority = roles.stream().map(item -> new SimpleGrantedAuthority(SecurityConst.ROLE + item.getRoleName())).collect(Collectors.toList());
        List<GrantedAuthority> permissionsAuthority = permissions.stream().map(item -> new SimpleGrantedAuthority(SecurityConst.PERMISSION + item.getPermissionName())).collect(Collectors.toList());
        rolesAuthority.addAll(permissionsAuthority);
        return rolesAuthority;
    }
}
