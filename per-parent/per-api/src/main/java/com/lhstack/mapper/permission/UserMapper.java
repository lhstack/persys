package com.lhstack.mapper.permission;

import com.lhstack.entity.permission.User;
import com.lhstack.entity.permission.UserExampleDTO;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    Long deleteByIdsAndRole(List<Long> ids);

    Long deleteByIdAndRole(Long id);

    List<User> findByNotExistThisAndExample(UserExampleDTO example);
}
