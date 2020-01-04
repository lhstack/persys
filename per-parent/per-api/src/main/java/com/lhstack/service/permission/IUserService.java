package com.lhstack.service.permission;

import com.github.pagehelper.PageInfo;
import com.lhstack.entity.permission.User;
import com.lhstack.entity.permission.UserExampleDTO;
import com.lhstack.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService extends IBaseService<Long,User> {


    /**
     * 查询不包含超级管理员以及拥有USER_QUERY权限的用户并且带筛选条件
     * @return
     */
    PageInfo<User> findByNotExistThisAndExample(UserExampleDTO example);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 分页查询不包含本用户的数据
     * @param username
     * @param page
     * @param size
     * @return
     */
    Page<User> findByUsernameNotIs(String username,Integer page,Integer size);

    /**
     * 根据ids删除用户
     * @param ids
     */
    void deleteAllByIds(List<Long> ids);

    /**
     * 更新用户并关联角色
     * @param user
     * @param rids
     * @return
     */
    User updateAndRole(User user, List<Long> rids);

    /**
     * 保存用户，并关联角色
     * @param user
     * @param rids
     * @return
     */
    User saveUserAndRole(User user, List<Long> rids);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 注册用户
     * @param entity
     * @return
     */
    User registry(User entity);

    /**
     * 根据id删除用户和关联的角色
     * @param id
     * @return
     */
    Long deleteByIdAndRole(Long id);

    /**
     * 根据ids删除用户和关联的角色
     * @param ids
     * @return
     */
    Long deleteByIdsAndRole(List<Long> ids);

    User findByNickName(String nickName);
}
