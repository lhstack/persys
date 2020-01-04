package com.lhstack.service.permission.impl;

import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.aspect.sys.SysLog;
import com.lhstack.config.security.SecurityConst;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.controller.excontroller.InsertException;
import com.lhstack.controller.permission.UserController;
import com.lhstack.entity.permission.Menu;
import com.lhstack.mapper.permission.MenuMapper;
import com.lhstack.mapper.permission.PermissionMapper;
import com.lhstack.repository.permission.MenuRepository;
import com.lhstack.service.permission.IMenuService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private MenuRepository menuRepository;

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Override
    @Cacheable(cacheNames = "menus",key = "@genertorKey.getKey('findByAllMenu')")
    public List<Menu> findAllMenu(){
        List<Menu> menus = menuMapper.findAllMenuExample(initExample(0L));
        menus.forEach(item ->{
            if(item.getIsParent()){
                item.setChild(menuMapper.findAllMenuExample(initExample(item.getId())));
            }
        });
        return menus;
    }

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Override
    @Cacheable(cacheNames = "menus",key = "@genertorKey.getKey('findAllByChildMenu',#p0)")
    public List<Menu> findAllByChildMenu(Long parentId){
        return menuMapper.findAllMenuExample(initExample(parentId));
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "menus",key = "@genertorKey.getKey('findByParentId',#p0)")
    public List<Menu> findByParentId(Long parentId) {
        return menuRepository.findByParentId(parentId);
    }

    @Override
    @Cacheable(cacheNames = "menus",key = "@genertorKey.getKey('findAllMenuIsAdmin')")
    public List<Menu> findAllMenuIsAdmin() {
        Map<String,Object> map = new HashMap<>();
        map.put("isAdmin",true);
        map.put("parentId",0L);
        List<Menu> menus = menuMapper.findAllMenuExample(map);
        menus.forEach(item ->{
            if(item.getIsParent()){
                map.put("parentId",item.getId());
                item.setChild(menuMapper.findAllMenuExample(map));
            }
        });
        return menus;
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = {"menus","permissions"},allEntries = true)
    @SysLog
    public Menu updateAndPermission(Long id, Menu newM, Long[] pids) {
        if(id == null){
            throw new NullPointerException("请填写id");
        }
        newM.setId(id);
        if(newM.getIsParent() && newM.getParentId() != 0){
            newM.setParentId(0L);
        }
        int i = menuMapper.updateByPrimaryKeySelective(newM);
        checkMenuIsParent(newM);
        if(i > 0){
            permissionMapper.deleteMenuAndPermissionByMid(id);
            if(ArrayUtils.isNotEmpty(pids)){
                permissionMapper.insertMenuAndPermissionByMid(id,pids);
            }
            return newM;
        }
      throw new InsertException("更新菜单以及权限失败");
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = {"menus","permissions"},allEntries = true)
    @SysLog
    public Menu addAndPermission(Menu menu, Long[] pids) {
        menu.setCreateTime(new Date())
            .setId(null);
        if(menu.getIsParent()){
            menu.setParentId(0L);
        }
        Menu result = menuRepository.save(menu);
        if(ArrayUtils.isNotEmpty(pids)){
            permissionMapper.insertMenuAndPermissionByMid(result.getId(),pids);
        }
        return result;
    }

    @RollbackTransactional
    @CacheEvict(cacheNames = {"menus","permissions"},allEntries = true)
    @Override
    @SysLog
    public Integer deleteByIdAndPermission(Long id) {
        int i = menuMapper.deleteByPrimaryKey(checkMenu(id).getId());
        if(i > 0){
            permissionMapper.deleteMenuAndPermissionByMid(id);
        }
        return i;
    }

    private Menu checkMenu(Long mid) {
        Menu menu = menuMapper.selectByPrimaryKey(mid);
        if(menu != null && menu.getIsParent()){
            Long id = menu.getId();
            Map<String,Object> map = new HashMap<>();
            map.put("isAdmin",true);
            map.put("parentId",menu.getId());
            List<Menu> list = menuMapper.findAllMenuExample(map);
            if(!CollectionUtils.isEmpty(list)){
                list.forEach(item ->{
                    item.setParentId(0L);
                    menuMapper.updateByPrimaryKeySelective(item);
                });
            }
        }
        return menu;
    }


    //检查菜单是否是父级菜单，并修改子级菜单状态
    private void checkMenuIsParent(Menu menu) {
        if(!menu.getIsParent() && menu.getParentId() == 0){
            Long id = menu.getId();
            Map<String,Object> map = new HashMap<>();
            map.put("isAdmin",true);
            map.put("parentId",menu.getId());
            List<Menu> list = menuMapper.findAllMenuExample(map);
            if(!CollectionUtils.isEmpty(list)){
                list.forEach(item ->{
                    item.setParentId(0L);
                    menuMapper.updateByPrimaryKeySelective(item);
                });
            }
        }
    }

    /**
     * 构建findAllMenu查询条件
     * @return
     */
    private Map<String,Object> initExample(Long parentId) {
        Authentication authentication = SecurityContextHolder.get();
        if(authentication == null){
            throw new NullPointerException("请先登录");
        }
        Map<String,Object> map = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(item -> ((GrantedAuthority) item).getAuthority().equals(UserController.ROLE_ADMIN));
        if(isAdmin){
            map.put("isAdmin",true);
        }else{
            map.put("isAdmin",false);
            List<String> permissions = authorities.stream().filter(item -> ((GrantedAuthority) item).getAuthority().startsWith(SecurityConst.PERMISSION)).map(item -> ((GrantedAuthority) item).getAuthority().substring(SecurityConst.PERMISSION.length())).collect(Collectors.toList());
            map.put("permissions",permissions);
        }
        map.put("parentId",parentId);
        return map;
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "menus",allEntries = true)
    @SysLog
    public Menu save(Menu entity) {
        return menuRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "menus",allEntries = true)
    @SysLog
    public Menu update(Long id, Menu entity) {
        entity.setId(id);
        return menuRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "menus",allEntries = true)
    @SysLog
    public void delete(Menu entity) {
        menuRepository.delete(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "menus",allEntries = true)
    @SysLog
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "menus",key = "'findById' + @genertorKey.getKey(#p0)")
    public Menu findById(Long id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "menus",key = "'findAll' + @genertorKey.getKey('all')")
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "menus",key = "'findAll' + @genertorKey.getKey(#p0,#p1)")
    public Page<Menu> findAll(Integer page, Integer size) {
        if(page < 1){
            page = 1;
        }
        return menuRepository.findAll(PageRequest.of(page - 1,size));
    }
}
